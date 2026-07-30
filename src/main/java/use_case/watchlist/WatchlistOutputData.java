package use_case.watchlist;

import java.util.List;

/** The output data for the Watchlist use case, containing the formatted stock items and failure status. */
public class WatchlistOutputData {

    /** Nested record or class representing an individual stock's output data for the watchlist. */
    public static class WatchlistStockOutputItem {
        private final String ticker;
        private final String companyName;
        private final String close;
        private final String dailyPriceChange;

        public WatchlistStockOutputItem(String ticker, String companyName, String close, String dailyPriceChange) {
            this.ticker = ticker;
            this.companyName = companyName;
            this.close = close;
            this.dailyPriceChange = dailyPriceChange;
        }

        public String getTicker() { return ticker; }
        public String getCompanyName() { return companyName; }
        public String getClose() { return close; }
        public String getDailyPriceChange() { return dailyPriceChange; }
    }

    private final List<WatchlistStockOutputItem> items;
    private final boolean useCaseFailed;

    /** Constructs a new WatchlistOutputData object with watchlist items and failure status.
     * @param items the list of stock output items.
     * @param useCaseFailed boolean indicating whether the operation failed.
     */
    public WatchlistOutputData(List<WatchlistStockOutputItem> items, boolean useCaseFailed) {
        this.items = items;
        this.useCaseFailed = useCaseFailed;
    }

    /** Returns the list of watchlist stock items.
     * @return the list of output items.
     */
    public List<WatchlistStockOutputItem> getItems() {
        return items;
    }

    /** Returns whether the use case failed.
     * @return true if failed, false otherwise.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}