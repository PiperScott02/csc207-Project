package use_case;

import entity.Stock;

public interface TickerSearchDataAccessInterface {

    /**
     * Create a Stock with the basic information about the given tickerSymbol.
     * @param tickerSymbol ticker to create a stock entity for
     * @return Stock with the basic information about the given tickerSymbol
     */
    Stock createBasicStock(String tickerSymbol);

}
