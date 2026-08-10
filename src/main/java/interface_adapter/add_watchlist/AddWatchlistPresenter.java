package interface_adapter.add_watchlist;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistState;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.add_watchlist.AddWatchlistOutputBoundary;
import use_case.add_watchlist.AddWatchlistOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The Presenter for the Add Watchlist Item Use Case.
 */
public class AddWatchlistPresenter implements AddWatchlistOutputBoundary {

    private final AddWatchlistViewModel addWatchlistViewModel;
    private final WatchlistViewModel watchlistViewModel; // Add this
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    public AddWatchlistPresenter(AddWatchlistViewModel addWatchlistViewModel,
                                 WatchlistViewModel watchlistViewModel, // Include in constructor
                                 LoggedInViewModel loggedInViewModel,
                                 ViewManagerModel viewManagerModel) {
        this.addWatchlistViewModel = addWatchlistViewModel;
        this.watchlistViewModel = watchlistViewModel;
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

        // 2. Map entity WatchlistStockItems to WatchlistState items and update WatchlistViewModel
        List<WatchlistState.WatchlistStockItem> stateItems = new ArrayList<>();
        for (entity.WatchlistStockItem item : outputData.getWatchlist()) {

            // Handle potentially null prices/changes safely
            String closeStr = (item.closePrice() != null) ? item.closePrice().toString() : "—";
            String changeStr = (item.dailyPriceChange() != null) ? item.dailyPriceChange().toString() : "—";

            stateItems.add(new WatchlistState.WatchlistStockItem(
                    item.ticker(),
                    item.companyName() != null ? item.companyName() : "",
                    closeStr,
                    changeStr
            ));
        }
        WatchlistState watchlistState = new WatchlistState();
        watchlistState.setItems(stateItems);
        watchlistViewModel.setState(watchlistState);
        watchlistViewModel.firePropertyChanged();

        // 3. Tell the ViewManager to switch back to the watchlist view (or dashboard)
        viewManagerModel.setState("watchlist");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        AddWatchlistState currentState = addWatchlistViewModel.getState();
        if (currentState == null) {
            currentState = new AddWatchlistState();
            addWatchlistViewModel.setState(currentState);
        }
        currentState.setAddWatchlistError(errorMessage);
        addWatchlistViewModel.setState(currentState);
        addWatchlistViewModel.firePropertyChanged();
    }
}