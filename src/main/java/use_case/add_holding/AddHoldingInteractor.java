package use_case.add_holding;

import java.io.IOException;
import java.time.LocalDate;
import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import entity.TransactionType;
import use_case.StockDailyDataAccessInterface;

/**
 * The Interactor for the Add Holding Use Case.
 */
public class AddHoldingInteractor implements AddHoldingInputBoundary {
    private final StockDailyDataAccessInterface stockDataAccessObject;
    private final AddHoldingOutputBoundary userPresenter;
    private final Portfolio portfolio;

    public AddHoldingInteractor(StockDailyDataAccessInterface stockDataAccessObject,
                                AddHoldingOutputBoundary userPresenter,
                                Portfolio portfolio) {
        this.stockDataAccessObject = stockDataAccessObject;
        this.userPresenter = userPresenter;
        this.portfolio = portfolio;
    }

    @Override
    public void execute(AddHoldingInputData addHoldingInputData) {
        // 1. Unpack the DTO
        String ticker = addHoldingInputData.getTicker();
        double shares = addHoldingInputData.getShares();
        LocalDate purchaseDate = addHoldingInputData.getPurchaseDate();

        // 2. Basic validation
        if (ticker == null || ticker.trim().isEmpty()) {
            userPresenter.prepareFailView("Ticker symbol cannot be empty.");
            return;
        }
        if (shares <= 0) {
            userPresenter.prepareFailView("Number of shares must be greater than zero.");
            return;
        }

        // 3. Fetch the Stock from your Data Access Object
        Stock stock = null;
        try {
            stock = stockDataAccessObject.createStockAndHistory(ticker);
        } catch (IOException | InterruptedException e) {
            userPresenter.prepareFailView("Error fetching stock data: " + e.getMessage());
            return;
        }

        if (stock == null) {
            userPresenter.prepareFailView("Stock ticker not found.");
            return;
        }

        // 4. Check if the portfolio already contains a holding for this stock, or create a new one
        StockHolding holding = portfolio.getHoldingByTicker(ticker);
        if (holding == null) {
            holding = new StockHolding();
            holding.setStock(stock);
            portfolio.addHolding(holding);
        }

        System.out.println("Price on " + purchaseDate + ": " + stock.getCloseOnDate(purchaseDate)); //TEST PRINT

        // 5. Record the purchase transaction using your StockHolding method
        // (Triggers the method that automatically looks up the closing price on that date)
        holding.makeTransaction(stock, shares, purchaseDate, TransactionType.BUY);

        // 6. Package results and notify presenter
        AddHoldingOutputData outputData = new AddHoldingOutputData(ticker, shares, portfolio.getHoldings(), false);
        userPresenter.prepareSuccessView(outputData);
    }
}