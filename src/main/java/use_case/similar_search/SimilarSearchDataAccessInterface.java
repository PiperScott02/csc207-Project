package use_case.similar_search;

import java.io.IOException;

/**
 * DAO for the Similar Search Use Case.
 */
public interface SimilarSearchDataAccessInterface {

    /**
     * Returns the similar names to given string.
     * @param tickerSymbol
     * @return List of similar company names/tickers for given keywork (tickerSymbol)
     * @throws IOException
     * @throws InterruptedException
     */
    String[][] similarStockInfo(String tickerSymbol) throws IOException, InterruptedException;

}
