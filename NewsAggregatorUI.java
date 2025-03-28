import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class NewsAggregatorUI extends JFrame {
    private final NewsAggregatorApp app;

    private JTextField searchField;
    private JPanel articleListPanel;

    public NewsAggregatorUI(NewsAggregatorApp app) {
        this.app = app;
        buildUI();
        setTitle("News Aggregator");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search Keyword");
        JButton showTrendingButton = new JButton("Show Top Trending");
        JButton sortByDateButton = new JButton("Sort by Date");
        JButton sortByPopularityButton = new JButton("Sort by Popularity");

        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showTrendingButton);
        topPanel.add(sortByDateButton);
        topPanel.add(sortByPopularityButton);

        articleListPanel = new JPanel();
        articleListPanel.setLayout(new BoxLayout(articleListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(articleListPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> onSearch());
        showTrendingButton.addActionListener(e -> onShowTrending());
        sortByDateButton.addActionListener(e -> onSortByDate());
        sortByPopularityButton.addActionListener(e -> onSortByPopularity());

        loadAllArticlesToPanel(app.articlesById.values());
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
            loadAllArticlesToPanel(app.articlesById.values());
            return;
        }

        if (!app.articlesByKeyword.containsKey(keyword)) {
            JOptionPane.showMessageDialog(this, "No articles found for keyword: " + keyword);
            return;
        }

        Set<Integer> ids = app.articlesByKeyword.get(keyword);
        for (Integer id : ids) {
            Article art = app.articlesById.get(id);
            addArticleCard(art);
        }
    }

    private void onShowTrending() {
        articleListPanel.removeAll();
        List<Article> topArticles = new ArrayList<>(app.trendingHeap);
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
                        for (Article a : app.articlesById.values()) {
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
}
