/**package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockHoldingTest {

    private StockHolding holding;
    private Stock mockStock;
    private LocalDate today;
    private LocalDate yesterday;

    @BeforeEach
    void setUp() {
        holding = new StockHolding();
        mockStock = mock(Stock.class);
        today = LocalDate.now();
        yesterday = today.minusDays(1);

        when(mockStock.getTickerSymbol()).thenReturn("AAPL");
        when(mockStock.getClose()).thenReturn(new BigDecimal("150.00"));
        holding.setStock(mockStock);
    }

    @Test
    void testGetAndSetStock() {
        assertEquals(mockStock, holding.getStock());
    }

    @Test
    void testMakeTransactionWithExplicitDate() {
        when(mockStock.getCloseOnDate(yesterday)).thenReturn(new BigDecimal("145.00"));

        holding.makeTransaction(mockStock, 10.0, yesterday, TransactionType.BUY);

        List<Transaction> transactions = holding.getTransactions();
        assertEquals(1, transactions.size());

        Transaction t = transactions.get(0);
        assertEquals(yesterday, t.getDate());
        assertEquals(10.0, t.getNumberOfShares());
        assertEquals(TransactionType.BUY, t.getType());
        assertEquals(new BigDecimal("145.00"), t.getPricePerShare());
    }

    @Test
    void testMakeTransactionWithLastTradingDay() {
        // 1. Stub both getLastTradingDay AND getCloseOnDate on mockStock
        when(mockStock.getLastTradingDay()).thenReturn(today);
        when(mockStock.getCloseOnDate(today)).thenReturn(new BigDecimal("150.00"));

        // 2. Ensure holding's internal stock is mockStock
        holding.setStock(mockStock);

        // 3. Perform transaction
        holding.makeTransaction(mockStock, 5.0, TransactionType.BUY);

        List<Transaction> transactions = holding.getTransactions();
        assertEquals(1, transactions.size());

        Transaction t = transactions.get(0);

        // Assert the transaction date matches today
        assertNotNull(t.getDate(), "Transaction date should not be null");
        assertEquals(today, t.getDate());
        assertEquals(5.0, t.getNumberOfShares());
        assertEquals(TransactionType.BUY, t.getType());
        assertEquals(new BigDecimal("150.00"), t.getPricePerShare());
    }

    @Test
    void testGetQuantityOnDateAndCurrentShares() {
        LocalDate lastWeek = today.minusDays(7);

        holding.makeTransaction(mockStock, 10.0, lastWeek, TransactionType.BUY);
        holding.makeTransaction(mockStock, 4.0, yesterday, TransactionType.SELL);
        holding.makeTransaction(mockStock, 5.0, today.plusDays(1), TransactionType.BUY);

        // Quantity last week (BUY 10)
        assertEquals(10.0, holding.getQuantityOnDate(lastWeek));

        // Quantity yesterday (BUY 10 - SELL 4)
        assertEquals(6.0, holding.getQuantityOnDate(yesterday));

        // Quantity today via getNumberOfShares()
        assertEquals(6.0, holding.getNumberOfShares());
    }

    @Test
    void testCalculateTotalValue() {
        holding.makeTransaction(mockStock, 10.0, today, TransactionType.BUY);

        // 10 shares * $150.00 close price = $1500.00
        BigDecimal expectedValue = new BigDecimal("1500.00");
        assertEquals(0, expectedValue.compareTo(holding.calculateTotalValue()));
    }

    @Test
    void testCalculateTotalValueOnDateSuccess() {
        when(mockStock.getCloseOnDate(yesterday)).thenReturn(new BigDecimal("140.00"));
        holding.makeTransaction(mockStock, 5.0, yesterday, TransactionType.BUY);

        // 5 shares * $140.00 = $700.00
        BigDecimal expectedValue = new BigDecimal("700.00");
        BigDecimal actualValue = holding.calculateTotalValueOnDate(yesterday);

        assertNotNull(actualValue);
        assertEquals(0, expectedValue.compareTo(actualValue));
    }

    @Test
    void testCalculateTotalValueOnDateNullPriceReturnsNull() {
        when(mockStock.getCloseOnDate(yesterday)).thenReturn(null);

        assertNull(holding.calculateTotalValueOnDate(yesterday));
    }

    @Test
    void testExtractStocks() {
        Stock mockStock2 = mock(Stock.class);

        StockHolding holding1 = new StockHolding();
        holding1.setStock(mockStock);

        StockHolding holding2 = new StockHolding();
        holding2.setStock(mockStock2);

        StockHolding holdingWithNullStock = new StockHolding();

        List<StockHolding> holdings = List.of(holding1, holding2, holdingWithNullStock);

        List<Stock> extracted = StockHolding.extractStocks(holdings);

        assertEquals(2, extracted.size());
        assertTrue(extracted.contains(mockStock));
        assertTrue(extracted.contains(mockStock2));
    }
}
 **/