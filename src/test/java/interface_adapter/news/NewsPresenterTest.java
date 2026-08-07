package interface_adapter.news;

import entity.NewsArticle;
import entity.NewsSentiment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.news.NewsOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewsPresenterTest {

    private NewsViewModel viewModel;
    private NewsPresenter presenter;

    @BeforeEach
    void setUp() {
        viewModel = new NewsViewModel();
        presenter = new NewsPresenter(viewModel);
    }

    @Test
    void testPrepareSuccessViewUpdatesState() {
        final List<NewsArticle> articles = List.of(createArticle());
        final NewsOutputData outputData = new NewsOutputData(
                "AAPL",
                articles,
                NewsSentiment.BULLISH
        );
        final boolean[] eventFired = {false};
        viewModel.addPropertyChangeListener(
                event -> eventFired[0] = true
        );

        presenter.prepareSuccessView(outputData);

        final NewsState state = viewModel.getState();
        assertEquals("AAPL", state.getTicker());
        assertEquals(articles, state.getArticles());
        assertEquals(
                NewsSentiment.BULLISH,
                state.getOverallSentiment()
        );
        assertEquals("", state.getErrorMessage());
        assertTrue(eventFired[0]);
    }

    @Test
    void testPrepareFailViewStoresErrorAndClearsArticles() {
        final NewsState initialState = viewModel.getState();
        initialState.setArticles(List.of(createArticle()));
        final boolean[] eventFired = {false};
        viewModel.addPropertyChangeListener(
                event -> eventFired[0] = true
        );

        presenter.prepareFailView("No news was found.");

        final NewsState state = viewModel.getState();
        assertEquals("No news was found.", state.getErrorMessage());
        assertTrue(state.getArticles().isEmpty());
        assertTrue(eventFired[0]);
    }

    private NewsArticle createArticle() {
        return new NewsArticle(
                "Test article",
                "Test summary",
                "https://example.com",
                "Test source",
                0.25,
                0.80,
                NewsSentiment.BULLISH
        );
    }
}
