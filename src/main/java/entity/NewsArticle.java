package entity;

/**
 * Represents one financial news article.
 */
public class NewsArticle {

    private final String title;
    private final String summary;
    private final String url;
    private final String source;
    private final double sentimentScore;
    private final NewsSentiment sentiment;

    public NewsArticle(
            String title,
            String summary,
            String url,
            String source,
            double sentimentScore,
            NewsSentiment sentiment) {

        this.title = title;
        this.summary = summary;
        this.url = url;
        this.source = source;
        this.sentimentScore = sentimentScore;
        this.sentiment = sentiment;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public NewsSentiment getSentiment() {
        return sentiment;
    }
}