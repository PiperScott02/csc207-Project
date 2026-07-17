import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public class Portfolio {
    private String portfolioName;

    private String userId;

    private List<StockHolding> holdings;

    private List<StockHolding> watchlisted;

    private List<LocalDate> masterTimeline;

    private double portfolioHealth;

    private double weightedBeta;

    private double trueBeta;

    private double alpha;

    private double sharpeRatio;


    public void setMasterTimeline(List<LocalDate> timeline) {
        this.masterTimeline = timeline;
    }

    public void setTrueBeta(double trueBeta) {
        this.trueBeta = trueBeta;
    }

    public void setWeightedBeta(double weightedBeta) {
        this.weightedBeta = weightedBeta;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }


    public List<LocalDate> getMasterTimeline() {

        return this.masterTimeline;
    }

    public List<StockHolding> getHoldings() {
        return this.holdings;
    }

    public BigDecimal calculateTotalPortfolioValue() {
        BigDecimal value = BigDecimal.ZERO;
        for (StockHolding holding: holdings) {
            value = value.add(holding.calculateTotalValue());
        }
        return value;
    }

    public BigDecimal calculateTotalPortfolioValueOnDate(LocalDate date) {
        BigDecimal value = BigDecimal.ZERO;
        for (StockHolding holding: holdings) {
            value = value.add(holding.calculateTotalValueOnDate(date));
        }
        return value;
    }

    public List<Stock> getStocks() {
        List<Stock> stockList = new ArrayList<Stock>();
        List<StockHolding> stockHoldings = this.getHoldings();
        for (int i = 0; i < stockHoldings.size(); i++) {
            stockList.add(stockHoldings.get(i).getStock());
        }
        return stockList;
    }

    public Map<Stock, List<DailyPriceData>> getStocksAndTimelines() {
        Map<Stock, List<DailyPriceData>> stocksAndTimelines = new HashMap<>();
        for (Stock stock : getStocks()) {
            stocksAndTimelines.put(stock, stock.getHistoricalTimeline());
        }
        return stocksAndTimelines;
    }


    private Map<StockHolding, List<DailyPriceData>> getHoldingAndTimelines() {
        Map<StockHolding, List<DailyPriceData>> stocksAndTimelines = new HashMap<>();
        for (StockHolding holding : this.holdings) {
            stocksAndTimelines.put(holding, holding.getStock().getHistoricalTimeline());
        }
        return stocksAndTimelines;
    }


    public double getHoldingShare(StockHolding holding) {
        BigDecimal holdingPrice = holding.calculateTotalValue();
        BigDecimal portfolioValue = this.calculateTotalPortfolioValue();
        if (portfolioValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return holdingPrice.divide(portfolioValue, 12, java.math.RoundingMode.HALF_UP).doubleValue();

    }

    public double calculateHoldingShareOnDay(StockHolding holding, LocalDate date) {
        BigDecimal holdingPriceOnDate = holding.calculateTotalValueOnDate(date);
        BigDecimal portfolioValueOnDate = this.calculateTotalPortfolioValueOnDate(date);
        if (portfolioValueOnDate.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return holdingPriceOnDate.divide(portfolioValueOnDate, 12, java.math.RoundingMode.HALF_UP).doubleValue();
    }

    public double calculatePortfolioDailyReturn (LocalDate date) {
        BigDecimal portfolioValueToday = calculateTotalPortfolioValueOnDate(date);
        int dateIndex = this.masterTimeline.indexOf(date);
        if (dateIndex <= 0) {
            return 0.0;
        }
        LocalDate dateBefore = this.masterTimeline.get(dateIndex - 1);
        BigDecimal portfolioValueYesterday = calculateTotalPortfolioValueOnDate(dateBefore);
        BigDecimal dailyChange = portfolioValueToday.subtract(portfolioValueYesterday);
        return dailyChange.divide(portfolioValueYesterday, 12, RoundingMode.HALF_UP).doubleValue();
    }

    public StockHolding getHoldingByTicker(String ticker) {
        for (StockHolding holding: this.holdings) {
            if (holding.getStock().getTickerSymbol().equals(ticker)) {
                return holding;
            }
        }
        return null;
    }


    }


