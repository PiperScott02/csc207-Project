package data_access.ticker_search;

import entity.Stock;
import use_case.TickerSearchDataAccessInterface;

import java.io.IOException;

public class TickerSearchDataAccessObject implements TickerSearchDataAccessInterface {
    private final TickerSearchDailyDataAccessObject tickerSearchDailyDataAccessObject;
    private final TickerSearchOverviewDataAccessObject tickerSearchOverviewDataAccessObject;

    public TickerSearchDataAccessObject(String apiKey) {
        this.tickerSearchDailyDataAccessObject = new TickerSearchDailyDataAccessObject(apiKey);
        this.tickerSearchOverviewDataAccessObject = new TickerSearchOverviewDataAccessObject(apiKey);
    }

    @Override
    public Stock createBasicStock(String tickerSymbol) {
        try {
            return this.tickerSearchOverviewDataAccessObject.createBasicStock(tickerSymbol);
        } catch (RuntimeException e) {
            return this.tickerSearchDailyDataAccessObject.createBasicStock(tickerSymbol);
        }
    }
}
