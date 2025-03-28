import java.util.*;
import java.util.List;

public class NewsAggregatorApp {
    Map<Integer, Article> articlesById = new HashMap<>();
    Map<String, Set<Integer>> articlesByKeyword = new HashMap<>();
    PriorityQueue<Article> trendingHeap = new PriorityQueue<>((a1, a2) -> Integer.compare(a2.popularity, a1.popularity));

    public NewsAggregatorApp() {
        initSampleData();
        new NewsAggregatorUI(this);
    }

    private void initSampleData() {
        ingestArticle(new Article(1, "Breaking News: Java 21 Released", "Java 21 includes exciting new features...",
                Arrays.asList("java", "programming", "tech"), "2025-03-20", 150));
        ingestArticle(new Article(2, "Stock Market Update", "Tech stocks soar after strong earnings...",
                Arrays.asList("stocks", "economy", "finance"), "2025-03-21", 85));
        ingestArticle(new Article(3, "AI Research Advances", "Researchers announce a new AI breakthrough...",
                Arrays.asList("ai", "research", "tech"), "2025-03-19", 230));
        ingestArticle(new Article(4, "Sports Championship Highlights", "The final match was a thrilling encounter...",
                Arrays.asList("sports", "championship"), "2025-03-22", 100));
        ingestArticle(new Article(5, "Local Food Festival", "Annual gourmet festival sees record turnout...",
                Arrays.asList("food", "festival", "local"), "2025-03-18", 40));
    }

    private void ingestArticle(Article article) {
        articlesById.put(article.id, article);
        for (String kw : article.keywords) {
            articlesByKeyword.computeIfAbsent(kw, k -> new HashSet<>()).add(article.id);
        }
        trendingHeap.add(article);
    }

    public static void main(String[] args) {
        new NewsAggregatorApp();
    }
}
