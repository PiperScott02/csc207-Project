package use_case.delete_watchlist;

import entity.User;
import interface_adapter.logged_in.LoggedInViewModel;
import data_access.FileUserDataAccessObject;
import interface_adapter.watchlist.WatchlistState;

public class DeleteWatchlistInteractor implements DeleteWatchlistInputBoundary {
    private final FileUserDataAccessObject userDataAccessObject;
    private final DeleteWatchlistOutputBoundary watchlistPresenter;
    private final LoggedInViewModel loggedInViewModel;

    public DeleteWatchlistInteractor(FileUserDataAccessObject userDataAccessObject,
                                     DeleteWatchlistOutputBoundary watchlistPresenter,
                                     LoggedInViewModel loggedInViewModel) {
        this.userDataAccessObject = userDataAccessObject;
        this.watchlistPresenter = watchlistPresenter;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void execute(DeleteWatchlistInputData deleteWatchlistInputData) {
        // 1. Get the current user directly from LoggedInViewModel (just like AddHoldingInteractor does)
        User currentUser = loggedInViewModel.getState().getUser();
        if (currentUser == null || currentUser.getPortfolio() == null) {
            watchlistPresenter.prepareFailView("No active user session found.");
            return;
        }

        String ticker = deleteWatchlistInputData.getTicker();

        try {
            // 2. Remove the stock from the user's portfolio watchlist
            boolean removed = currentUser.getPortfolio().removeWatchlistByTicker(ticker);

            if (removed) {
                // 3. Persist the changes using the exact same save method as holdings!
                userDataAccessObject.save(currentUser);
            }

            // 4. Prepare success view with the updated watchlist
            WatchlistState updatedState = new WatchlistState();
            updatedState.setItems(userDataAccessObject.getWatchlistItems()); // or grab from portfolio

            watchlistPresenter.prepareSuccessView(new DeleteWatchlistOutputData(updatedState));
        } catch (Exception e) {
            watchlistPresenter.prepareFailView("Failed to remove stock: " + e.getMessage());
        }
    }
}