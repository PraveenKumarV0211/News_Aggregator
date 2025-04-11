class NewsAggregatorUI extends javax.swing.JFrame {
    private final NewsAggregatorApp app;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel articleListPanel;
    private javax.swing.JLabel statusLabel;

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
        // Header panel
        javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JLabel titleLabel = new javax.swing.JLabel("Boston Daily Times", javax.swing.SwingConstants.LEFT);
        java.time.LocalDate today = java.time.LocalDate.now();
        javax.swing.JLabel dateLabel = new javax.swing.JLabel("Date: " + today + " | Location: Boston", javax.swing.SwingConstants.RIGHT);

        titleLabel.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 24));
        dateLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
        headerPanel.add(titleLabel, java.awt.BorderLayout.WEST);
        headerPanel.add(dateLabel, java.awt.BorderLayout.EAST);

        // Top panel with search + sort
        javax.swing.JPanel topPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        searchField = new javax.swing.JTextField(15);
        javax.swing.JButton searchButton = new javax.swing.JButton("Search");
        javax.swing.JButton showTrendingButton = new javax.swing.JButton("Show Top Trending");
        javax.swing.JButton resetButton = new javax.swing.JButton("Reset");

        String[] sortOptions = {"Sort by Date", "Sort by Popularity"};
        javax.swing.JComboBox<String> sortDropdown = new javax.swing.JComboBox<>(sortOptions);

        topPanel.add(new javax.swing.JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showTrendingButton);
        topPanel.add(sortDropdown);
        topPanel.add(resetButton);

        // Article list
        articleListPanel = new javax.swing.JPanel();
        articleListPanel.setLayout(new javax.swing.BoxLayout(articleListPanel, javax.swing.BoxLayout.Y_AXIS));
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(articleListPanel);

        // Status bar
        statusLabel = new javax.swing.JLabel("Welcome to Boston Daily Times!");
        statusLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Combine panels
        javax.swing.JPanel northPanel = new javax.swing.JPanel();
        northPanel.setLayout(new javax.swing.BoxLayout(northPanel, javax.swing.BoxLayout.Y_AXIS));
        northPanel.add(headerPanel);
        northPanel.add(topPanel);

        add(northPanel, java.awt.BorderLayout.NORTH);
        add(scrollPane, java.awt.BorderLayout.CENTER);
        add(statusLabel, java.awt.BorderLayout.SOUTH);

        // Listeners
        searchField.addActionListener(e -> searchButton.doClick());
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            loadArticles(app.getArticlesByKeyword(keyword));
            statusLabel.setText("Search results for: '" + keyword + "'");
        });

        showTrendingButton.addActionListener(e -> {
            loadArticles(app.getTopTrendingArticles(3));
            statusLabel.setText("Top 3 trending articles.");
        });

        sortDropdown.addActionListener(e -> {
            String selected = (String) sortDropdown.getSelectedItem();
            sortAndReload(selected.contains("Date") ? "date" : "popularity");
            statusLabel.setText("Articles sorted by " + selected.toLowerCase() + ".");
        });

        resetButton.addActionListener(e -> {
            searchField.setText("");
            loadArticles(app.getAllArticles());
            statusLabel.setText("Reset to all articles.");
        });

        // Initial load
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
        card.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 90));

        javax.swing.JLabel titleLabel = new javax.swing.JLabel(
                "<html><b>" + article.title + "</b> (" + article.date + ")</html>");
        javax.swing.JButton viewButton = new javax.swing.JButton("View");

        javax.swing.JLabel popularityLabel = new javax.swing.JLabel("🔥 Popularity: " + article.popularity);
        popularityLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.ITALIC, 12));
        popularityLabel.setForeground(java.awt.Color.DARK_GRAY);

        viewButton.addActionListener(e -> openEnhancedArticleWindow(article));

        javax.swing.JPanel centerPanel = new javax.swing.JPanel();
        centerPanel.setLayout(new javax.swing.BoxLayout(centerPanel, javax.swing.BoxLayout.Y_AXIS));
        centerPanel.add(titleLabel);
        centerPanel.add(popularityLabel);


        if (article.keywords != null && article.keywords.size() > 0) {
            StringBuilder keywordStr = new StringBuilder("Keywords: ");
            for (int i = 0; i < article.keywords.size(); i++) {
                keywordStr.append(article.keywords.get(i));
                if (i < article.keywords.size() - 1) keywordStr.append(", ");
            }
            javax.swing.JLabel keywordsLabel = new javax.swing.JLabel("<html><i>" + keywordStr + "</i></html>");
            centerPanel.add(keywordsLabel);
        }

        card.add(centerPanel, java.awt.BorderLayout.CENTER);
        card.add(viewButton, java.awt.BorderLayout.EAST);
        articleListPanel.add(card);
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

        javax.swing.JButton copyButton = new javax.swing.JButton("Copy Summary");
        copyButton.addActionListener(e -> {
            java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(summaryArea.getText());
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        });
        summaryPanel.add(copyButton, java.awt.BorderLayout.SOUTH);

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

        tabbedPane.addTab("Original", originalPanel);
        tabbedPane.addTab("AI Summary", summaryPanel);
        tabbedPane.addTab("Sentiment", sentimentPanel);
        tabbedPane.addTab("Related Topics", relatedPanel);
        detailFrame.add(tabbedPane);
        detailFrame.setVisible(true);

        new Thread(() -> {
            try {
                String summary = app.summarizeArticle(article);
                javax.swing.SwingUtilities.invokeLater(() -> summaryArea.setText(summary));
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> summaryArea.setText("Error generating summary: " + ex.getMessage()));
            }
        }).start();

        new Thread(() -> {
            try {
                String sentiment = app.analyzeArticleSentiment(article);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    sentimentArea.setText("Sentiment Analysis: " + sentiment + "\n\nThis article has a " +
                            sentiment.toLowerCase() + " tone.");
                });
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> sentimentArea.setText("Error analyzing sentiment: " + ex.getMessage()));
            }
        }).start();

        new Thread(() -> {
            try {
                String relatedTopics = app.getRelatedTopics(article);
                javax.swing.SwingUtilities.invokeLater(() -> relatedArea.setText(relatedTopics));
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> relatedArea.setText("Error finding related topics: " + ex.getMessage()));
            }
        }).start();
    }

    private MyList<Article> mergeSort(MyList<Article> list, String sortBy) {
        if (list.size() <= 1) return list;

        int mid = list.size() / 2;
        MyList<Article> left = new MyList<>();
        MyList<Article> right = new MyList<>();
        for (int i = 0; i < mid; i++) left.add(list.get(i));
        for (int i = mid; i < list.size(); i++) right.add(list.get(i));

        left = mergeSort(left, sortBy);
        right = mergeSort(right, sortBy);
        return merge(left, right, sortBy);
    }

    private MyList<Article> merge(MyList<Article> left, MyList<Article> right, String sortBy) {
        MyList<Article> merged = new MyList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            Article a = left.get(i);
            Article b = right.get(j);
            boolean condition = sortBy.equals("date")
                    ? a.date.compareTo(b.date) >= 0
                    : a.popularity >= b.popularity;

            if (condition) {
                merged.add(a);
                i++;
            } else {
                merged.add(b);
                j++;
            }
        }

        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));

        return merged;
    }

    private void sortAndReload(String sortBy) {
        MyList<Article> articles = app.getAllArticles();
        MyList<Article> sorted = mergeSort(articles, sortBy);
        loadArticles(sorted);
    }

}
