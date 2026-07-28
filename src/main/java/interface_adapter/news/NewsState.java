package interface_adapter.news;

import entity.NewsArticle;
import entity.NewsSentiment;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the information displayed by the news view.
 */
public class NewsState {

    private String ticker = "";
    private List<NewsArticle> articles = new ArrayList<>();
    private NewsSentiment overallSentiment = NewsSentiment.NEUTRAL;
    private String errorMessage = "";

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticle> articles) {
        this.articles = articles;
    }

    public NewsSentiment getOverallSentiment() {
        return overallSentiment;
    }

    public void setOverallSentiment(NewsSentiment overallSentiment) {
        this.overallSentiment = overallSentiment;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}