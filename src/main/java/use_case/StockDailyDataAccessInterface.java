package use_case;

import entity.Stock;

import java.io.IOException;

/**
 * DAO for the Ticker Search Use Case.
 */
public interface StockDailyDataAccessInterface {

    /**
     * Return Stock object associated with the given ticker.
     * @param tickerSymbol
     * @return Stock object associated with tickerSymbol
     * @throws IOException
     * @throws InterruptedException
     */
    Stock createStockAndHistory(String tickerSymbol) throws IOException, InterruptedException;

}
