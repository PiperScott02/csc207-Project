package use_case.add_watchlist;

import data_access.FileUserDataAccessObject;
import entity.Portfolio;
import entity.Stock;
import entity.User;
import entity.WatchlistStockItem;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.StockDailyDataAccessInterface;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * The Interactor for the Add Watchlist Item Use Case.
 */
public class AddWatchlistInteractor implements AddWatchlistInputBoundary {

    private final StockDailyDataAccessInterface stockDataAccessObject;
    private final AddWatchlistOutputBoundary userPresenter;
    private final LoggedInViewModel loggedInViewModel;
    private final FileUserDataAccessObject userDataAccessObject;

    public AddWatchlistInteractor(StockDailyDataAccessInterface stockDataAccessObject,
                                  AddWatchlistOutputBoundary userPresenter,
                                  LoggedInViewModel loggedInViewModel,
                                  FileUserDataAccessObject userDataAccessObject) {
        this.stockDataAccessObject = stockDataAccessObject;
        this.userPresenter = userPresenter;
        this.loggedInViewModel = loggedInViewModel;
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void execute(AddWatchlistInputData addWatchlistInputData) {
        // 1. Unpack the DTO
        String ticker = addWatchlistInputData.getTicker();

        // 2. Basic validation
        if (ticker == null || ticker.trim().isEmpty()) {
            userPresenter.prepareFailView("Ticker symbol cannot be empty.");
            return;
        }

        // Retrieve the current user's portfolio dynamically
        if (loggedInViewModel.getState() == null || loggedInViewModel.getState().getUser() == null) {
            userPresenter.prepareFailView("No active user session found.");
            return;
        }
        User currentUser = loggedInViewModel.getState().getUser();
        Portfolio portfolio = loggedInViewModel.getState().getUser().getPortfolio();

        // Check if already in the watchlist
        boolean alreadyExists = portfolio.getWatchlist().stream()
                .anyMatch(item -> item.ticker().equalsIgnoreCase(ticker));
        if (alreadyExists) {
            userPresenter.prepareFailView("Stock is already in your watchlist.");
            return;
        }

        // 3. Fetch the Stock from your Data Access Object to get pricing details
        Stock stock;
        try {
            stock = stockDataAccessObject.createStockAndHistory(ticker);
        } catch (IOException | InterruptedException e) {
            userPresenter.prepareFailView("Error fetching stock data: " + e.getMessage());
            return;
        }

        if (stock == null) {
            // Alternatively, fallback to search if history creation fails
            userPresenter.prepareFailView("Stock ticker not found.");
            return;
        }

        // 4. Extract latest close price and daily change from the stock entity
        BigDecimal closePrice = stock.getClose();
        BigDecimal dailyChange = stock.getDailyPriceChange();

        // 5. Create the WatchlistStockItem record and add to portfolio
        WatchlistStockItem newItem = new WatchlistStockItem(
                stock.getTickerSymbol(),
                stock.getCompanyName(),
                closePrice,
                dailyChange
        );

        portfolio.addWatchlist(newItem);

        // Save to CSV disk file
        userDataAccessObject.save(currentUser);

        // 6. Package results and notify presenter
        AddWatchlistOutputData outputData = new AddWatchlistOutputData(ticker, portfolio.getWatchlist(), false);
        userPresenter.prepareSuccessView(outputData);
    }
}