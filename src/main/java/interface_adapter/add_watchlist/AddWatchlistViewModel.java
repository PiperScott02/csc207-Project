package interface_adapter.add_watchlist;

import interface_adapter.ViewModel;

public class AddWatchlistViewModel extends ViewModel {

    public static final String TITLE_LABEL = "Add New Watchlist Item";
    public static final String TICKER_LABEL = "Ticker Symbol:";

    public static final String SAVE_BUTTON_LABEL = "Add Watchlist Item";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    private AddWatchlistState state = new AddWatchlistState();

    public AddWatchlistViewModel() {
        super("add watchlist");
    }

    public void setState(AddWatchlistState state) {
        this.state = state;
    }

    public AddWatchlistState getState() {
        return state;
    }
}