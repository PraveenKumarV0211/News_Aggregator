// Updated NewsAggregatorApp to use custom data structures

public class NewsAggregatorApp {
    MyMap<Integer, Article> articlesById = new MyMap<>();
    MyMap<String, MySet<Integer>> articlesByKeyword = new MyMap<>();
    MyHeap<Article> trendingHeap = new MyHeap<>((a1, a2) -> Integer.compare(a2.popularity, a1.popularity));

    public NewsAggregatorApp() {
        initSampleData();
        new NewsAggregatorUI(this);
    }

    private void initSampleData() {
        ingestArticle(new Article(1, "Breaking News: Java 21 Released",
                "Java 21 includes exciting new features and enhancements that streamline development and improve runtime performance. " +
                        "Key additions include pattern matching for switch statements, virtual threads for improved concurrency, and record patterns. " +
                        "These updates aim to simplify code and reduce boilerplate, making Java more modern and expressive. " +
                        "Developers around the world are already exploring the new capabilities and reporting positive feedback on its impact in production environments. " +
                        "With these features, Java continues to stay competitive in the evolving software development ecosystem.",
                new MyList<>(new String[]{"java", "programming", "tech"}), "2025-03-20", 150));

        ingestArticle(new Article(2, "Stock Market Update",
                "Tech stocks soar after strong earnings reports from major companies like Apple, Microsoft, and Google. " +
                        "Investors are showing renewed confidence as quarterly results beat analyst expectations. " +
                        "The NASDAQ index jumped by 3.5% in a single day, fueled by growth in AI and cloud computing divisions. " +
                        "Meanwhile, economic data shows consumer spending remains stable, easing recession fears. " +
                        "Financial analysts suggest that this may signal a rebound for the broader economy. " +
                        "Small-cap tech firms are also experiencing upward momentum, suggesting a potential bull run on the horizon.",
                new MyList<>(new String[]{"stocks", "economy", "finance"}), "2025-03-21", 85));

        ingestArticle(new Article(3, "AI Research Advances",
                "Researchers announce a new AI breakthrough that significantly improves natural language understanding and reasoning capabilities. " +
                        "The system, trained on multimodal data including images and video, demonstrates superior performance on benchmark tests. " +
                        "This advancement is seen as a step closer to general AI, with the potential to revolutionize industries from healthcare to education. " +
                        "The research team emphasized ethical considerations, open sourcing part of the model to encourage transparency. " +
                        "Experts are calling this a major leap in deep learning, comparable to the release of transformers or diffusion models.",
                new MyList<>(new String[]{"ai", "research", "tech"}), "2025-03-19", 230));

        ingestArticle(new Article(4, "Sports Championship Highlights",
                "The final match of the national championship was a thrilling encounter that kept fans on the edge of their seats. " +
                        "Team Thunderhawks faced off against the defending champions, the Silver Blades, in a neck-and-neck contest. " +
                        "With a last-minute goal in overtime, Thunderhawks secured a historic win, ending a decade-long title drought. " +
                        "Commentators praised the strategy and resilience shown by both teams. " +
                        "The stadium was packed with over 50,000 fans, and millions tuned in worldwide. " +
                        "The MVP of the match, forward James Daniels, delivered a stunning performance that sealed the victory.",
                new MyList<>(new String[]{"sports", "championship"}), "2025-03-22", 100));

        ingestArticle(new Article(5, "Local Food Festival",
                "The annual gourmet food festival saw record turnout this year, with thousands gathering to enjoy local delicacies and international cuisines. " +
                        "From artisan pastries to authentic street food, the event offered a vibrant celebration of culinary diversity. " +
                        "Live music, cooking demos, and farmer’s markets added to the festive atmosphere. " +
                        "Organizers emphasized sustainability, with compostable utensils and local sourcing. " +
                        "Several up-and-coming chefs showcased their creativity, earning praise from critics and food lovers alike. " +
                        "The event not only boosted tourism but also raised funds for local food banks and community kitchens.",
                new MyList<>(new String[]{"food", "festival", "local"}), "2025-03-18", 40));

    }

    private void ingestArticle(Article article) {
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
        MyList<Article> all = new MyList<>();
        for (int i = 0; i < articlesById.bucketCount(); i++) {
            MyList<Article> bucket = articlesById.getBucket(i);
            for (int j = 0; j < bucket.size(); j++) {
                all.add(bucket.get(j));
            }
        }
        return all;
    }


    public MyList<Article> getArticlesByKeyword(String keyword) {
        MyList<Article> result = new MyList<>(new String[]{"java", "programming", "tech"});
        MySet<Integer> ids = articlesByKeyword.get(keyword);
        if (ids != null) {
            MyList<Integer> idList = ids.getAll();
            for (int i = 0; i < idList.size(); i++) {
                Article art = articlesById.get(idList.get(i));
                if (art != null) result.add(art);
            }
        }
        return result;
    }

    public MyList<Article> getTopTrendingArticles(int topN) {
        MyList<Article> sorted = trendingHeap.toSortedList();
        MyList<Article> top = new MyList<>(new String[]{"java", "programming", "tech"});
        for (int i = 0; i < topN && i < sorted.size(); i++) {
            top.add(sorted.get(i));
        }
        return top;
    }

    public static void main(String[] args) {
        new NewsAggregatorApp();
    }
}

// Updated Article class to use MyList

// Updated NewsAggregatorUI class to use custom data structures
// You can now build the UI methods (onSearch, onSortByDate, etc.) using MyList<Article>
// Instead of relying on Java collections, loop through MyList elements like shown above.
