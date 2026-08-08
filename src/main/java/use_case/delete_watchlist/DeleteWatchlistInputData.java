package use_case.delete_watchlist;

public class DeleteWatchlistInputData {
    private final String ticker;

    public DeleteWatchlistInputData(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }
}