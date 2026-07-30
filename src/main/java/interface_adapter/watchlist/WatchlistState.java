package interface_adapter.watchlist;

import java.util.ArrayList;
import java.util.List;

/** Represents the view state for the watchlist, holding a list of stock items,
 * error messages, and support for individual stock details. */
public class WatchlistState {

    /** Inner representation of a stock item in the watchlist. */
    public static class WatchlistStockItem {
        private String ticker = "";
        private String companyName = "";
        private String close = "";
        private String dailyPriceChange = "";

        public WatchlistStockItem(String ticker, String companyName, String close, String dailyPriceChange) {
            this.ticker = ticker;
            this.companyName = companyName;
            this.close = close;
            this.dailyPriceChange = dailyPriceChange;
        }

        public WatchlistStockItem(WatchlistStockItem copy) {
            this.ticker = copy.ticker;
            this.companyName = copy.companyName;
            this.close = copy.close;
            this.dailyPriceChange = copy.dailyPriceChange;
        }

        public String getTicker() { return ticker; }
        public String getCompanyName() { return companyName; }
        public String getClose() { return close; }
        public String getDailyPriceChange() { return dailyPriceChange; }

        public void setTicker(String ticker) { this.ticker = ticker; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }
        public void setClose(String close) { this.close = close; }
        public void setDailyPriceChange(String dailyPriceChange) { this.dailyPriceChange = dailyPriceChange; }
    }

    private List<WatchlistStockItem> items = new ArrayList<>();
    private String errorMessage = "";

    /** Copy constructor to create a new WatchlistState from an existing one.
     * @param copy the WatchlistState to copy from.
     */
    public WatchlistState(WatchlistState copy) {
        this.errorMessage = copy.errorMessage;
        this.items = new ArrayList<>();
        for (WatchlistStockItem item : copy.items) {
            this.items.add(new WatchlistStockItem(item));
        }
    }

    /** Default constructor to create an empty WatchlistState. */
    public WatchlistState() {
    }

    /** Returns the list of watchlist stock items.
     * @return the list of items.
     */
    public List<WatchlistStockItem> getItems() {
        return items;
    }

    /** Sets the list of watchlist stock items.
     * @param items the list of items to set.
     */
    public void setItems(List<WatchlistStockItem> items) {
        this.items = items;
    }

    /** Returns the error message.
     * @return the error message string.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /** Sets the error message.
     * @param errorMessage the error message string to set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}