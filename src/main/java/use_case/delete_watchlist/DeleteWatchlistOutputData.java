package use_case.delete_watchlist;

import interface_adapter.watchlist.WatchlistState;

public class DeleteWatchlistOutputData {
    private final WatchlistState watchlistState;

    public DeleteWatchlistOutputData(WatchlistState watchlistState) {
        this.watchlistState = watchlistState;
    }

    public WatchlistState getWatchlistState() {
        return watchlistState;
    }
}