package use_case.news;

import entity.NewsArticle;
import entity.NewsSentiment;
import entity.NewsSentimentCalculator;

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
        final String ticker = newsInputData.getTicker();

        if (ticker == null || ticker.trim().isEmpty()) {
            newsPresenter.prepareFailView("Please enter a stock ticker.");
            return;
        }

        final String cleanedTicker = ticker.trim().toUpperCase();
        final List<NewsArticle> articles;

        try {
            articles = newsDataAccessObject.getNews(cleanedTicker);
        }
        catch (RuntimeException exception) {
            newsPresenter.prepareFailView(
                    "Unable to retrieve news: " + exception.getMessage()
            );
            return;
        }

        if (articles == null || articles.isEmpty()) {
            newsPresenter.prepareFailView(
                    "No news articles were found for "
                            + cleanedTicker + "."
            );
            return;
        }

        final NewsSentiment overallSentiment =
                calculateOverallSentiment(articles);

        final NewsOutputData outputData = new NewsOutputData(
                cleanedTicker,
                articles,
                overallSentiment
        );

        newsPresenter.prepareSuccessView(outputData);
    }

    /**
     * Calculates overall sentiment using relevance as each article's weight.
     *
     * @param articles the articles returned by the news data access object
     * @return the overall sentiment category
     */
    private NewsSentiment calculateOverallSentiment(
            List<NewsArticle> articles) {

        double averageScore = NewsSentimentCalculator.calculateRawSentiment(articles);

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

    /**
     * Calculates a normal average when no relevance scores are available.
     *
     * @param articles the news articles
     * @return the simple average sentiment score
     */
    private double calculateSimpleAverage(List<NewsArticle> articles) {
        double totalScore = 0.0;

        for (NewsArticle article : articles) {
            totalScore += article.getSentimentScore();
        }

        return totalScore / articles.size();
    }
}
