package interface_adapter.watchlist;

import entity.User;
import use_case.watchlist.WatchlistInputBoundary;
import use_case.watchlist.WatchlistInputData;

/** The controller for the Watchlist use case. */
public class WatchlistController {
    private final WatchlistInputBoundary watchlistUseCaseInteractor;

    public WatchlistController(WatchlistInputBoundary watchlistInputBoundary) {
        this.watchlistUseCaseInteractor = watchlistInputBoundary;
    }

    /**
     * Executes the Watchlist Use Case.
     *
     * @param user the user whose watchlist is to be displayed.
     */
    public void execute(User user) {
        final WatchlistInputData watchlistInputData = new WatchlistInputData(user);

        watchlistUseCaseInteractor.execute(watchlistInputData);
    }
}