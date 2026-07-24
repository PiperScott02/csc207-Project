package app;

import entity.Stock;
import entity.StockHolding;
import use_case.analysis.StockFinancialService;
import use_case.stock.StockDataAccessInterface;

public class StockHoldingFactory {
    private final StockDataAccessInterface stockDataAccessObject;

    public StockHoldingFactory(StockDataAccessInterface stockDataAccessObject) {
        this.stockDataAccessObject = stockDataAccessObject;
    }

    public StockHolding create(String ticker) {
        Stock stock = stockDataAccessObject.get(ticker);
        StockHolding holding = new StockHolding();
        holding.setStock(stock);
        return holding;
    }
}