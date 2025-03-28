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
        ingestArticle(new Article(1, "Breaking News: Java 21 Released", "Java 21 includes exciting new features...",
                new MyList<>(new String[]{"java", "programming", "tech"}), "2025-03-20", 150));
        ingestArticle(new Article(2, "Stock Market Update", "Tech stocks soar after strong earnings...",
                new MyList<>(new String[]{"stocks", "economy", "finance"}), "2025-03-21", 85));
        ingestArticle(new Article(3, "AI Research Advances", "Researchers announce a new AI breakthrough...",
                new MyList<>(new String[]{"ai", "research", "tech"}), "2025-03-19", 230));
        ingestArticle(new Article(4, "Sports Championship Highlights", "The final match was a thrilling encounter...",
                new MyList<>(new String[]{"sports", "championship"}), "2025-03-22", 100));
        ingestArticle(new Article(5, "Local Food Festival", "Annual gourmet festival sees record turnout...",
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
