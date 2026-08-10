package use_case.similar_search;

import entity.SimilarStocks;

/**
 * DAO for the Similar Search Use Case.
 */
public interface SimilarSearchDataAccessInterface {

    /**
     * Returns the similar names to given string.
     * @param tickerSymbol keyword to find similar ticker symbols
     * @return List of similar company names/tickers for given keywork (tickerSymbol)
     */
    SimilarStocks similarStockInfo(String tickerSymbol);

}
