import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NewsAggregatorApp extends JFrame {
    private Map<Integer, Article> articlesById = new HashMap<>();
    private Map<String, Set<Integer>> articlesByKeyword = new HashMap<>();
    private PriorityQueue<Article> trendingHeap = new PriorityQueue<>((a1, a2) -> Integer.compare(a2.popularity, a1.popularity));

    private JTextField searchField;
    private JButton searchButton, showTrendingButton, sortByDateButton, sortByPopularityButton;
    private JPanel articleListPanel;
    private JScrollPane scrollPane;

    public NewsAggregatorApp() {
        super("News Aggregator with View Buttons");
        initSampleData();
        buildUI();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    private void buildUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search Keyword");
        showTrendingButton = new JButton("Show Top Trending");
        sortByDateButton = new JButton("Sort by Date");
        sortByPopularityButton = new JButton("Sort by Popularity");

        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showTrendingButton);
        topPanel.add(sortByDateButton);
        topPanel.add(sortByPopularityButton);

        articleListPanel = new JPanel();
        articleListPanel.setLayout(new BoxLayout(articleListPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(articleListPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> onSearch());
        showTrendingButton.addActionListener(e -> onShowTrending());
        sortByDateButton.addActionListener(e -> onSortByDate());
        sortByPopularityButton.addActionListener(e -> onSortByPopularity());

        loadAllArticlesToPanel(articlesById.values());
    }

    private void addArticleCard(Article article) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel titleLabel = new JLabel("<html><b>" + article.title + "</b> (" + article.date + ")</html>");
        JButton viewButton = new JButton("View");

        viewButton.addActionListener(e -> openArticleWindow(article));

        card.add(titleLabel, BorderLayout.CENTER);
        card.add(viewButton, BorderLayout.EAST);

        articleListPanel.add(card);
        articleListPanel.revalidate();
        articleListPanel.repaint();
    }

    private void openArticleWindow(Article article) {
        JFrame detailFrame = new JFrame(article.title);
        JTextArea contentArea = new JTextArea(article.content);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);

        detailFrame.add(new JScrollPane(contentArea));
        detailFrame.setSize(400, 300);
        detailFrame.setLocationRelativeTo(this);
        detailFrame.setVisible(true);
    }

    private void loadAllArticlesToPanel(Collection<Article> articles) {
        articleListPanel.removeAll();
        for (Article article : articles) {
            addArticleCard(article);
        }
    }

    private void onSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        articleListPanel.removeAll();

        if (keyword.isEmpty()) {
            loadAllArticlesToPanel(articlesById.values());
            return;
        }

        if (!articlesByKeyword.containsKey(keyword)) {
            JOptionPane.showMessageDialog(this, "No articles found for keyword: " + keyword);
            return;
        }

        Set<Integer> ids = articlesByKeyword.get(keyword);
        for (Integer id : ids) {
            Article art = articlesById.get(id);
            addArticleCard(art);
        }
    }

    private void onShowTrending() {
        articleListPanel.removeAll();
        List<Article> topArticles = new ArrayList<>(trendingHeap);
        topArticles.sort((a1, a2) -> Integer.compare(a2.popularity, a1.popularity));
        for (int i = 0; i < Math.min(3, topArticles.size()); i++) {
            addArticleCard(topArticles.get(i));
        }
    }

    private void onSortByDate() {
        List<Article> articles = getCurrentDisplayedArticles();
        articles.sort((a1, a2) -> a2.date.compareTo(a1.date));
        loadAllArticlesToPanel(articles);
    }

    private void onSortByPopularity() {
        List<Article> articles = getCurrentDisplayedArticles();
        articles.sort((a1, a2) -> Integer.compare(a2.popularity, a1.popularity));
        loadAllArticlesToPanel(articles);
    }

    private List<Article> getCurrentDisplayedArticles() {
        List<Article> displayed = new ArrayList<>();
        for (Component comp : articleListPanel.getComponents()) {
            if (comp instanceof JPanel panel) {
                for (Component c : panel.getComponents()) {
                    if (c instanceof JLabel label) {
                        String text = label.getText();
                        for (Article a : articlesById.values()) {
                            if (text.contains(a.title)) {
                                displayed.add(a);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return displayed;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NewsAggregatorApp app = new NewsAggregatorApp();
            app.setVisible(true);
        });
    }
}
