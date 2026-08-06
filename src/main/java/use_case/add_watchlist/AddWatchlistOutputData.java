package use_case.add_watchlist;

import entity.WatchlistStockItem;
import java.util.List;

public class AddWatchlistOutputData {
    private final String ticker;
    private final List<WatchlistStockItem> watchlist;
    private final boolean useCaseFailed;

    public AddWatchlistOutputData(String ticker, List<WatchlistStockItem> watchlist, boolean useCaseFailed) {
        this.ticker = ticker;
        this.watchlist = watchlist;
        this.useCaseFailed = useCaseFailed;
    }

    public String getTicker() {
        return ticker;
    }

    public List<WatchlistStockItem> getWatchlist() {
        return watchlist;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}