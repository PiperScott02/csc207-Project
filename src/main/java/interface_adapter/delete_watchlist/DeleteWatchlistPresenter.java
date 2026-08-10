package interface_adapter.delete_watchlist;

import interface_adapter.ViewManagerModel;
import interface_adapter.watchlist.WatchlistState;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.delete_watchlist.DeleteWatchlistOutputBoundary;
import use_case.delete_watchlist.DeleteWatchlistOutputData;

public class DeleteWatchlistPresenter implements DeleteWatchlistOutputBoundary {
    private final WatchlistViewModel watchlistViewModel;
    private final ViewManagerModel viewManagerModel;

    public DeleteWatchlistPresenter(WatchlistViewModel watchlistViewModel, ViewManagerModel viewManagerModel) {
        this.watchlistViewModel = watchlistViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(DeleteWatchlistOutputData outputData) {
        WatchlistState currentState = watchlistViewModel.getState();
        currentState.setItems(outputData.getWatchlistState().getItems());
        watchlistViewModel.setState(currentState);
        watchlistViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        // Handle error display if needed
    }
}