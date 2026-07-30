package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistController;
import interface_adapter.watchlist.WatchlistPresenter;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.stock.StockDataAccessInterface;
import use_case.watchlist.WatchlistInputBoundary;
import use_case.watchlist.WatchlistInteractor;
import use_case.watchlist.WatchlistOutputBoundary;
import view.WatchlistView;

public final class WatchlistUseCaseFactory {

    /** Prevent instantiation. */
    private WatchlistUseCaseFactory() {
    }

    /**
     * Creates and returns the fully-wired WatchlistView.
     */
    public static WatchlistView create(
            ViewManagerModel viewManagerModel,
            WatchlistViewModel watchlistViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDataAccessInterface stockDataAccessObject) {

        final WatchlistController watchlistController = createWatchlistUseCase(
                viewManagerModel,
                watchlistViewModel,
                stockDataAccessObject
        );

        return new WatchlistView(
                watchlistViewModel,
                viewManagerModel,
                loggedInViewModel
        );
    }

    /**
     * Creates and wires the Controller, Interactor, and Presenter for the Watchlist Use Case.
     */
    public static WatchlistController createWatchlistUseCase(
            ViewManagerModel viewManagerModel,
            WatchlistViewModel watchlistViewModel,
            StockDataAccessInterface stockDataAccessObject) {

        final WatchlistOutputBoundary watchlistOutputBoundary =
                new WatchlistPresenter(viewManagerModel, watchlistViewModel);

        final WatchlistInputBoundary watchlistInteractor =
                new WatchlistInteractor(stockDataAccessObject,
                        watchlistOutputBoundary
                );

        return new WatchlistController(watchlistInteractor);
    }
}