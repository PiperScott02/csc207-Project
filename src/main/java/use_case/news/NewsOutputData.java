package use_case.news;

import entity.NewsArticle;
import entity.NewsSentiment;

import java.util.List;

/**
 * The output data returned by the news use case.
 */
public class NewsOutputData {

    private final String ticker;
    private final List<NewsArticle> articles;
    private final NewsSentiment overallSentiment;

    public NewsOutputData(
            String ticker,
            List<NewsArticle> articles,
            NewsSentiment overallSentiment) {

        this.ticker = ticker;
        this.articles = articles;
        this.overallSentiment = overallSentiment;
    }

    public String getTicker() {
        return ticker;
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }

    public NewsSentiment getOverallSentiment() {
        return overallSentiment;
    }
}