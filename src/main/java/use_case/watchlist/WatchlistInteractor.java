package use_case.watchlist;

import entity.Stock;
import entity.User;
import entity.WatchlistStockItem;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.stock.StockDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/** The Interactor for the Watchlist use case, handling business logic for retrieving and populating watchlist items. */
public class WatchlistInteractor implements WatchlistInputBoundary {
    private final StockDataAccessInterface stockDataAccessObject;
    private final WatchlistOutputBoundary watchlistPresenter;
    private final LoggedInViewModel loggedInViewModel;

    public WatchlistInteractor(StockDataAccessInterface stockDataAccessObject,
                               WatchlistOutputBoundary watchlistPresenter,
                               LoggedInViewModel loggedInViewModel) {
        this.stockDataAccessObject = stockDataAccessObject;
        this.watchlistPresenter = watchlistPresenter;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void execute(WatchlistInputData watchlistInputData) {
        // Grab the user straight from the active session ViewModel
        if (loggedInViewModel.getState() == null || loggedInViewModel.getState().getUser() == null) {
            watchlistPresenter.prepareFailView("No active user session found.");
            return;
        }

        User user = loggedInViewModel.getState().getUser();

        if (user == null || user.getPortfolio() == null) {
            watchlistPresenter.prepareFailView("User or portfolio not found.");
            return;
        }

        List<WatchlistStockItem> rawItems = user.getPortfolio().getWatchlist();

        List<WatchlistOutputData.WatchlistStockOutputItem> outputItems = new ArrayList<>();
        if (rawItems != null) {
            for (WatchlistStockItem item : rawItems) {
                // Fetch the stock using stockDataAccessObject
                Stock stock = stockDataAccessObject.get(item.ticker());

                if (stock != null) {
                    WatchlistOutputData.WatchlistStockOutputItem outputItem =
                            new WatchlistOutputData.WatchlistStockOutputItem(
                                    stock.getTickerSymbol(),
                                    stock.getCompanyName(),
                                    stock.getClose().toString(),
                                    stock.getDailyPriceChange().toString()
                            );
                    outputItems.add(outputItem);
                } else {
                    // Fallback to basic entity data if stock isn't found in DAO
                    WatchlistOutputData.WatchlistStockOutputItem outputItem =
                            new WatchlistOutputData.WatchlistStockOutputItem(
                                    item.ticker(),
                                    item.companyName(),
                                    String.valueOf(item.closePrice()),
                                    String.valueOf(item.dailyPriceChange())
                            );
                    outputItems.add(outputItem);
                }
            }
        }

        WatchlistOutputData watchlistOutputData = new WatchlistOutputData(outputItems, false);
        watchlistPresenter.prepareSuccessView(watchlistOutputData);
    }
}