package interface_adapter.watchlist;

import interface_adapter.ViewManagerModel;
import use_case.watchlist.WatchlistOutputBoundary;
import use_case.watchlist.WatchlistOutputData;

import java.util.ArrayList;
import java.util.List;

/** The Presenter for the Watchlist use case, handling state updates and view switching on success or failure. */
public class WatchlistPresenter implements WatchlistOutputBoundary {

    private final WatchlistViewModel watchlistViewModel;
    private final ViewManagerModel viewManagerModel;

    public WatchlistPresenter(ViewManagerModel viewManagerModel, WatchlistViewModel watchlistViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.watchlistViewModel = watchlistViewModel;
    }

    @Override
    public void prepareSuccessView(WatchlistOutputData outputData) {
        WatchlistState currentState = watchlistViewModel.getState();

        List<WatchlistState.WatchlistStockItem> stateItems = new ArrayList<>();
        for (WatchlistOutputData.WatchlistStockOutputItem outputItem : outputData.getItems()) {
            WatchlistState.WatchlistStockItem stateItem = new WatchlistState.WatchlistStockItem(
                    outputItem.getTicker(),
                    outputItem.getCompanyName(),
                    outputItem.getClose(),
                    outputItem.getDailyPriceChange()
            );
            stateItems.add(stateItem);
        }

        currentState.setItems(stateItems);
        currentState.setErrorMessage("");

        watchlistViewModel.setState(currentState);
        watchlistViewModel.firePropertyChanged();

        viewManagerModel.setState(watchlistViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        WatchlistState currentState = watchlistViewModel.getState();
        currentState.setErrorMessage(errorMessage);

        watchlistViewModel.setState(currentState);
        watchlistViewModel.firePropertyChanged();
    }
}