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
    public Stock createBasicStock(String tickerSymbol) throws IOException, InterruptedException {
        Stock stock = this.tickerSearchOverviewDataAccessObject.createBasicStock(tickerSymbol);
        if (stock == null) {
            stock = this.tickerSearchDailyDataAccessObject.createBasicStock(tickerSymbol);
        }
        return stock;
    }
}
