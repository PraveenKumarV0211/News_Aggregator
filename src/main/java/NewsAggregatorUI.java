class NewsAggregatorUI extends javax.swing.JFrame {
    private final NewsAggregatorApp app;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel articleListPanel;

    public NewsAggregatorUI(NewsAggregatorApp app) {
        this.app = app;
        buildUI();
        setTitle("News Aggregator");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildUI() {
        javax.swing.JPanel topPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        searchField = new javax.swing.JTextField(15);
        javax.swing.JButton searchButton = new javax.swing.JButton("Search Keyword");
        javax.swing.JButton showTrendingButton = new javax.swing.JButton("Show Top Trending");
        javax.swing.JButton sortByDateButton = new javax.swing.JButton("Sort by Date");
        javax.swing.JButton sortByPopularityButton = new javax.swing.JButton("Sort by Popularity");

        topPanel.add(new javax.swing.JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showTrendingButton);
        topPanel.add(sortByDateButton);
        topPanel.add(sortByPopularityButton);

        articleListPanel = new javax.swing.JPanel();
        articleListPanel.setLayout(new javax.swing.BoxLayout(articleListPanel, javax.swing.BoxLayout.Y_AXIS));
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(articleListPanel);

        add(topPanel, java.awt.BorderLayout.NORTH);
        add(scrollPane, java.awt.BorderLayout.CENTER);

        searchButton.addActionListener(
                e -> loadArticles(app.getArticlesByKeyword(searchField.getText().trim().toLowerCase())));
        showTrendingButton.addActionListener(e -> loadArticles(app.getTopTrendingArticles(3)));
        sortByDateButton.addActionListener(e -> sortAndReload("date"));
        sortByPopularityButton.addActionListener(e -> sortAndReload("popularity"));

        loadArticles(app.getAllArticles());
    }

    private void loadArticles(MyList<Article> articles) {
        articleListPanel.removeAll();
        for (int i = 0; i < articles.size(); i++) {
            addArticleCard(articles.get(i));
        }
        articleListPanel.revalidate();
        articleListPanel.repaint();
    }

    private void addArticleCard(Article article) {
        javax.swing.JPanel card = new javax.swing.JPanel(new java.awt.BorderLayout());
        card.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        card.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 70));

        javax.swing.JLabel titleLabel = new javax.swing.JLabel(
                "<html><b>" + article.title + "</b> (" + article.date + ")</html>");
        javax.swing.JButton viewButton = new javax.swing.JButton("View");

        viewButton.addActionListener(e -> openEnhancedArticleWindow(article));

        card.add(titleLabel, java.awt.BorderLayout.CENTER);
        card.add(viewButton, java.awt.BorderLayout.EAST);

        articleListPanel.add(card);
    }

    private void openArticleWindow(Article article) {
        javax.swing.JFrame detailFrame = new javax.swing.JFrame(article.title);
        javax.swing.JTextArea contentArea = new javax.swing.JTextArea(article.content);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);

        detailFrame.add(new javax.swing.JScrollPane(contentArea));
        detailFrame.setSize(400, 300);
        detailFrame.setLocationRelativeTo(this);
        detailFrame.setVisible(true);
    }

    private void openEnhancedArticleWindow(Article article) {

        javax.swing.JFrame detailFrame = new javax.swing.JFrame("AI-Enhanced: " + article.title);
        detailFrame.setSize(700, 600);
        detailFrame.setLocationRelativeTo(this);


        javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();


        javax.swing.JPanel originalPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JTextArea contentArea = new javax.swing.JTextArea(article.content);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        originalPanel.add(new javax.swing.JScrollPane(contentArea), java.awt.BorderLayout.CENTER);


        javax.swing.JPanel summaryPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JTextArea summaryArea = new javax.swing.JTextArea("Generating summary with AI...");
        summaryArea.setWrapStyleWord(true);
        summaryArea.setLineWrap(true);
        summaryArea.setEditable(false);
        summaryArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        summaryPanel.add(new javax.swing.JScrollPane(summaryArea), java.awt.BorderLayout.CENTER);


        javax.swing.JPanel sentimentPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JTextArea sentimentArea = new javax.swing.JTextArea("Analyzing sentiment with AI...");
        sentimentArea.setWrapStyleWord(true);
        sentimentArea.setLineWrap(true);
        sentimentArea.setEditable(false);
        sentimentArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        sentimentPanel.add(new javax.swing.JScrollPane(sentimentArea), java.awt.BorderLayout.CENTER);


        javax.swing.JPanel relatedPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JTextArea relatedArea = new javax.swing.JTextArea("Finding related topics with AI...");
        relatedArea.setWrapStyleWord(true);
        relatedArea.setLineWrap(true);
        relatedArea.setEditable(false);
        relatedArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        relatedPanel.add(new javax.swing.JScrollPane(relatedArea), java.awt.BorderLayout.CENTER);

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Original", originalPanel);
        tabbedPane.addTab("AI Summary", summaryPanel);
        tabbedPane.addTab("Sentiment", sentimentPanel);
        tabbedPane.addTab("Related Topics", relatedPanel);

        // Add tabbed pane to the frame
        detailFrame.add(tabbedPane);

        // Show the frame
        detailFrame.setVisible(true);

        // Perform OpenAI operations in background threads

        // Get summary
        new Thread(() -> {
            try {
                String summary = app.summarizeArticle(article);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    summaryArea.setText(summary);
                });
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    summaryArea.setText("Error generating summary: " + ex.getMessage());
                });
            }
        }).start();

        // Get sentiment analysis
        new Thread(() -> {
            try {
                String sentiment = app.analyzeArticleSentiment(article);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    // Format the sentiment with some additional context
                    String formattedSentiment = "Sentiment Analysis: " + sentiment + "\n\n";
                    formattedSentiment += "This article has been analyzed as having a " + sentiment.toLowerCase() +
                            " sentiment. This may indicate the overall tone and perspective of the content.";
                    sentimentArea.setText(formattedSentiment);
                });
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    sentimentArea.setText("Error analyzing sentiment: " + ex.getMessage());
                });
            }
        }).start();

        // Get related topics
        new Thread(() -> {
            try {
                String relatedTopics = app.getRelatedTopics(article);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    relatedArea.setText(relatedTopics);
                });
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    relatedArea.setText("Error finding related topics: " + ex.getMessage());
                });
            }
        }).start();
    }

    private void sortAndReload(String sortBy) {
        MyList<Article> articles = app.getAllArticles();
        for (int i = 0; i < articles.size(); i++) {
            for (int j = i + 1; j < articles.size(); j++) {
                boolean condition = sortBy.equals("date") ? articles.get(i).date.compareTo(articles.get(j).date) < 0
                        : articles.get(i).popularity < articles.get(j).popularity;
                if (!condition) {
                    Article temp = articles.get(i);
                    articles.set(i, articles.get(j));
                    articles.set(j, temp);
                }
            }
        }
        loadArticles(articles);
    }
}
