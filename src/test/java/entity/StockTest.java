package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    private Stock stock;
    private LocalDate day1;
    private LocalDate day2;
    private LocalDate day3;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        day1 = LocalDate.of(2026, 8, 1);
        day2 = LocalDate.of(2026, 8, 2);
        day3 = LocalDate.of(2026, 8, 3);
    }

    // ==========================================
    // 1. DATE & TIMELINE LOOKUPS
    // ==========================================

    @Test
    @DisplayName("getCloseOnDate and getOpenOnDate return correct values when date exists")
    void testGetPricesOnDateSuccess() {
        DailyPriceData data1 = new DailyPriceData();
        data1.setDate(day1);
        data1.setOpen(new BigDecimal("100.00"));
        data1.setClose(new BigDecimal("105.00"));

        stock.setHistoricalTimeline(List.of(data1));

        assertEquals(new BigDecimal("100.00"), stock.getOpenOnDate(day1));
        assertEquals(new BigDecimal("105.00"), stock.getCloseOnDate(day1));
    }

    @Test
    @DisplayName("getCloseOnDate returns null when date is not found in timeline")
    void testGetCloseOnDateNotFound() {
        stock.setHistoricalTimeline(Collections.emptyList());
        assertNull(stock.getCloseOnDate(day1));
    }

    // ==========================================
    // 2. TRADING DAY CALCULATIONS
    // ==========================================

    @Test
    @DisplayName("getDatesSorted returns dates in ascending order")
    void testGetDatesSorted() {
        Map<LocalDate, DailyPriceData> timeSeries = new HashMap<>();
        timeSeries.put(day3, new DailyPriceData());
        timeSeries.put(day1, new DailyPriceData());
        timeSeries.put(day2, new DailyPriceData());

        stock.setTimeSeries(timeSeries);

        List<LocalDate> sorted = stock.getDatesSorted();
        assertEquals(List.of(day1, day2, day3), sorted);
    }

    @Test
    @DisplayName("getPreviousTradingDay returns correct prior date or null if first/missing")
    void testGetPreviousTradingDay() {
        Map<LocalDate, DailyPriceData> timeSeries = new HashMap<>();
        timeSeries.put(day1, new DailyPriceData());
        timeSeries.put(day2, new DailyPriceData());
        timeSeries.put(day3, new DailyPriceData());
        stock.setTimeSeries(timeSeries);

        // Day 2's previous day should be Day 1
        assertEquals(day1, stock.getPreviousTradingDay(day2));

        // Day 1 is the first day, so previous day should be null
        assertNull(stock.getPreviousTradingDay(day1));

        // Unknown date (not in series) should return null
        LocalDate unknownDate = LocalDate.of(2026, 12, 25);
        assertNull(stock.getPreviousTradingDay(unknownDate));
    }

    @Test
    @DisplayName("getLastTradingDay returns the most recent date")
    void testGetLastTradingDay() {
        Map<LocalDate, DailyPriceData> timeSeries = new HashMap<>();
        timeSeries.put(day1, new DailyPriceData());
        timeSeries.put(day3, new DailyPriceData());
        stock.setTimeSeries(timeSeries);

        assertEquals(day3, stock.getLastTradingDay());
    }

    // ==========================================
    // 3. DAILY PRICE CHANGE
    // ==========================================

    @Test
    @DisplayName("getDailyChangeOnDate calculates difference between today and yesterday close")
    void testGetDailyChangeOnDateSuccess() {
        DailyPriceData data1 = new DailyPriceData();
        data1.setDate(day1);
        data1.setClose(new BigDecimal("100.00"));

        DailyPriceData data2 = new DailyPriceData();
        data2.setDate(day2);
        data2.setClose(new BigDecimal("110.00"));

        stock.setHistoricalTimeline(List.of(data1, data2));

        Map<LocalDate, DailyPriceData> timeSeries = new HashMap<>();
        timeSeries.put(day1, data1);
        timeSeries.put(day2, data2);
        stock.setTimeSeries(timeSeries);

        // Change on Day 2: 110.00 - 100.00 = 10.00
        BigDecimal change = stock.getDailyChangeOnDate(day2);
        assertNotNull(change);
        assertEquals(new BigDecimal("10.00"), change);
    }

    @Test
    @DisplayName("getDailyChangeOnDate returns null if day is the first trading day")
    void testGetDailyChangeOnDateFirstDayReturnsNull() {
        DailyPriceData data1 = new DailyPriceData();
        data1.setDate(day1);
        data1.setClose(new BigDecimal("100.00"));

        stock.setHistoricalTimeline(List.of(data1));
        stock.setTimeSeries(Map.of(day1, data1));

        assertNull(stock.getDailyChangeOnDate(day1));
    }

    // ==========================================
    // 4. METRICS CHECK
    // ==========================================

    @Test
    @DisplayName("hasCalculatedMetrics returns true only when beta, alpha, and sharpeRatio are all non-null")
    void testHasCalculatedMetrics() {
        assertFalse(stock.hasCalculatedMetrics(), "Should be false when all are null");

        stock.setBeta(1.2);
        assertFalse(stock.hasCalculatedMetrics(), "Should be false when only beta is set");

        stock.setAlpha(0.05);
        assertFalse(stock.hasCalculatedMetrics(), "Should be false when missing sharpeRatio");

        stock.setSharpeRatio(1.8);
        assertTrue(stock.hasCalculatedMetrics(), "Should be true when all three are set");
    }
}