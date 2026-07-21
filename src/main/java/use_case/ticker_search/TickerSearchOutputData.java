package use_case.ticker_search;

import entity.Stock;

/**
 * Output Data for Ticker Search Use Case.
 */
public class TickerSearchOutputData {

    private final Stock tickerStock;
    private final boolean useCaseFailed;

    public TickerSearchOutputData(Stock tickerStock, boolean useCaseFailed) {
        this.tickerStock = tickerStock;
        this.useCaseFailed = useCaseFailed;
    }

    public Stock getTickerStock() {
        return tickerStock;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
