package use_case.news;

import entity.NewsArticle;
import entity.NewsSentiment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewsInteractorTest {

    private InMemoryNewsDataAccessObject newsDataAccessObject;
    private TestNewsPresenter presenter;
    private NewsInteractor interactor;

    @BeforeEach
    void setUp() {
        newsDataAccessObject = new InMemoryNewsDataAccessObject();
        presenter = new TestNewsPresenter();
        interactor = new NewsInteractor(newsDataAccessObject, presenter);
    }

    @Test
    void testBlankTickerFails() {
        interactor.execute(new NewsInputData("   "));

        assertTrue(presenter.isFailViewCalled);
        assertFalse(presenter.isSuccessViewCalled);
        assertEquals(
                "Please enter a stock ticker.",
                presenter.errorMessage
        );
    }

    @Test
    void testTickerIsTrimmedAndConvertedToUppercase() {
        newsDataAccessObject.articles = List.of(
                createArticle("Article", 0.20, 0.80)
        );

        interactor.execute(new NewsInputData("  aapl  "));

        assertEquals("AAPL", newsDataAccessObject.requestedTicker);
        assertTrue(presenter.isSuccessViewCalled);
        assertEquals("AAPL", presenter.outputData.getTicker());
    }

    @Test
    void testEmptyResultFails() {
        newsDataAccessObject.articles = Collections.emptyList();

        interactor.execute(new NewsInputData("TSLA"));

        assertTrue(presenter.isFailViewCalled);
        assertEquals(
                "No news articles were found for TSLA.",
                presenter.errorMessage
        );
    }

    @Test
    void testNullResultFails() {
        newsDataAccessObject.articles = null;

        interactor.execute(new NewsInputData("TSLA"));

        assertTrue(presenter.isFailViewCalled);
        assertEquals(
                "No news articles were found for TSLA.",
                presenter.errorMessage
        );
    }

    @Test
    void testDataAccessExceptionFails() {
        newsDataAccessObject.exception =
                new RuntimeException("API limit reached");

        interactor.execute(new NewsInputData("NVDA"));

        assertTrue(presenter.isFailViewCalled);
        assertEquals(
                "Unable to retrieve news: API limit reached",
                presenter.errorMessage
        );
    }

    @Test
    void testOverallSentimentUsesRelevanceWeightedAverage() {
        newsDataAccessObject.articles = List.of(
                createArticle("Highly relevant", 0.60, 0.90),
                createArticle("Barely relevant", -0.60, 0.10)
        );

        interactor.execute(new NewsInputData("GOOG"));

        assertTrue(presenter.isSuccessViewCalled);
        assertEquals(
                NewsSentiment.BULLISH,
                presenter.outputData.getOverallSentiment()
        );
    }

    @Test
    void testSimpleAverageUsedWhenTotalRelevanceIsZero() {
        newsDataAccessObject.articles = List.of(
                createArticle("Positive", 0.60, 0.0),
                createArticle("Negative", -0.20, 0.0)
        );

        interactor.execute(new NewsInputData("AMZN"));

        assertTrue(presenter.isSuccessViewCalled);
        assertEquals(
                NewsSentiment.BULLISH,
                presenter.outputData.getOverallSentiment()
        );
    }

    @Test
    void testOverallSentimentCanBeBearish() {
        newsDataAccessObject.articles = List.of(
                createArticle("Negative", -0.40, 0.90),
                createArticle("Positive", 0.10, 0.10)
        );

        interactor.execute(new NewsInputData("META"));

        assertEquals(
                NewsSentiment.BEARISH,
                presenter.outputData.getOverallSentiment()
        );
    }

    @Test
    void testOverallSentimentCanBeNeutral() {
        newsDataAccessObject.articles = List.of(
                createArticle("Slightly positive", 0.10, 0.50),
                createArticle("Slightly negative", -0.10, 0.50)
        );

        interactor.execute(new NewsInputData("MSFT"));

        assertEquals(
                NewsSentiment.NEUTRAL,
                presenter.outputData.getOverallSentiment()
        );
    }

    @Test
    void testOnlyTenMostRelevantArticlesAreReturned() {
        final List<NewsArticle> articles = new ArrayList<>();

        for (int index = 0; index < 12; index++) {
            articles.add(createArticle(
                    "Article " + index,
                    0.0,
                    index / 12.0
            ));
        }
        newsDataAccessObject.articles = articles;

        interactor.execute(new NewsInputData("AAPL"));

        assertNotNull(presenter.outputData);
        assertEquals(10, presenter.outputData.getArticles().size());
        assertEquals(
                "Article 11",
                presenter.outputData.getArticles().get(0).getTitle()
        );
        assertEquals(
                "Article 2",
                presenter.outputData.getArticles().get(9).getTitle()
        );

        for (int index = 1;
             index < presenter.outputData.getArticles().size();
             index++) {

            final double previousRelevance = presenter.outputData
                    .getArticles().get(index - 1).getRelevanceScore();
            final double currentRelevance = presenter.outputData
                    .getArticles().get(index).getRelevanceScore();

            assertTrue(previousRelevance >= currentRelevance);
        }
    }

    private NewsArticle createArticle(
            String title,
            double sentimentScore,
            double relevanceScore) {

        return new NewsArticle(
                title,
                "Test summary",
                "https://example.com",
                "Test source",
                sentimentScore,
                relevanceScore,
                NewsSentiment.NEUTRAL
        );
    }

    private static final class InMemoryNewsDataAccessObject
            implements NewsDataAccessInterface {

        private List<NewsArticle> articles = new ArrayList<>();
        private RuntimeException exception;
        private String requestedTicker;

        @Override
        public List<NewsArticle> getNews(String ticker) {
            requestedTicker = ticker;

            if (exception != null) {
                throw exception;
            }
            return articles;
        }
    }

    private static final class TestNewsPresenter
            implements NewsOutputBoundary {

        private boolean isSuccessViewCalled;
        private boolean isFailViewCalled;
        private NewsOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(NewsOutputData newsOutputData) {
            isSuccessViewCalled = true;
            outputData = newsOutputData;
        }

        @Override
        public void prepareFailView(String error) {
            isFailViewCalled = true;
            errorMessage = error;
        }
    }
}
