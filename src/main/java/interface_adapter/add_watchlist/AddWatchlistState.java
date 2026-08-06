package interface_adapter.add_watchlist;

/**
 * The State for the Add Watchlist Item View Model, holding the ticker and error message if any occurs.
 */
public class AddWatchlistState {
    private String ticker = "";
    private String addWatchlistError;

    public AddWatchlistState(AddWatchlistState copy) {
        this.ticker = copy.ticker;
        this.addWatchlistError = copy.addWatchlistError;
    }

    public AddWatchlistState() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getAddWatchlistError() {
        return addWatchlistError;
    }

    public void setAddWatchlistError(String addWatchlistError) {
        this.addWatchlistError = addWatchlistError;
    }
}