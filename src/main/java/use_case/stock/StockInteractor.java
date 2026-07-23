package use_case.stock;

import entity.Stock;
import use_case.analysis.StockFinancialService;

/** The Interactor for the Stock use case, handling business logic for stock retrieval and analysis. */
public class StockInteractor implements StockInputBoundary {
    private final StockDataAccessInterface stockDataAccessObject;
    private final StockOutputBoundary stockPresenter;

    public StockInteractor(StockDataAccessInterface stockDataAccessObject,
                           StockOutputBoundary stockPresenter) {
        this.stockDataAccessObject = stockDataAccessObject;
        this.stockPresenter = stockPresenter;
    }

    @Override
    public void execute(StockInputData stockInputData) {
        String ticker = stockInputData.getTickerSymbol();

        if (!stockDataAccessObject.existsByName(ticker)) {
            stockPresenter.prepareFailView("Stock ticker not found: " + ticker);
            return;
        }

        Stock stock = stockDataAccessObject.get(ticker);
        Stock market = stockDataAccessObject.get("SPY");

        StockFinancialService.calculateAndAssignMetrics(stock, market);

        StockOutputData stockOutputData = new StockOutputData(
                stock.getTickerSymbol(),
                stock.getCompanyName(),
                stock.getClose().toString(),
                stock.getBeta().toString(),
                stock.getAlpha().toString(),
                stock.getSharpeRatio().toString(),
                false
        );
        stockPresenter.prepareSuccessView(stockOutputData);
    }
}