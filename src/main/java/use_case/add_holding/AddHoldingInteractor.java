package use_case.add_holding;

import java.io.IOException;
import java.time.LocalDate;
import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import entity.TransactionType;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.StockDailyDataAccessInterface;

/**
 * The Interactor for the Add Holding Use Case.
 */
public class AddHoldingInteractor implements AddHoldingInputBoundary {
    private final StockDailyDataAccessInterface stockDataAccessObject;
    private final AddHoldingOutputBoundary userPresenter;
    private final LoggedInViewModel loggedInViewModel;

    public AddHoldingInteractor(StockDailyDataAccessInterface stockDataAccessObject,
                                AddHoldingOutputBoundary userPresenter,
                                LoggedInViewModel loggedInViewModel) {
        this.stockDataAccessObject = stockDataAccessObject;
        this.userPresenter = userPresenter;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void execute(AddHoldingInputData addHoldingInputData) {
        // Unpack the DTO carried over from the Add Holding Controller AND force uppercase ticker
        String ticker = addHoldingInputData.getTicker().toUpperCase();
        double shares = addHoldingInputData.getShares();
        LocalDate purchaseDate = addHoldingInputData.getPurchaseDate();

        // Basic Validation & Semantics Checks
        if (ticker == null || ticker.trim().isEmpty()) {
            userPresenter.prepareFailView("Ticker symbol cannot be empty.");
            return;
        }
        if (shares <= 0) {
            userPresenter.prepareFailView("Number of shares must be greater than zero.");
            return;
        }
        if (purchaseDate == null) {
            userPresenter.prepareFailView("Purchase date cannot be null.");
            return;
        }
        if (purchaseDate.isAfter(LocalDate.now())) {
            userPresenter.prepareFailView("Purchase date cannot be in the future.");
            return;
        }
        if (loggedInViewModel.getState() == null || loggedInViewModel.getState().getUser() == null) {
            userPresenter.prepareFailView("No active user session found.");
            return;
        }

        // Access the current user's portfolio so holdings can be added
        Portfolio portfolio = loggedInViewModel.getState().getUser().getPortfolio();

        // Fetch the Stock from Data Access Object (talk to external API)
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

        // Check if the portfolio already contains a holding for this stock, or create a new one
        StockHolding holding = portfolio.getHoldingByTicker(ticker);
        if (holding == null) {
            holding = new StockHolding();
            holding.setStock(stock);
            portfolio.addHolding(holding);
        }

        // Record the purchase transaction using StockHolding method
        holding.makeTransaction(stock, shares, purchaseDate, TransactionType.BUY);

        // Package results and notify presenter
        AddHoldingOutputData outputData = new AddHoldingOutputData(ticker, shares, portfolio.getHoldings(), false);
        userPresenter.prepareSuccessView(outputData);
    }
}