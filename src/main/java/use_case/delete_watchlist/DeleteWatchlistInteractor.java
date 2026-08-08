package use_case.delete_watchlist;

import interface_adapter.watchlist.WatchlistState;
import use_case.watchlist.WatchlistDataAccessInterface;

public class DeleteWatchlistInteractor implements DeleteWatchlistInputBoundary {
    private final WatchlistDataAccessInterface watchlistDataAccessObject;
    private final DeleteWatchlistOutputBoundary watchlistPresenter;

    public DeleteWatchlistInteractor(WatchlistDataAccessInterface watchlistDataAccessObject,
                                     DeleteWatchlistOutputBoundary watchlistPresenter) {
        this.watchlistDataAccessObject = watchlistDataAccessObject;
        this.watchlistPresenter = watchlistPresenter;
    }

    @Override
    public void execute(DeleteWatchlistInputData deleteWatchlistInputData) {
        String ticker = deleteWatchlistInputData.getTicker();
        try {
            watchlistDataAccessObject.removeWatchlistStock(ticker);

            WatchlistState updatedState = new WatchlistState();
            updatedState.setItems(watchlistDataAccessObject.getWatchlistItems());

            watchlistPresenter.prepareSuccessView(new DeleteWatchlistOutputData(updatedState));
        } catch (Exception e) {
            watchlistPresenter.prepareFailView("Failed to remove stock: " + e.getMessage());
        }
    }
}