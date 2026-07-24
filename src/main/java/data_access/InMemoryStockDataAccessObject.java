package data_access;

import entity.DailyPriceData;
import entity.Stock;
import use_case.stock.StockDataAccessInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InMemoryStockDataAccessObject implements StockDataAccessInterface {

    private final Stock appleStock = new Stock();
    private final Stock spyStock = new Stock();

    public InMemoryStockDataAccessObject() {
        // Setup Apple Mock Stock
        appleStock.setTickerSymbol("AAPL");
        appleStock.setCompanyName("Apple Inc.");
        appleStock.setClose(new BigDecimal("150.00"));
        appleStock.setPreviousClose(new BigDecimal("147.50"));
        appleStock.setDailyChange(new BigDecimal("2.50"));

        // Setup SPY Market Mock Stock
        spyStock.setTickerSymbol("SPY");
        spyStock.setCompanyName("SPDR S&P 500 ETF Trust");
        spyStock.setClose(new BigDecimal("400.00"));
        spyStock.setPreviousClose(new BigDecimal("398.00"));
        spyStock.setDailyChange(new BigDecimal("2.00"));

        LocalDate today = LocalDate.now();
        Map<LocalDate, DailyPriceData> appleTimeSeries = new HashMap<>();
        List<DailyPriceData> appleTimeline = new ArrayList<>();

        Map<LocalDate, DailyPriceData> spyTimeSeries = new HashMap<>();
        List<DailyPriceData> spyTimeline = new ArrayList<>();

        // Generate 30 days of historical data with minor variations to allow variance/covariance calculations
        double currentApplePrice = 140.00;
        double currentSpyPrice = 390.00;
        Random rand = new Random(42); // Seeded for consistency

        for (int i = 30; i >= 0; i--) {
            LocalDate date = today.minusDays(i);

            currentApplePrice += (rand.nextDouble() - 0.48) * 2.0;
            currentSpyPrice += (rand.nextDouble() - 0.49) * 1.5;

            DailyPriceData appleDay = new DailyPriceData();
            appleDay.setDate(date);
            appleDay.setClose(BigDecimal.valueOf(Math.round(currentApplePrice * 100.0) / 100.0));
            appleTimeSeries.put(date, appleDay);
            appleTimeline.add(appleDay);

            DailyPriceData spyDay = new DailyPriceData();
            spyDay.setDate(date);
            spyDay.setClose(BigDecimal.valueOf(Math.round(currentSpyPrice * 100.0) / 100.0));
            spyTimeSeries.put(date, spyDay);
            spyTimeline.add(spyDay);
        }

        // Set final close to match the last generated day
        appleStock.setClose(appleTimeline.get(appleTimeline.size() - 1).getClose());
        appleStock.setPreviousClose(appleTimeline.get(appleTimeline.size() - 2).getClose());
        appleStock.setTimeSeries(appleTimeSeries);
        appleStock.setHistoricalTimeline(appleTimeline);

        spyStock.setClose(spyTimeline.get(spyTimeline.size() - 1).getClose());
        spyStock.setPreviousClose(spyTimeline.get(spyTimeline.size() - 2).getClose());
        spyStock.setTimeSeries(spyTimeSeries);
        spyStock.setHistoricalTimeline(spyTimeline);
    }

    @Override
    public boolean existsByName(String string) {
        return string != null && (string.equalsIgnoreCase("AAPL") || string.equalsIgnoreCase("SPY"));
    }

    @Override
    public Stock get(String string) {
        if (string != null && string.equalsIgnoreCase("SPY")) {
            return this.spyStock;
        }
        return this.appleStock;
    }
}