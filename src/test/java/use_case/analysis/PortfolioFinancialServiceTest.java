/**package use_case.analysis;

import entity.DailyPriceData;
import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import entity.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioFinancialServiceTest {

    private Portfolio portfolio;
    private Stock marketStock;

    @BeforeEach
    void setUp() {
        LocalDate initialDate = LocalDate.now().minusDays(2);
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();

        // SPY Market Data
        DailyPriceData spyInitialData = new DailyPriceData();
        spyInitialData.setDate(initialDate);
        spyInitialData.setOpen(BigDecimal.valueOf(100));
        spyInitialData.setHigh(BigDecimal.valueOf(102));
        spyInitialData.setLow(BigDecimal.valueOf(99));
        spyInitialData.setClose(BigDecimal.valueOf(101));
        spyInitialData.setVolume(900L);

        DailyPriceData spyPastData = new DailyPriceData();
        spyPastData.setDate(pastDate);
        spyPastData.setOpen(BigDecimal.valueOf(101));
        spyPastData.setHigh(BigDecimal.valueOf(103));
        spyPastData.setLow(BigDecimal.valueOf(100));
        spyPastData.setClose(BigDecimal.valueOf(102));
        spyPastData.setVolume(1000L);

        DailyPriceData spyTodayData = new DailyPriceData();
        spyTodayData.setDate(today);
        spyTodayData.setOpen(BigDecimal.valueOf(102));
        spyTodayData.setHigh(BigDecimal.valueOf(104));
        spyTodayData.setLow(BigDecimal.valueOf(101));
        spyTodayData.setClose(BigDecimal.valueOf(103));
        spyTodayData.setVolume(1200L);

        marketStock = new Stock();
        marketStock.setTickerSymbol("SPY");
        marketStock.setClose(BigDecimal.valueOf(103.00));
        marketStock.setTimeSeries(Map.of(initialDate, spyInitialData, pastDate, spyPastData, today, spyTodayData));
        marketStock.setHistoricalTimeline(List.of(spyInitialData, spyPastData, spyTodayData));

        // Stock A (AAPL) Data - Up then Down (Volatility/Sign Reversal)
        DailyPriceData aaplInitialData = new DailyPriceData();
        aaplInitialData.setDate(initialDate);
        aaplInitialData.setOpen(BigDecimal.valueOf(140));
        aaplInitialData.setHigh(BigDecimal.valueOf(146));
        aaplInitialData.setLow(BigDecimal.valueOf(139));
        aaplInitialData.setClose(BigDecimal.valueOf(145));
        aaplInitialData.setVolume(2000L);

        DailyPriceData aaplPastData = new DailyPriceData();
        aaplPastData.setDate(pastDate);
        aaplPastData.setOpen(BigDecimal.valueOf(145));
        aaplPastData.setHigh(BigDecimal.valueOf(146));
        aaplPastData.setLow(BigDecimal.valueOf(138));
        aaplPastData.setClose(BigDecimal.valueOf(140));
        aaplPastData.setVolume(2200L);

        DailyPriceData aaplTodayData = new DailyPriceData();
        aaplTodayData.setDate(today);
        aaplTodayData.setOpen(BigDecimal.valueOf(140));
        aaplTodayData.setHigh(BigDecimal.valueOf(150));
        aaplTodayData.setLow(BigDecimal.valueOf(139));
        aaplTodayData.setClose(BigDecimal.valueOf(148));
        aaplTodayData.setVolume(2500L);

        Stock stockA = new Stock();
        stockA.setTickerSymbol("AAPL");
        stockA.setClose(BigDecimal.valueOf(148.00));
        stockA.setTimeSeries(Map.of(initialDate, aaplInitialData, pastDate, aaplPastData, today, aaplTodayData));
        stockA.setHistoricalTimeline(List.of(aaplInitialData, aaplPastData, aaplTodayData));

        StockHolding holdingA = new StockHolding();
        holdingA.setStock(stockA);
        holdingA.makeTransaction(stockA, 10.0, initialDate, TransactionType.BUY);

        // Stock B (MSFT) Data - Down then Up (Divergent/Uncorrelated pattern)
        DailyPriceData msftInitialData = new DailyPriceData();
        msftInitialData.setDate(initialDate);
        msftInitialData.setOpen(BigDecimal.valueOf(310));
        msftInitialData.setHigh(BigDecimal.valueOf(312));
        msftInitialData.setLow(BigDecimal.valueOf(300));
        msftInitialData.setClose(BigDecimal.valueOf(302));
        msftInitialData.setVolume(1500L);

        DailyPriceData msftPastData = new DailyPriceData();
        msftPastData.setDate(pastDate);
        msftPastData.setOpen(BigDecimal.valueOf(302));
        msftPastData.setHigh(BigDecimal.valueOf(315));
        msftPastData.setLow(BigDecimal.valueOf(301));
        msftPastData.setClose(BigDecimal.valueOf(312));
        msftPastData.setVolume(1600L);

        DailyPriceData msftTodayData = new DailyPriceData();
        msftTodayData.setDate(today);
        msftTodayData.setOpen(BigDecimal.valueOf(312));
        msftTodayData.setHigh(BigDecimal.valueOf(314));
        msftTodayData.setLow(BigDecimal.valueOf(304));
        msftTodayData.setClose(BigDecimal.valueOf(306));
        msftTodayData.setVolume(1800L);

        Stock stockB = new Stock();
        stockB.setTickerSymbol("MSFT");
        stockB.setClose(BigDecimal.valueOf(306.00));
        stockB.setTimeSeries(Map.of(initialDate, msftInitialData, pastDate, msftPastData, today, msftTodayData));
        stockB.setHistoricalTimeline(List.of(msftInitialData, msftPastData, msftTodayData));

        StockHolding holdingB = new StockHolding();
        holdingB.setStock(stockB);
        holdingB.makeTransaction(stockB, 5.0, initialDate, TransactionType.BUY);

        portfolio = new Portfolio(List.of(holdingA, holdingB));
        portfolio.buildMasterTimeline();
    }

    @Test
    void calculateAndAssignMetrics() {
        assertDoesNotThrow(() -> PortfolioFinancialService.calculateAndAssignMetrics(portfolio, marketStock));
        assertNotNull(portfolio.getTrueBeta());
        assertNotNull(portfolio.getWeightedBeta());
        assertNotNull(portfolio.getAlpha());
        assertNotNull(portfolio.getSharpeRatio());
    }

    @Test
    void calculateCustomPortfolioReturnsNumber() {
        Map<String, Double> customViews = Map.of("AAPL", 0.15, "MSFT", 0.12);
        double returns = PortfolioFinancialService.calculateCustomPortfolioReturnsNumber(portfolio, customViews);
        System.out.println(returns);
        assertEquals(0.1347, returns, 0.0001);

        double fallbackReturns = PortfolioFinancialService.calculateCustomPortfolioReturnsNumber(portfolio, Map.of("GOOG", 0.10));
        System.out.println(fallbackReturns);
        assertEquals(0.0091, fallbackReturns, 0.0001);
    }

    @Test
    void calculateSharpeRatio() {
        double sharpeRatio = PortfolioFinancialService.calculateSharpeRatio(portfolio);
        System.out.println("Sharpe Ratio: " + sharpeRatio);
        assertEquals(0.68630669, sharpeRatio, 0.000001);
    }

    @Test
    void calculateCdr() {
        double cdr = PortfolioFinancialService.calculateCdr(portfolio);
        System.out.println("CDR: " + cdr);
        assertEquals(3.8848879, cdr, 0.0001);
    }
}
 **/