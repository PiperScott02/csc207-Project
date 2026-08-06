package use_case.stock;

import entity.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StockInteractorTest {

    private InMemoryStockDAO stockDAO;
    private TestStockPresenter presenter;

    @BeforeEach
    void setUp() {
        stockDAO = new InMemoryStockDAO();
        presenter = new TestStockPresenter();
    }

    @Test
    void testExecuteStockNotFoundFails() {
        StockInputData inputData = new StockInputData("INVALID");
        StockInteractor interactor = new StockInteractor(stockDAO, presenter);

        interactor.execute(inputData);

        assertTrue(presenter.isFailViewCalled);
        assertEquals("Stock ticker not found: INVALID", presenter.errorMessage);
        assertFalse(presenter.isSuccessViewCalled);
    }

    @Test
    void testExecuteMissingBenchmarkFails() {
        // Setup AAPL Stock without saving SPY to the DAO
        Stock aapl = new Stock();
        aapl.setTickerSymbol("AAPL");
        aapl.setCompanyName("Apple Inc.");
        aapl.setClose(new BigDecimal("150.00"));
        aapl.setDailyChange(new BigDecimal("2.50"));
        stockDAO.save(aapl);

        StockInputData inputData = new StockInputData("AAPL");
        StockInteractor interactor = new StockInteractor(stockDAO, presenter);

        interactor.execute(inputData);

        assertTrue(presenter.isFailViewCalled);
        assertEquals("Benchmark stock (SPY) data not found.", presenter.errorMessage);
        assertFalse(presenter.isSuccessViewCalled);
    }

    @Test
    void testExecuteSuccess() {
        // Setup AAPL Stock
        Stock aapl = new Stock();
        aapl.setTickerSymbol("AAPL");
        aapl.setCompanyName("Apple Inc.");
        aapl.setClose(new BigDecimal("150.00"));
        aapl.setDailyChange(new BigDecimal("2.50"));
        aapl.setBeta(1.20);
        aapl.setAlpha(0.05);
        aapl.setSharpeRatio(1.85);

        // Setup Market benchmark Stock (SPY)
        Stock spy = new Stock();
        spy.setTickerSymbol("SPY");
        spy.setCompanyName("SPDR S&P 500 ETF Trust");
        spy.setClose(new BigDecimal("400.00"));
        spy.setDailyChange(new BigDecimal("1.10"));
        spy.setBeta(1.00);
        spy.setAlpha(0.00);
        spy.setSharpeRatio(1.10);

        stockDAO.save(aapl);
        stockDAO.save(spy);

        StockInputData inputData = new StockInputData("AAPL");
        StockInteractor interactor = new StockInteractor(stockDAO, presenter);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccessViewCalled);
        assertFalse(presenter.isFailViewCalled);
        assertNotNull(presenter.outputData);

        assertEquals("AAPL", presenter.outputData.getTickerSymbol());
        assertEquals("Apple Inc.", presenter.outputData.getCompanyName());
        assertEquals("150.00", presenter.outputData.getClose());
        assertEquals("2.50", presenter.outputData.getDailyPriceChange());
        assertNotNull(presenter.outputData.getBeta());
        assertNotNull(presenter.outputData.getAnnualizedAlpha());
        assertNotNull(presenter.outputData.getAnnualizedSharpeRatio());
        assertFalse(presenter.outputData.isUseCaseFailed());
    }

    // --- Fake Test Doubles ---

    private static class InMemoryStockDAO implements StockDataAccessInterface {
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

    private static class TestStockPresenter implements StockOutputBoundary {
        boolean isSuccessViewCalled = false;
        boolean isFailViewCalled = false;
        StockOutputData outputData = null;
        String errorMessage = null;

        @Override
        public void prepareSuccessView(StockOutputData stockOutputData) {
            this.isSuccessViewCalled = true;
            this.outputData = stockOutputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.isFailViewCalled = true;
            this.errorMessage = error;
        }
    }
}