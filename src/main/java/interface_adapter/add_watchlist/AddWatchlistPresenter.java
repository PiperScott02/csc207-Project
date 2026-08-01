package interface_adapter.add_watchlist;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.add_watchlist.AddWatchlistOutputBoundary;
import use_case.add_watchlist.AddWatchlistOutputData;

/**
 * The Presenter for the Add Watchlist Item Use Case.
 */
public class AddWatchlistPresenter implements AddWatchlistOutputBoundary {

    private final AddWatchlistViewModel addWatchlistViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    public AddWatchlistPresenter(AddWatchlistViewModel addWatchlistViewModel,
                                 LoggedInViewModel loggedInViewModel,
                                 ViewManagerModel viewManagerModel) {
        this.addWatchlistViewModel = addWatchlistViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(AddWatchlistOutputData outputData) {
        // 1. Update the LoggedInState with the new watchlist list
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setWatchlist(outputData.getWatchlist());
        loggedInViewModel.setState(loggedInState);
        loggedInViewModel.firePropertyChanged();

        // 2. Tell the ViewManager to switch back to the main dashboard ("logged in") view
        viewManagerModel.setState("logged in");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Update the state with the error message and notify the view to display it
        AddWatchlistState currentState = addWatchlistViewModel.getState();
        currentState.setAddWatchlistError(errorMessage);
        addWatchlistViewModel.setState(currentState);
        addWatchlistViewModel.firePropertyChanged();
    }
}