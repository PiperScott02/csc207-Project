/**package use_case.analysis;

import entity.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BlackLittermanServiceTest {

    private Portfolio portfolio;
    private Stock marketStock;

    @BeforeEach
    void setUp() {
        LocalDate dayMinus4 = LocalDate.now().minusDays(4);
        LocalDate dayMinus3 = LocalDate.now().minusDays(3);
        LocalDate initialDate = LocalDate.now().minusDays(2);
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();

        // SPY Market Data (5 data points for non-singular matrix)
        DailyPriceData spyDay4 = createPriceData(dayMinus4, 98, 101, 97, 99, 800L);
        DailyPriceData spyDay3 = createPriceData(dayMinus3, 99, 102, 98, 100, 850L);
        DailyPriceData spyInitialData = createPriceData(initialDate, 100, 102, 99, 101, 900L);
        DailyPriceData spyPastData = createPriceData(pastDate, 101, 103, 100, 102, 1000L);
        DailyPriceData spyTodayData = createPriceData(today, 102, 104, 101, 103, 1200L);

        marketStock = new Stock();
        marketStock.setTickerSymbol("SPY");
        marketStock.setClose(BigDecimal.valueOf(103.00));
        marketStock.setTimeSeries(Map.of(
                dayMinus4, spyDay4,
                dayMinus3, spyDay3,
                initialDate, spyInitialData,
                pastDate, spyPastData,
                today, spyTodayData
        ));
        marketStock.setHistoricalTimeline(List.of(spyDay4, spyDay3, spyInitialData, spyPastData, spyTodayData));

        // Stock A (AAPL) Data
        DailyPriceData aaplDay4 = createPriceData(dayMinus4, 135, 142, 134, 138, 1800L);
        DailyPriceData aaplDay3 = createPriceData(dayMinus3, 138, 144, 137, 142, 1900L);
        DailyPriceData aaplInitialData = createPriceData(initialDate, 140, 146, 139, 145, 2000L);
        DailyPriceData aaplPastData = createPriceData(pastDate, 145, 146, 138, 140, 2200L);
        DailyPriceData aaplTodayData = createPriceData(today, 140, 150, 139, 148, 2500L);

        Stock stockA = new Stock();
        stockA.setTickerSymbol("AAPL");
        stockA.setClose(BigDecimal.valueOf(148.00));
        stockA.setTimeSeries(Map.of(
                dayMinus4, aaplDay4,
                dayMinus3, aaplDay3,
                initialDate, aaplInitialData,
                pastDate, aaplPastData,
                today, aaplTodayData
        ));
        stockA.setHistoricalTimeline(List.of(aaplDay4, aaplDay3, aaplInitialData, aaplPastData, aaplTodayData));
        stockA.setSharesOutstanding(200.0);

        StockHolding holdingA = new StockHolding();
        holdingA.setStock(stockA);
        holdingA.makeTransaction(stockA, 10.0, initialDate, TransactionType.BUY);

        // Stock B (MSFT) Data
        DailyPriceData msftDay4 = createPriceData(dayMinus4, 315, 318, 308, 310, 1400L);
        DailyPriceData msftDay3 = createPriceData(dayMinus3, 310, 314, 305, 308, 1450L);
        DailyPriceData msftInitialData = createPriceData(initialDate, 310, 312, 300, 302, 1500L);
        DailyPriceData msftPastData = createPriceData(pastDate, 302, 315, 301, 312, 1600L);
        DailyPriceData msftTodayData = createPriceData(today, 312, 314, 304, 306, 1800L);

        Stock stockB = new Stock();
        stockB.setTickerSymbol("MSFT");
        stockB.setClose(BigDecimal.valueOf(306.00));
        stockB.setTimeSeries(Map.of(
                dayMinus4, msftDay4,
                dayMinus3, msftDay3,
                initialDate, msftInitialData,
                pastDate, msftPastData,
                today, msftTodayData
        ));
        stockB.setHistoricalTimeline(List.of(msftDay4, msftDay3, msftInitialData, msftPastData, msftTodayData));
        stockB.setSharesOutstanding(100.0);

        StockHolding holdingB = new StockHolding();
        holdingB.setStock(stockB);
        holdingB.makeTransaction(stockB, 5.0, initialDate, TransactionType.BUY);

        portfolio = new Portfolio(List.of(holdingA, holdingB));
        portfolio.buildMasterTimeline();
    }

    private DailyPriceData createPriceData(LocalDate date, double open, double high, double low, double close, long volume) {
        DailyPriceData data = new DailyPriceData();
        data.setDate(date);
        data.setOpen(BigDecimal.valueOf(open));
        data.setHigh(BigDecimal.valueOf(high));
        data.setLow(BigDecimal.valueOf(low));
        data.setClose(BigDecimal.valueOf(close));
        data.setVolume(volume);
        return data;
    }

    @Test
    void computeMarketWeightCaps() {
        List<StockHolding> holdings = portfolio.getHoldings();
        BlackLittermanService blackLittermanService = new BlackLittermanService();
        Map<String, Double> caps = blackLittermanService.computeMarketWeightCaps(holdings);

        System.out.println(caps.get("AAPL"));
        System.out.println(caps.get("MSFT"));

        assertEquals(2, caps.size());
        assertEquals(0.491694352159, caps.get("AAPL"), 1e-6);
        assertEquals(0.508305647841, caps.get("MSFT"), 1e-6);
    }

    @Test
    void impliedEquilibriumExpectedReturn() {
        List<StockHolding> holdings = portfolio.getHoldings();
        BlackLittermanService blackLittermanService = new BlackLittermanService();

        RealMatrix pi = blackLittermanService.impliedEquilibriumExpectedReturn(holdings);

        // Verify the matrix is not null and has the correct dimensions (rows = number of unique stocks, 1 column)
        assertNotNull(pi);
        assertEquals(2, pi.getRowDimension());
        assertEquals(1, pi.getColumnDimension());

        System.out.println(pi.getEntry(0, 0));
        System.out.println(pi.getEntry(1, 0));

        // Verify that the resulting expected returns are finite numbers
        assertTrue(Double.isFinite(pi.getEntry(0, 0)));
        assertTrue(Double.isFinite(pi.getEntry(1, 0)));
    }

    @Test
    void pickMatrix() {
        List<StockHolding> holdings = portfolio.getHoldings();
        BlackLittermanService blackLittermanService = new BlackLittermanService();

        // Create a view list targeting AAPL
        List<String> viewTickers = List.of("AAPL");
        RealMatrix p = blackLittermanService.pickMatrix(holdings, viewTickers);

        // Verify dimensions: 1 view x 2 stocks in universe
        assertNotNull(p);
        assertEquals(1, p.getRowDimension());
        assertEquals(2, p.getColumnDimension());

        // Verify that the pick entry for AAPL is 1.0 (assuming AAPL is index 0)
        int aaplIndex = new AssetUniverseService(holdings).indexOf("AAPL");
        assertEquals(1.0, p.getEntry(0, aaplIndex), 1e-6);
    }

    @Test
    void omegaMatrix() {
        List<StockHolding> holdings = portfolio.getHoldings();
        BlackLittermanService blackLittermanService = new BlackLittermanService();

        List<String> viewTickers = List.of("AAPL");
        Map<String, String> confidenceLevels = Map.of("AAPL", "Medium");
        RealMatrix covarianceMatrix = blackLittermanService.buildAlignedCovarianceMatrix(holdings);

        RealMatrix omega = blackLittermanService.omegaMatrix(holdings, viewTickers, confidenceLevels, covarianceMatrix);

        // Verify dimensions: 1 view x 1 view diagonal matrix
        assertNotNull(omega);
        assertEquals(1, omega.getRowDimension());
        assertEquals(1, omega.getColumnDimension());

        // Verify formula: (1.0 - confidence) * DEFAULT_TAU * assetVariance
        // Medium confidence = 0.50, DEFAULT_TAU = 0.05
        int aaplIndex = new AssetUniverseService(holdings).indexOf("AAPL");
        double assetVariance = covarianceMatrix.getEntry(aaplIndex, aaplIndex);
        double expectedOmega = (1.0 - 0.50) * 0.05 * assetVariance;

        assertEquals(expectedOmega, omega.getEntry(0, 0), 1e-8);
    }

    @Test
    void computeAdjustedReturns() {
        List<StockHolding> holdings = portfolio.getHoldings();
        BlackLittermanService blackLittermanService = new BlackLittermanService();

        // 1. Test with Active Views and Confidence Levels
        Map<String, Double> userViews = Map.of("AAPL", 15.0);
        Map<String, String> confidenceLevels = Map.of("AAPL", "High");

        Map<String, Double> adjustedReturns = blackLittermanService.computeAdjustedReturns(holdings, userViews, confidenceLevels);

        assertNotNull(adjustedReturns);
        assertTrue(adjustedReturns.containsKey("AAPL"));
        assertTrue(adjustedReturns.containsKey("MSFT"));

        System.out.println("Adjusted Returns Apple: " + adjustedReturns.get("AAPL"));
        System.out.println("Adjusted Returns Microsoft: " + adjustedReturns.get("MSFT"));

        assertEquals(0.158443, adjustedReturns.get("AAPL"), 1e-4);
        assertEquals(-0.054066, adjustedReturns.get("MSFT"), 1e-4);

        // 2. Test Fallback Branch (All views set to "None" or empty confidence)
        Map<String, String> noneConfidence = Map.of("AAPL", "None");
        Map<String, Double> fallbackReturns = blackLittermanService.computeAdjustedReturns(holdings, userViews, noneConfidence);

        assertNotNull(fallbackReturns);

        System.out.println("Fallback Returns Apple: " + fallbackReturns.get("AAPL"));
        System.out.println("Fallback Returns Microsoft: " + fallbackReturns.get("MSFT"));

        assertTrue(fallbackReturns.containsKey("AAPL"));
        assertTrue(fallbackReturns.containsKey("MSFT"));

        assertEquals(0.192838, fallbackReturns.get("AAPL"), 1e-4);
        assertEquals(-0.070356, fallbackReturns.get("MSFT"), 1e-4);    }
}
 **/