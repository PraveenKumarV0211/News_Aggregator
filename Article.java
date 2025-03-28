class Article {
    int id;
    String title;
    String content;
    MyList<String> keywords;
    String date;
    int popularity;

    public Article(int id, String title, String content, MyList<String> keywords, String date, int popularity) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.keywords = new MyList<>(new String[]{"java", "programming", "tech"});
        for (int i = 0; i < keywords.size(); i++) {
            this.keywords.add(keywords.get(i).toLowerCase());
        }
        this.date = date;
        this.popularity = popularity;
    }
}
