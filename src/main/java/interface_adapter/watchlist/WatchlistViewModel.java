package interface_adapter.watchlist;

import interface_adapter.ViewModel;

/** ViewModel for managing and observing the state of the watchlist in the user interface. */
public class WatchlistViewModel extends ViewModel<WatchlistState> {

    public static final String TITLE_LABEL = "Your Watchlist";

    /** Constructs a new WatchlistViewmodel with the view name "watchlist" and initializes its default state. */
    public WatchlistViewModel() {
        super("watchlist");
        setState(new WatchlistState());
    }

    /** Returns the View Model's view name.**/
    public String getViewName() {
        return "watchlist";
    }
}