package use_case.watchlist;

import interface_adapter.watchlist.WatchlistState;
import java.util.List;

public interface WatchlistDataAccessInterface {


    /**
     * Adds a stock ticker to the user's saved watchlist.
     * @param ticker the stock ticker symbol to add
     */
    void addWatchlistStock(String ticker);


    /**
     * Removes a stock ticker from the user's saved watchlist.
     * @param ticker the stock ticker symbol to remove
     */
    void removeWatchlistStock(String ticker);


    /**
     * Retrieves the current list of watchlist stock items.
     * @return a list of WatchlistStockItem objects
     */
    List<WatchlistState.WatchlistStockItem> getWatchlistItems();


    /**
     * Checks if a given ticker already exists in the watchlist.
     * @param ticker the stock ticker symbol to check
     * @return true if it exists, false otherwise
     */
    boolean exists(String ticker);
}