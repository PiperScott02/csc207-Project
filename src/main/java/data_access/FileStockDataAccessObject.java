package data_access;

import data_access.stock_daily.StockService;
import entity.Stock;
import use_case.analysis.StockFinancialService;
import use_case.stock.StockDataAccessInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FileStockDataAccessObject implements StockDataAccessInterface {

    /** Checks whether a stock with the ticker identifier exists in the APIs' data by trying to create a stock obect
     * with it and seeing if it is successfully created.
     * @param ticker the stock ticker symbol.
     * @return bool whether the stock ticker is found or not in the API.
     */
    @Override
    public boolean existsByName(String ticker) {
        try {
            StockService stockService = new StockService();
            TimeUnit.SECONDS.sleep(1);
            Stock stock = stockService.createStockAndHistory(ticker);
            return stock != null && stock.getHistoricalTimeline() != null && !stock.getHistoricalTimeline().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /** Tries to create a stock object for the given ticker.
     * @param ticker the stock ticker symbol.
     * @return
     */
    @Override
    public Stock get(String ticker) {
        try {
            StockService stockService = new StockService();
            return stockService.createStockAndHistory(ticker);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch stock data for: " + ticker, e);
        }
    }
}