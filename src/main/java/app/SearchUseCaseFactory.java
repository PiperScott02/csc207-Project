package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.similar_search.SimilarSearchController;
import interface_adapter.similar_search.SimilarSearchPresenter;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockViewModel;
import interface_adapter.ticker_search.TickerSearchController;
import interface_adapter.ticker_search.TickerSearchPresenter;
import interface_adapter.ticker_search.TickerSearchViewModel;
import use_case.TickerSearchDataAccessInterface;
import use_case.similar_search.SimilarSearchDataAccessInterface;
import use_case.similar_search.SimilarSearchInputBoundary;
import use_case.similar_search.SimilarSearchInteractor;
import use_case.similar_search.SimilarSearchOutputBoundary;
import use_case.ticker_search.TickerSearchInputBoundary;
import use_case.ticker_search.TickerSearchInteractor;
import use_case.ticker_search.TickerSearchOutputBoundary;
import view.SearchView;

/**
 * Factory for creating the SearchView and its dependencies.
 */
public final class SearchUseCaseFactory {

    private SearchUseCaseFactory() {
        // Prevent instantiation
    }

    /**
     * Creates the SearchView and its use case components.
     *
     * @param viewManagerModel the view manager model
     * @param similarSearchViewModel the similar search view model
     * @param tickerSearchViewModel the ticker search view model
     * @param stockViewModel the stock view model
     * @param tickerSearchDataAccessObject the ticker search DAO
     * @param similarSearchDataAccessObject the similar search DAO
     * @param stockController the stock controller
     * @param loggedInViewModel the logged in view model
     * @param blackLittermanController the black litterman controller
     * @param portfolioHealthController the portfolio health controller
     * @return the SearchView instance
     */
    public static SearchView create(
            ViewManagerModel viewManagerModel,
            SimilarSearchViewModel similarSearchViewModel,
            TickerSearchViewModel tickerSearchViewModel,
            StockViewModel stockViewModel,
            TickerSearchDataAccessInterface tickerSearchDataAccessObject,
            SimilarSearchDataAccessInterface similarSearchDataAccessObject,
            StockController stockController,
            LoggedInViewModel loggedInViewModel,
            BlackLittermanController blackLittermanController,
            PortfolioHealthController portfolioHealthController) {

        // 1. Build Ticker Search use case components
        final TickerSearchOutputBoundary tickerSearchOutputBoundary =
                new TickerSearchPresenter(viewManagerModel, tickerSearchViewModel);
        final TickerSearchInputBoundary tickerSearchInteractor =
                new TickerSearchInteractor(tickerSearchDataAccessObject, tickerSearchOutputBoundary);
        final TickerSearchController tickerSearchController =
                new TickerSearchController(tickerSearchInteractor);

        // 2. Build Similar Search use case components
        final SimilarSearchOutputBoundary similarSearchOutputBoundary =
                new SimilarSearchPresenter(viewManagerModel, similarSearchViewModel);
        final SimilarSearchInputBoundary similarSearchInteractor =
                new SimilarSearchInteractor(
                        similarSearchDataAccessObject,
                        tickerSearchDataAccessObject,
                        similarSearchOutputBoundary
                );
        final SimilarSearchController similarSearchController =
                new SimilarSearchController(similarSearchInteractor);

        // 3. Return the fully initialized SearchView
        return new SearchView(
                similarSearchController,
                similarSearchViewModel,
                tickerSearchController,
                tickerSearchViewModel,
                stockController,
                viewManagerModel,
                stockViewModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController
        );
    }
}