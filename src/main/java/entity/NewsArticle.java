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
    private final double relevanceScore;
    private final NewsSentiment sentiment;

    public NewsArticle(
            String title,
            String summary,
            String url,
            String source,
            double sentimentScore,
            double relevanceScore,
            NewsSentiment sentiment) {

        this.title = title;
        this.summary = summary;
        this.url = url;
        this.source = source;
        this.sentimentScore = sentimentScore;
        this.relevanceScore = relevanceScore;
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

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public NewsSentiment getSentiment() {
        return sentiment;
    }
}
