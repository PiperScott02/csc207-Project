package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.similar_search.SimilarSearchController;
import interface_adapter.similar_search.SimilarSearchPresenter;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockViewModel;
import interface_adapter.ticker_search.TickerSearchController;
import interface_adapter.ticker_search.TickerSearchPresenter;
import interface_adapter.ticker_search.TickerSearchViewModel;
import use_case.StockDailyDataAccessInterface;
import use_case.TickerSearchDataAccessInterface;
import use_case.similar_search.SimilarSearchDataAccessInterface;
import use_case.similar_search.SimilarSearchInteractor;
import use_case.similar_search.SimilarSearchOutputBoundary;
import use_case.ticker_search.TickerSearchInteractor;
import use_case.ticker_search.TickerSearchOutputBoundary;
import view.SearchView;

public class SearchUseCaseFactory {

    /** Prevent instantiation. */
    private SearchUseCaseFactory() {

    }

    public static SearchView create(
            ViewManagerModel viewManagerModel,
            SimilarSearchViewModel similarSearchViewModel,
            TickerSearchViewModel tickerSearchViewModel,
            StockViewModel stockViewModel,
            TickerSearchDataAccessInterface tickerSearchDataAccessObject,
            SimilarSearchDataAccessInterface similarSearchDataAccessObject,
            StockController stockController,
            LoggedInViewModel loggedInViewModel) {

        final SimilarSearchController similarSearchController =
                createSimilarSearchController(
                        viewManagerModel,
                        similarSearchViewModel,
                        tickerSearchDataAccessObject,
                        similarSearchDataAccessObject);
        final TickerSearchController tickerSearchController =
                createTickerSearchController(viewManagerModel, tickerSearchViewModel, tickerSearchDataAccessObject);

        return new SearchView(
                similarSearchController,
                similarSearchViewModel,
                tickerSearchController,
                tickerSearchViewModel,
                stockController,
                viewManagerModel,
                stockViewModel,
                loggedInViewModel
        );

    }

    private static SimilarSearchController
    createSimilarSearchController(ViewManagerModel viewManagerModel,
                                  SimilarSearchViewModel similarSearchViewModel,
                                  TickerSearchDataAccessInterface tickerSearchDataAccessObject,
                                  SimilarSearchDataAccessInterface similarSearchDataAccessObject) {

        final SimilarSearchOutputBoundary similarSearchOutputBoundary =
                new SimilarSearchPresenter(viewManagerModel, similarSearchViewModel);

        final SimilarSearchInteractor similarSearchInteractor =
                new SimilarSearchInteractor(
                        similarSearchDataAccessObject,
                        tickerSearchDataAccessObject,
                        similarSearchOutputBoundary);

        return new SimilarSearchController(similarSearchInteractor);
    }

    private static TickerSearchController
    createTickerSearchController(ViewManagerModel viewManagerModel,
                                 TickerSearchViewModel tickerSearchViewModel,
                                 TickerSearchDataAccessInterface tickerSearchDataAccessObject) {

        final TickerSearchOutputBoundary tickerSearchOutputBoundary =
                new TickerSearchPresenter(viewManagerModel, tickerSearchViewModel);

        final TickerSearchInteractor tickerSearchInteractor =
                new TickerSearchInteractor(tickerSearchDataAccessObject, tickerSearchOutputBoundary);

        return new TickerSearchController(tickerSearchInteractor);
    }

}