package use_case.news;

import entity.NewsArticle;
import entity.NewsSentiment;

import java.util.List;

/**
 * The interactor for the news use case.
 */
public class NewsInteractor implements NewsInputBoundary {

    private static final double BULLISH_THRESHOLD = 0.15;
    private static final double BEARISH_THRESHOLD = -0.15;

    private final NewsDataAccessInterface newsDataAccessObject;
    private final NewsOutputBoundary newsPresenter;

    public NewsInteractor(
            NewsDataAccessInterface newsDataAccessObject,
            NewsOutputBoundary newsPresenter) {

        this.newsDataAccessObject = newsDataAccessObject;
        this.newsPresenter = newsPresenter;
    }

    @Override
    public void execute(NewsInputData newsInputData) {
        String ticker = newsInputData.getTicker();

        if (ticker == null || ticker.trim().isEmpty()) {
            newsPresenter.prepareFailView("Please enter a stock ticker.");
            return;
        }

        List<NewsArticle> articles;

        try {
            articles = newsDataAccessObject.getNews(
                    ticker.trim().toUpperCase()
            );
        }
        catch (RuntimeException exception) {
            newsPresenter.prepareFailView(
                    "Unable to retrieve news: " + exception.getMessage()
            );
            return;
        }

        if (articles == null || articles.isEmpty()) {
            newsPresenter.prepareFailView(
                    "No news articles were found for " + ticker.toUpperCase() + "."
            );
            return;
        }

        NewsSentiment overallSentiment =
                calculateOverallSentiment(articles);

        NewsOutputData outputData = new NewsOutputData(
                ticker.trim().toUpperCase(),
                articles,
                overallSentiment
        );

        newsPresenter.prepareSuccessView(outputData);
    }

    private NewsSentiment calculateOverallSentiment(
            List<NewsArticle> articles) {

        double totalScore = 0.0;

        for (NewsArticle article : articles) {
            totalScore += article.getSentimentScore();
        }

        double averageScore = totalScore / articles.size();

        if (averageScore >= BULLISH_THRESHOLD) {
            return NewsSentiment.BULLISH;
        }
        else if (averageScore <= BEARISH_THRESHOLD) {
            return NewsSentiment.BEARISH;
        }
        else {
            return NewsSentiment.NEUTRAL;
        }
    }
}