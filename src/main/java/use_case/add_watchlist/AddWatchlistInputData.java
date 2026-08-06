package use_case.add_watchlist;

public class AddWatchlistInputData {
    private final String ticker;

    public AddWatchlistInputData(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }
}