package use_case.stock;

import entity.Stock;

/** Data access interface for stock-related operations. */
public interface StockDataAccessInterface {

    /** Checks whether a stock with the given ticker symbol exists.
     * @param ticker the stock ticker symbol.
     * @return true if the stock exists, false otherwise.
     */
    boolean existsByName(String ticker);

    /** Retrieves the Stock entity corresponding to the given ticker symbol.
     * @param ticker the stock ticker symbol.
     * @return the Stock object.
     */
    Stock get(String ticker);
}