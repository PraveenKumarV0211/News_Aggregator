import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ArticleFileLoader {

    public static List<Article> loadArticlesFromDirectory(String dirPath) {
        List<Article> articles = new ArrayList<>();
        File folder = new File(dirPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) {
            System.err.println("No article files found in directory: " + dirPath);
            return articles;
        }

        int id = 1;
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String title = "", date = "", content = "";
                int popularity = 0;
                List<String> keywords = new ArrayList<>();

                String line;
                boolean readingContent = false;
                StringBuilder contentBuilder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Title:")) {
                        title = line.substring(6).trim();
                    } else if (line.startsWith("Date:")) {
                        date = line.substring(5).trim();
                    } else if (line.startsWith("Keywords:")) {
                        String[] kw = line.substring(9).trim().split(",");
                        for (String k : kw) {
                            keywords.add(k.trim());
                        }
                    } else if (line.startsWith("Popularity:")) {
                        popularity = Integer.parseInt(line.substring(11).trim());
                    } else if (line.equals("---")) {
                        readingContent = true;
                    } else if (readingContent) {
                        contentBuilder.append(line).append("\n");
                    }
                }

                content = contentBuilder.toString().trim();
                MyList<String> myKeywordList = new MyList<>();
                for (String keyword : keywords) {
                    myKeywordList.add(keyword.trim().toLowerCase());
                }
                Article article = new Article(id++, title, content, myKeywordList, date, popularity);
                articles.add(article);
            } catch (Exception e) {
                System.err.println("Failed to read file: " + file.getName() + " - " + e.getMessage());
            }
        }

        return articles;
    }
}
