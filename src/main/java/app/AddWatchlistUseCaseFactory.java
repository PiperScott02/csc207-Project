package app;

import data_access.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_watchlist.AddWatchlistController;
import interface_adapter.add_watchlist.AddWatchlistPresenter;
import interface_adapter.add_watchlist.AddWatchlistViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.StockDailyDataAccessInterface;
import use_case.add_watchlist.AddWatchlistInputBoundary;
import use_case.add_watchlist.AddWatchlistInteractor;
import use_case.add_watchlist.AddWatchlistOutputBoundary;
import use_case.watchlist.WatchlistDataAccessInterface;
import view.AddWatchlistView;

/**
 * Factory class for the Add Watchlist use case, similar to AddHoldingUseCaseFactory.
 */
public class AddWatchlistUseCaseFactory {

    private AddWatchlistUseCaseFactory() {
        // Prevent instantiation
    }

    public static AddWatchlistView create(
            ViewManagerModel viewManagerModel,
            AddWatchlistViewModel addWatchlistViewModel,
            WatchlistViewModel watchlistViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDailyDataAccessInterface stockDataAccessObject,
            WatchlistDataAccessInterface watchlistDataAccessObject) {

        AddWatchlistController addWatchlistController = createWatchlistUseCase(
                viewManagerModel,
                addWatchlistViewModel,
                watchlistViewModel,
                loggedInViewModel,
                stockDataAccessObject,
                watchlistDataAccessObject
        );

        return new AddWatchlistView(addWatchlistViewModel, addWatchlistController, viewManagerModel);
    }

    private static AddWatchlistController createWatchlistUseCase(
            ViewManagerModel viewManagerModel,
            AddWatchlistViewModel addWatchlistViewModel,
            WatchlistViewModel watchlistViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDailyDataAccessInterface stockDataAccessObject,
            WatchlistDataAccessInterface watchlistDataAccessObject) {

        AddWatchlistOutputBoundary addWatchlistOutputBoundary = new AddWatchlistPresenter(
                addWatchlistViewModel,
                watchlistViewModel,
                loggedInViewModel,
                viewManagerModel
        );

        AddWatchlistInputBoundary addWatchlistInteractor = new AddWatchlistInteractor(
                stockDataAccessObject,
                addWatchlistOutputBoundary,
                loggedInViewModel,
                (FileUserDataAccessObject) watchlistDataAccessObject
        );

        return new AddWatchlistController(addWatchlistInteractor);
    }
}