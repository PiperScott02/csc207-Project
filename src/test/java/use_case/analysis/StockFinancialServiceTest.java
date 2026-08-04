package use_case.analysis;

import entity.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StockFinancialServiceTest {

    private Stock mockStock;
    private LocalDate date1;
    private LocalDate date2;
    private LocalDate date3;

    @BeforeEach
    void setUp() {
        // Initialize a mock Stock object before each test runs
        mockStock = Mockito.mock(Stock.class);

        // Set up some sample dates
        date1 = LocalDate.of(2026, 6, 1);
        date2 = LocalDate.of(2026, 6, 2);
        date3 = LocalDate.of(2026, 6, 3);
    }

    @Test
    void returnRatios() {
        // 1. ARRANGE: Define what the mock Stock should return when queried
        List<LocalDate> sortedDates = Arrays.asList(date1, date2, date3);
        when(mockStock.getDatesSorted()).thenReturn(sortedDates);

        // Setup behavior for date2 (Day 2 change: 2.0, Previous close on date1: 100.0)
        when(mockStock.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockStock.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("2.0"));
        when(mockStock.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));

        // Setup behavior for date3 (Day 3 change: -1.5, Previous close on date2: 102.0)
        when(mockStock.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockStock.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("-1.5"));
        when(mockStock.getCloseOnDate(date2)).thenReturn(new BigDecimal("102.0"));

        // 2. ACT: Call the method you are testing
        List<Double> ratios = StockFinancialService.returnRatios(mockStock);

        System.out.println(ratios);

        // 3. ASSERT: Verify the results are correct
        assertNotNull(ratios);
        assertEquals(2, ratios.size(), "Should have 2 return ratios for 3 sorted dates");

        // Ratio 1: 2.0 / 100.0 = 0.02
        assertEquals(0.02, ratios.get(0), 0.0001);

        // Ratio 2: -1.5 / 102.0 ≈ -0.01470588
        assertEquals(-0.01470588, ratios.get(1), 0.0001);
    }

    @Test
    void calculateBeta() {
        // 1. ARRANGE: Create a market stock mock and a target stock mock
        Stock mockMarket = Mockito.mock(Stock.class);

        List<LocalDate> sortedDates = Arrays.asList(date1, date2, date3);

        // Setup dates for both stocks
        when(mockStock.getDatesSorted()).thenReturn(sortedDates);
        when(mockMarket.getDatesSorted()).thenReturn(sortedDates);

        // Setup behavior for target Stock (mockStock)
        // Date 2: change 2.0, prev close 100.0 -> ratio = 0.02
        when(mockStock.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockStock.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("2.0"));
        when(mockStock.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));

        // Date 3: change -1.5, prev close 102.0 -> ratio ≈ -0.01470588
        when(mockStock.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockStock.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("-1.5"));
        when(mockStock.getCloseOnDate(date2)).thenReturn(new BigDecimal("102.0"));

        // Setup behavior for market Stock (mockMarket) with different returns to test covariance/variance
        // Date 2: change 1.0, prev close 50.0 -> ratio = 0.02
        when(mockMarket.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockMarket.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("1.0"));
        when(mockMarket.getCloseOnDate(date1)).thenReturn(new BigDecimal("50.0"));

        // Date 3: change 1.0, prev close 51.0 -> ratio ≈ 0.01960784
        when(mockMarket.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockMarket.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("1.0"));
        when(mockMarket.getCloseOnDate(date2)).thenReturn(new BigDecimal("51.0"));

        // 2. ACT: Call calculateBeta
        double beta = StockFinancialService.calculateBeta(mockStock, mockMarket);

        System.out.println("Calculated Beta: " + beta);

        // 3. ASSERT: Verify the result
        // Stock returns: [0.02, -0.01470588]
        // Market returns: [0.02, 0.01960784]
        assertEquals(88.49, beta, 0.01);    }

    @Test
    void calculateBetaZeroMarketVariance() {
        // Edge case: Market variance is 0 (all market returns are identical)
        Stock mockMarket = Mockito.mock(Stock.class);
        List<LocalDate> sortedDates = Arrays.asList(date1, date2, date3);

        when(mockStock.getDatesSorted()).thenReturn(sortedDates);
        when(mockMarket.getDatesSorted()).thenReturn(sortedDates);

        // Stock data
        when(mockStock.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockStock.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("2.0"));
        when(mockStock.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));
        when(mockStock.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockStock.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("1.0"));
        when(mockStock.getCloseOnDate(date2)).thenReturn(new BigDecimal("102.0"));

        // Market data with 0 variance (flat returns)
        when(mockMarket.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockMarket.getDailyChangeOnDate(date2)).thenReturn(BigDecimal.ZERO);
        when(mockMarket.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));
        when(mockMarket.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockMarket.getDailyChangeOnDate(date3)).thenReturn(BigDecimal.ZERO);
        when(mockMarket.getCloseOnDate(date2)).thenReturn(new BigDecimal("100.0"));

        double beta = StockFinancialService.calculateBeta(mockStock, mockMarket);
        assertEquals(0.0, beta, 0.0001, "Beta should safely return 0.0 if market variance is zero");
    }
    @Test
    void calculateAlpha() {
        Stock mockMarket = Mockito.mock(Stock.class);
        List<LocalDate> sortedDates = Arrays.asList(date1, date2, date3);

        when(mockStock.getDatesSorted()).thenReturn(sortedDates);
        when(mockMarket.getDatesSorted()).thenReturn(sortedDates);

        // Target Stock Ratios: [0.10, 0.20]
        when(mockStock.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockStock.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("10.0"));
        when(mockStock.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));

        when(mockStock.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockStock.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("20.0"));
        when(mockStock.getCloseOnDate(date2)).thenReturn(new BigDecimal("100.0"));

        // Market Stock Ratios: [0.05, 0.10]
        when(mockMarket.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockMarket.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("5.0"));
        when(mockMarket.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));

        when(mockMarket.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockMarket.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("10.0"));
        when(mockMarket.getCloseOnDate(date2)).thenReturn(new BigDecimal("100.0"));

        double alpha = StockFinancialService.calculateAlpha(mockStock, mockMarket);

        System.out.println("Calculated Alpha: " + alpha);

        assertEquals(0.0001476, alpha, 0.000001);
    }

    @Test
    void calculateSharpeRatio() {
        List<LocalDate> sortedDates = Arrays.asList(date1, date2, date3);
        when(mockStock.getDatesSorted()).thenReturn(sortedDates);

        // Target Stock Ratios: [0.10, 0.20]
        when(mockStock.getPreviousTradingDay(date2)).thenReturn(date1);
        when(mockStock.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("10.0"));
        when(mockStock.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));

        when(mockStock.getPreviousTradingDay(date3)).thenReturn(date2);
        when(mockStock.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("20.0"));
        when(mockStock.getCloseOnDate(date2)).thenReturn(new BigDecimal("100.0"));

        double sharpeRatio = StockFinancialService.calculateSharpeRatio(mockStock);


        System.out.println("Calculated Sharpe Ratio: " + sharpeRatio);
        // Assert against the expected value (~2.1192)
        assertEquals(2.1192, sharpeRatio, 0.001);
    }

    @Test
    void buildCovariancesArray() {
        // 1. ARRANGE: Create two separate mock stocks
        Stock stockA = Mockito.mock(Stock.class);
        Stock stockB = Mockito.mock(Stock.class);

        List<LocalDate> sortedDates = Arrays.asList(date1, date2, date3);

        // Setup dates for both stocks
        when(stockA.getDatesSorted()).thenReturn(sortedDates);
        when(stockB.getDatesSorted()).thenReturn(sortedDates);

        // Setup Ratios for stockA: [0.10, 0.20]
        when(stockA.getPreviousTradingDay(date2)).thenReturn(date1);
        when(stockA.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("10.0"));
        when(stockA.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));

        when(stockA.getPreviousTradingDay(date3)).thenReturn(date2);
        when(stockA.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("20.0"));
        when(stockA.getCloseOnDate(date2)).thenReturn(new BigDecimal("100.0"));

        // Setup Ratios for stockB: [0.05, 0.10] (will result in a symmetric covariance matrix)
        when(stockB.getPreviousTradingDay(date2)).thenReturn(date1);
        when(stockB.getDailyChangeOnDate(date2)).thenReturn(new BigDecimal("5.0"));
        when(stockB.getCloseOnDate(date1)).thenReturn(new BigDecimal("100.0"));

        when(stockB.getPreviousTradingDay(date3)).thenReturn(date2);
        when(stockB.getDailyChangeOnDate(date3)).thenReturn(new BigDecimal("10.0"));
        when(stockB.getCloseOnDate(date2)).thenReturn(new BigDecimal("100.0"));

        List<Stock> stockList = Arrays.asList(stockA, stockB);

        // 2. ACT: Call buildCovariancesArray
        double[][] covarianceMatrix = StockFinancialService.buildCovariancesArray(stockList);

        // 3. ASSERT: Verify dimensions and properties of the array
        assertNotNull(covarianceMatrix);
        assertEquals(2, covarianceMatrix.length, "Matrix should have 2 rows");
        assertEquals(2, covarianceMatrix[0].length, "Matrix should have 2 columns");

        // The diagonal elements (covariance of a stock with itself) should equal its variance
        double varianceA = StatisticsService.calculateVariance(StockFinancialService.returnRatios(stockA));
        System.out.println("Calculated Variance A: " + varianceA);
        assertEquals(varianceA, covarianceMatrix[0][0], 0.0001);

        // The matrix should be symmetric (cov(A,B) == cov(B,A))
        assertEquals(covarianceMatrix[0][1], covarianceMatrix[1][0], 0.0001);
        System.out.println("Calculated Covariance 0,1: " + covarianceMatrix[0][1]);
    }
}