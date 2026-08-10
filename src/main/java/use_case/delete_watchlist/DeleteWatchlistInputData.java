package use_case.delete_watchlist;

import entity.User;

public class DeleteWatchlistInputData {
    private final String ticker;
    private final User user;

    public DeleteWatchlistInputData(String ticker, User user) {
        this.ticker = ticker;
        this.user = user;
    }

    public String getTicker() {
        return ticker;
    }

    public User getUser() {
        return user;
    }
}