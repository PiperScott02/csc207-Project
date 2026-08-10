package app;

import data_access.AlphaVantageNewsDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.news.NewsController;
import interface_adapter.news.NewsPresenter;
import interface_adapter.news.NewsViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import use_case.news.NewsDataAccessInterface;
import use_case.news.NewsInputBoundary;
import use_case.news.NewsInteractor;
import use_case.news.NewsOutputBoundary;
import view.NewsView;

/**
 * Creates and connects all classes needed for the news use case.
 */
public final class NewsUseCaseFactory {

    private NewsUseCaseFactory() {
        // Prevent this utility class from being instantiated.
    }

    /**
     * Creates the stock news view and its dependencies.
     *
     * @param newsViewModel the view model for the news page
     * @param viewManagerModel controls which application screen is visible
     * @param loggedInViewModel the view model for the logged-in user
     * @param blackLittermanController controller for Black-Litterman workflow
     * @param portfolioHealthController controller for portfolio health calculations
     * @param apiKey the Alpha Vantage API key
     * @return the completed NewsView
     */
    public static NewsView create(
            NewsViewModel newsViewModel,
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            BlackLittermanController blackLittermanController,
            PortfolioHealthController portfolioHealthController,
            String apiKey) {

        final NewsDataAccessInterface newsDataAccessObject =
                new AlphaVantageNewsDataAccessObject(apiKey);

        final NewsOutputBoundary newsPresenter =
                new NewsPresenter(newsViewModel);

        final NewsInputBoundary newsInteractor =
                new NewsInteractor(
                        newsDataAccessObject,
                        newsPresenter
                );

        final NewsController newsController =
                new NewsController(newsInteractor);

        return new NewsView(
                newsViewModel,
                newsController,
                viewManagerModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController
        );
    }
}