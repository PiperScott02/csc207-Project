package app;


import data_access.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_watchlist.AddWatchlistController;
import interface_adapter.add_watchlist.AddWatchlistPresenter;
import interface_adapter.add_watchlist.AddWatchlistViewModel;
import interface_adapter.delete_watchlist.DeleteWatchlistController;
import interface_adapter.delete_watchlist.DeleteWatchlistPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistController;
import interface_adapter.watchlist.WatchlistPresenter;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.StockDailyDataAccessInterface;
import use_case.add_watchlist.AddWatchlistInputBoundary;
import use_case.add_watchlist.AddWatchlistInteractor;
import use_case.add_watchlist.AddWatchlistOutputBoundary;
import use_case.delete_watchlist.DeleteWatchlistInputBoundary;
import use_case.delete_watchlist.DeleteWatchlistInteractor;
import use_case.delete_watchlist.DeleteWatchlistOutputBoundary;
import use_case.stock.StockDataAccessInterface;
import use_case.watchlist.WatchlistInputBoundary;
import use_case.watchlist.WatchlistInteractor;
import use_case.watchlist.WatchlistOutputBoundary;
import use_case.watchlist.WatchlistDataAccessInterface;
import view.WatchlistView;


public final class WatchlistUseCaseFactory {

    /**
     * Prevent instantiation.
     */
    private WatchlistUseCaseFactory() {
    }

    /**
     * Creates and returns the fully-wired WatchlistView.
     */
    public static WatchlistView create(
            ViewManagerModel viewManagerModel,
            WatchlistViewModel watchlistViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDataAccessInterface stockDataAccessObject,
            WatchlistDataAccessInterface watchlistDataAccessObject) {

        final WatchlistController watchlistController = createWatchlistUseCase(
                viewManagerModel,
                watchlistViewModel,
                loggedInViewModel,
                stockDataAccessObject
        );

        final AddWatchlistViewModel addWatchlistViewModel = new AddWatchlistViewModel();

        final AddWatchlistController addWatchlistController = createAddWatchlistUseCase(
                viewManagerModel,
                watchlistViewModel,
                loggedInViewModel,
                stockDataAccessObject,
                addWatchlistViewModel,
                watchlistDataAccessObject
        );

        final DeleteWatchlistController deleteWatchlistController = createDeleteWatchlistUseCase(
                viewManagerModel,
                watchlistViewModel,
                loggedInViewModel,
                watchlistDataAccessObject
        );

        return new WatchlistView(
                watchlistViewModel,
                viewManagerModel,
                loggedInViewModel,
                addWatchlistViewModel,
                addWatchlistController,
                deleteWatchlistController,
                watchlistController
        );
    }

    /**
     * Creates and wires the Controller, Interactor, and Presenter for the Watchlist Use Case.
     */
    public static WatchlistController createWatchlistUseCase(
            ViewManagerModel viewManagerModel,
            WatchlistViewModel watchlistViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDataAccessInterface stockDataAccessObject) {

        final WatchlistOutputBoundary watchlistOutputBoundary =
                new WatchlistPresenter(viewManagerModel, watchlistViewModel);

        final WatchlistInputBoundary watchlistInteractor =
                new WatchlistInteractor(stockDataAccessObject,
                        watchlistOutputBoundary,
                        loggedInViewModel
                );


        return new WatchlistController(watchlistInteractor);
    }


    /**
     * Creates and wires the Controller, Interactor, and Presenter for the Add Watchlist Use Case.
     */
    private static AddWatchlistController createAddWatchlistUseCase(
            ViewManagerModel viewManagerModel,
            WatchlistViewModel watchlistViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDataAccessInterface stockDataAccessObject,
            AddWatchlistViewModel addWatchlistViewModel,
            WatchlistDataAccessInterface watchlistDataAccessObject) {


        final AddWatchlistOutputBoundary addWatchlistOutputBoundary =
                new AddWatchlistPresenter(
                        addWatchlistViewModel,
                        watchlistViewModel,
                        loggedInViewModel,
                        viewManagerModel
                );


        final AddWatchlistInputBoundary addWatchlistInteractor =
                new AddWatchlistInteractor(
                        (StockDailyDataAccessInterface) stockDataAccessObject,
                        addWatchlistOutputBoundary,
                        loggedInViewModel,
                        (FileUserDataAccessObject) watchlistDataAccessObject
                );


        return new AddWatchlistController(addWatchlistInteractor);
    }

    /**
     * Creates and wires the Controller, Interactor, and Presenter for the Delete Watchlist Use Case.
     */
    private static DeleteWatchlistController createDeleteWatchlistUseCase(
            ViewManagerModel viewManagerModel,
            WatchlistViewModel watchlistViewModel,
            LoggedInViewModel loggedInViewModel,
            WatchlistDataAccessInterface watchlistDataAccessObject) {

        final DeleteWatchlistOutputBoundary deleteWatchlistOutputBoundary =
                new DeleteWatchlistPresenter(watchlistViewModel, viewManagerModel);

        // Cast to FileUserDataAccessObject so we can call the save(currentUser) method
        final FileUserDataAccessObject userDataAccessObject =
                (FileUserDataAccessObject) watchlistDataAccessObject;

        final DeleteWatchlistInputBoundary deleteWatchlistInteractor =
                new DeleteWatchlistInteractor(
                        userDataAccessObject,
                        deleteWatchlistOutputBoundary,
                        loggedInViewModel
                );

        return new DeleteWatchlistController(deleteWatchlistInteractor, userDataAccessObject);
    }
}