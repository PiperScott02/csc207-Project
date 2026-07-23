package interface_adapter.stock;

import use_case.stock.StockInputBoundary;
import use_case.stock.StockInputData;

/** The controller for the Stock use case.*/

public class StockController {
    private final StockInputBoundary stockUseCaseInteractor;

    public StockController(StockInputBoundary stockInputBoundary) {
        this.stockUseCaseInteractor = stockInputBoundary;
    }

    /**Executes the Stock Use Case.
     * @param ticker the ticker of the stock to be displayed.
     */
    public void execute(String ticker) {
        final StockInputData stockInputData = new StockInputData(ticker);

        stockUseCaseInteractor.execute(stockInputData);
    }
}
