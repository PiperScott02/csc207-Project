package use_case.watchlist;

import entity.*;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.stock.StockDataAccessInterface;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WatchlistInteractorTest {

    private InMemoryStockDataAccessObject stockDAO;
    private TestWatchlistPresenter presenter;
    private LoggedInViewModel loggedInViewModel;

    @BeforeEach
    void setUp() {
        stockDAO = new InMemoryStockDataAccessObject();
        presenter = new TestWatchlistPresenter();
        loggedInViewModel = new LoggedInViewModel();
    }

    @Test
    void testExecuteNullUserFails() {
        // State has no user
        loggedInViewModel.setState(new LoggedInState());

        WatchlistInputData inputData = new WatchlistInputData(null);
        WatchlistInteractor interactor = new WatchlistInteractor(stockDAO, presenter, loggedInViewModel);

        interactor.execute(inputData);

        assertTrue(presenter.isFailViewCalled);
        assertEquals("User or portfolio not found.", presenter.errorMessage);
        assertFalse(presenter.isSuccessViewCalled);
    }

    @Test
    void testExecuteNullPortfolioFails() {
        User user = new CommonUser("Piper", "123");
        user.setPortfolio(null);

        LoggedInState state = new LoggedInState();
        state.setUser(user);
        loggedInViewModel.setState(state);

        WatchlistInputData inputData = new WatchlistInputData(user);
        WatchlistInteractor interactor = new WatchlistInteractor(stockDAO, presenter, loggedInViewModel);

        interactor.execute(inputData);

        assertTrue(presenter.isFailViewCalled);
        assertEquals("User or portfolio not found.", presenter.errorMessage);
        assertFalse(presenter.isSuccessViewCalled);
    }

    @Test
    void testExecuteSuccessWithStockInDAO() {
        // Construct Stock using setters and BigDecimal
        Stock aaplStock = new Stock();
        aaplStock.setTickerSymbol("AAPL");
        aaplStock.setCompanyName("Apple Inc.");
        aaplStock.setClose(new BigDecimal("150.00"));
        aaplStock.setDailyChange(new BigDecimal("2.50"));

        stockDAO.save(aaplStock);

        // Setup User Portfolio with Watchlist items using BigDecimal
        List<WatchlistStockItem> watchlist = new ArrayList<>();
        watchlist.add(new WatchlistStockItem("AAPL", "Apple Inc.", new BigDecimal("150.00"), new BigDecimal("2.50")));

        Portfolio portfolio = new Portfolio();
        portfolio.setWatchlist(watchlist);

        User user = new CommonUser("Piper", "123");
        user.setPortfolio(portfolio);

        LoggedInState state = new LoggedInState();
        state.setUser(user);
        loggedInViewModel.setState(state);

        WatchlistInputData inputData = new WatchlistInputData(user);
        WatchlistInteractor interactor = new WatchlistInteractor(stockDAO, presenter, loggedInViewModel);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccessViewCalled);
        assertFalse(presenter.isFailViewCalled);
        assertNotNull(presenter.outputData);

        List<WatchlistOutputData.WatchlistStockOutputItem> items = presenter.outputData.getItems();
        assertEquals(1, items.size());

        WatchlistOutputData.WatchlistStockOutputItem item = items.get(0);
        assertEquals("AAPL", item.getTicker());
        assertEquals("Apple Inc.", item.getCompanyName());
        assertEquals("150.00", item.getClose());
        assertEquals("2.50", item.getDailyPriceChange());
    }

    @Test
    void testExecuteSuccessFallbackWhenStockNotInDAO() {
        // Stock is NOT saved to stockDAO, testing the fallback branch with BigDecimal items
        List<WatchlistStockItem> watchlist = new ArrayList<>();
        watchlist.add(new WatchlistStockItem("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00"), new BigDecimal("-15.00")));

        Portfolio portfolio = new Portfolio();
        portfolio.setWatchlist(watchlist);

        User user = new CommonUser("Piper", "123");
        user.setPortfolio(portfolio);

        LoggedInState state = new LoggedInState();
        state.setUser(user);
        loggedInViewModel.setState(state);

        WatchlistInputData inputData = new WatchlistInputData(user);
        WatchlistInteractor interactor = new WatchlistInteractor(stockDAO, presenter, loggedInViewModel);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccessViewCalled);
        assertFalse(presenter.isFailViewCalled);

        List<WatchlistOutputData.WatchlistStockOutputItem> items = presenter.outputData.getItems();
        assertEquals(1, items.size());

        WatchlistOutputData.WatchlistStockOutputItem item = items.get(0);
        assertEquals("GOOGL", item.getTicker());
        assertEquals("Alphabet Inc.", item.getCompanyName());
        assertEquals("2800.00", item.getClose());
        assertEquals("-15.00", item.getDailyPriceChange());
    }

    @Test
    void testExecuteSuccessWithNullWatchlist() {
        Portfolio portfolio = new Portfolio();
        portfolio.setWatchlist(null);

        User user = new CommonUser("Piper", "123");
        user.setPortfolio(portfolio);

        LoggedInState state = new LoggedInState();
        state.setUser(user);
        loggedInViewModel.setState(state);

        WatchlistInputData inputData = new WatchlistInputData(user);
        WatchlistInteractor interactor = new WatchlistInteractor(stockDAO, presenter, loggedInViewModel);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccessViewCalled);
        assertFalse(presenter.isFailViewCalled);
        assertNotNull(presenter.outputData);
        assertTrue(presenter.outputData.getItems().isEmpty());
    }

    // --- Fake Test Doubles ---

    private static class InMemoryStockDataAccessObject implements StockDataAccessInterface {
        private final Map<String, Stock> stocks = new HashMap<>();

        public void save(Stock stock) {
            stocks.put(stock.getTickerSymbol(), stock);
        }

        @Override
        public boolean existsByName(String ticker) {
            return stocks.containsKey(ticker);
        }

        @Override
        public Stock get(String ticker) {
            return stocks.get(ticker);
        }
    }

    private static class TestWatchlistPresenter implements WatchlistOutputBoundary {
        boolean isSuccessViewCalled = false;
        boolean isFailViewCalled = false;
        WatchlistOutputData outputData = null;
        String errorMessage = null;

        @Override
        public void prepareSuccessView(WatchlistOutputData watchlistOutputData) {
            this.isSuccessViewCalled = true;
            this.outputData = watchlistOutputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.isFailViewCalled = true;
            this.errorMessage = error;
        }
    }
}