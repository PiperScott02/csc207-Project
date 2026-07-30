package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.similar_search.SimilarSearchController;
import interface_adapter.similar_search.SimilarSearchPresenter;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockViewModel;
import interface_adapter.ticker_search.TickerSearchController;
import interface_adapter.ticker_search.TickerSearchPresenter;
import interface_adapter.ticker_search.TickerSearchViewModel;
import use_case.StockDailyDataAccessInterface;
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
            StockDailyDataAccessInterface stockDailyDataAccessObject,
            SimilarSearchDataAccessInterface similarSearchDataAccessObject,
            StockController stockController) {

        final SimilarSearchController similarSearchController =
                createSimilarSearchController(viewManagerModel, similarSearchViewModel,
                        stockDailyDataAccessObject, similarSearchDataAccessObject);
        final TickerSearchController tickerSearchController =
                createTickerSearchController(viewManagerModel, tickerSearchViewModel, stockDailyDataAccessObject);

        return new SearchView(
                similarSearchController,
                similarSearchViewModel,
                tickerSearchController,
                tickerSearchViewModel,
                stockController,
                viewManagerModel,
                stockViewModel
        );

    }

    private static SimilarSearchController
    createSimilarSearchController(ViewManagerModel viewManagerModel,
                                  SimilarSearchViewModel similarSearchViewModel,
                                  StockDailyDataAccessInterface stockDailyDataAccessObject,
                                  SimilarSearchDataAccessInterface similarSearchDataAccessObject) {

        final SimilarSearchOutputBoundary similarSearchOutputBoundary =
                new SimilarSearchPresenter(viewManagerModel, similarSearchViewModel);

        final SimilarSearchInteractor similarSearchInteractor =
                new SimilarSearchInteractor(similarSearchDataAccessObject,
                        stockDailyDataAccessObject, similarSearchOutputBoundary);

        return new SimilarSearchController(similarSearchInteractor);
    }

    private static TickerSearchController
    createTickerSearchController(ViewManagerModel viewManagerModel,
                                 TickerSearchViewModel tickerSearchViewModel,
                                 StockDailyDataAccessInterface stockDailyDataAccessObject) {

        final TickerSearchOutputBoundary tickerSearchOutputBoundary =
                new TickerSearchPresenter(viewManagerModel, tickerSearchViewModel);

        final TickerSearchInteractor tickerSearchInteractor =
                new TickerSearchInteractor(stockDailyDataAccessObject, tickerSearchOutputBoundary);

        return new TickerSearchController(tickerSearchInteractor);
    }

}