static class Article {
        int id;
        String title;
        String content;
        List<String> keywords;
        String date;
        int popularity;

        public Article(int id, String title, String content, List<String> keywords, String date, int popularity) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.keywords = keywords.stream().map(String::toLowerCase).collect(Collectors.toList());
            this.date = date;
            this.popularity = popularity;
        }
    }