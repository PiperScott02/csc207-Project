package app;

import data_access.AlphaVantageNewsDataAccessObject;
import interface_adapter.news.NewsController;
import interface_adapter.news.NewsPresenter;
import interface_adapter.news.NewsViewModel;
import use_case.news.NewsDataAccessInterface;
import use_case.news.NewsInputBoundary;
import use_case.news.NewsInteractor;
import use_case.news.NewsOutputBoundary;
import view.NewsView;
import interface_adapter.ViewManagerModel;

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
     * @param apiKey the Alpha Vantage API key
     * @return the completed NewsView
     */
    public static NewsView create(
            NewsViewModel newsViewModel,
            ViewManagerModel viewManagerModel,
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
                viewManagerModel
        );
    }
}