package use_case.analysis;

import entity.Stock;

import java.io.IOException;

public interface StockDailyDataAccessInterface {

    Stock createStockAndHistory(String tickerSymbol) throws IOException, InterruptedException;

}
