package use_case.add_holding;

import java.io.IOException;
import java.time.LocalDate;

import entity.*;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.StockDailyDataAccessInterface;
import data_access.FileUserDataAccessObject;

/**
 * The Interactor for the Add Holding Use Case.
 */
public class AddHoldingInteractor implements AddHoldingInputBoundary {
    private final StockDailyDataAccessInterface stockDataAccessObject;
    private final AddHoldingOutputBoundary userPresenter;
    private final LoggedInViewModel loggedInViewModel;
    private final FileUserDataAccessObject userDataAccessObject;

    public AddHoldingInteractor(StockDailyDataAccessInterface stockDataAccessObject,
                                AddHoldingOutputBoundary userPresenter,
                                LoggedInViewModel loggedInViewModel,
                                FileUserDataAccessObject userDataAccessObject) {
        this.stockDataAccessObject = stockDataAccessObject;
        this.userPresenter = userPresenter;
        this.loggedInViewModel = loggedInViewModel;
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void execute(AddHoldingInputData addHoldingInputData) {
        if (loggedInViewModel.getState() == null || loggedInViewModel.getState().getUser() == null) {
            userPresenter.prepareFailView("No active user session found.");
            return;
        }

        User currentUser = loggedInViewModel.getState().getUser();
        Portfolio portfolio = currentUser.getPortfolio();

        String ticker = addHoldingInputData.getTicker().toUpperCase();
        double shares = addHoldingInputData.getShares();
        LocalDate purchaseDate = addHoldingInputData.getPurchaseDate();

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

        StockHolding holding = portfolio.getHoldingByTicker(ticker);
        if (holding == null) {
            holding = new StockHolding();
            holding.setStock(stock);
            portfolio.addHolding(holding);
        }

        holding.makeTransaction(stock, shares, purchaseDate, TransactionType.BUY);

        userDataAccessObject.save(currentUser);

        AddHoldingOutputData outputData = new AddHoldingOutputData(ticker, shares, portfolio.getHoldings(), false);
        userPresenter.prepareSuccessView(outputData);
    }
}