
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class NewsAggregatorApp {
    MyMap<Integer, Article> articlesById = new MyMap<>();
    MyMap<String, MySet<Integer>> articlesByKeyword = new MyMap<>();
    MyHeap<Article> trendingHeap = new MyHeap<>((a1, a2) -> Integer.compare(a1.popularity, a2.popularity));

    private OpenAIService openAIService;
    private static final String DEFAULT_API_KEY = "your-api-key-here";
    private Properties config = new Properties();

    public NewsAggregatorApp() {
        loadConfig();
        initializeOpenAI();
        insertSampleData();
        new NewsAggregatorUI(this);
    }

    private void loadConfig() {
        try (InputStream input = new FileInputStream("config.properties")) {
            config.load(input);
            System.out.println("Configuration loaded successfully.");
        } catch (Exception e) {
            System.err.println("Warning: Failed to load configuration file: " + e.getMessage());
            System.err.println("Using default settings.");
        }
    }

    private void initializeOpenAI() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = config.getProperty("openai.api.key", DEFAULT_API_KEY);
        }

        try {
            openAIService = new OpenAIService(apiKey);
            System.out.println("OpenAI service initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize OpenAI service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertSampleData() {
        List<Article> loadedArticles = ArticleFileLoader.loadArticlesFromDirectory("src/articles");
        for (Article article : loadedArticles) {
            insertArticle(article);
        }
    }
    private void insertArticle(Article article) {
        articlesById.put(article.id, article);
        for (int i = 0; i < article.keywords.size(); i++) {
            String kw = article.keywords.get(i);
            MySet<Integer> ids = articlesByKeyword.get(kw);
            if (ids == null) {
                ids = new MySet<>();
                articlesByKeyword.put(kw, ids);
            }
            ids.add(article.id);
        }
        trendingHeap.add(article);
    }

    public MyList<Article> getAllArticles() {
        MyList<Article> allarticles = new MyList<>();
        for (int i = 0; i < articlesById.bucketCount(); i++) {
            MyList<Article> bucket = articlesById.getBucket(i);
            for (int j = 0; j < bucket.size(); j++) {
                allarticles.add(bucket.get(j));
            }
        }
        return allarticles;
    }

    public MyList<Article> getArticlesByKeyword(String keyword) {
        MyList<Article> result = new MyList<>();
        MySet<Integer> ids = articlesByKeyword.get(keyword);
        if (ids != null) {
            MyList<Integer> idList = ids.getAll();
            for (int i = 0; i < idList.size(); i++) {
                Article art = articlesById.get(idList.get(i));
                if (art != null)
                    result.add(art);
            }
        }
        return result;
    }

    public MyList<Article> getTopTrendingArticles(int topN) {
        MyList<Article> sorted = trendingHeap.toSortedList();
        MyList<Article> top = new MyList<>();
        for (int i = 0; i < topN && i < sorted.size(); i++) {
            top.add(sorted.get(i));
        }
        return top;
    }

    /**
     * Summarize an article using OpenAI
     *
     * @param article The article to summarize
     * @return A summarized version of the article content
     */
    public String summarizeArticle(Article article) {
        if (openAIService == null) {
            return "OpenAI service not available. Please check your API key.";
        }
        try {
            return openAIService.summarizeArticle(article.content);
        } catch (Exception e) {
            System.err.println("Error summarizing article: " + e.getMessage());
            return "Error summarizing article. Please try again later.";
        }
    }

    /**
     * Analyze the sentiment of an article using OpenAI
     *
     * @param article The article to analyze
     * @return The sentiment analysis result
     */
    public String analyzeArticleSentiment(Article article) {
        if (openAIService == null) {
            return "OpenAI service not available. Please check your API key.";
        }
        try {
            return openAIService.analyzeSentiment(article.content);
        } catch (Exception e) {
            System.err.println("Error analyzing sentiment: " + e.getMessage());
            return "Error analyzing sentiment. Please try again later.";
        }
    }

    /**
     * Get related topics for an article using OpenAI
     *
     * @param article The article to analyze
     * @return A list of related topics
     */
    public String getRelatedTopics(Article article) {
        if (openAIService == null) {
            return "OpenAI service not available. Please check your API key.";
        }
        try {
            return openAIService.suggestRelatedTopics(article.content);
        } catch (Exception e) {
            System.err.println("Error getting related topics: " + e.getMessage());
            return "Error getting related topics. Please try again later.";
        }
    }

    public static void main(String[] args) {
        new NewsAggregatorApp();
    }
}
