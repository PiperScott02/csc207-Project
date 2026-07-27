package entity;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

/** Represents a user's portfolio containing stock holdings, watchlists, timeline details, and financial metrics. */
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


    /** Sets the master timeline for this portfolio.
     * @param timeline the list of LocalDates representing the master timeline to set.
     */
    public void setMasterTimeline(List<LocalDate> timeline) {
        this.masterTimeline = timeline;
    }

    /** Sets the true beta value for this portfolio.
     * @param trueBeta the true beta to set.
     */
    public void setTrueBeta(double trueBeta) {
        this.trueBeta = trueBeta;
    }

    /** Sets the weighted beta value for this portfolio.
     * @param weightedBeta the weighted beta to set.
     */
    public void setWeightedBeta(double weightedBeta) {
        this.weightedBeta = weightedBeta;
    }

    /** Sets the alpha value for this portfolio.
     * @param alpha the alpha to set.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /** Sets the Sharpe ratio for this portfolio.
     * @param sharpeRatio the Sharpe ratio to set.
     */
    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }


    /** Returns the master timeline of this portfolio.
     * @return the list of LocalDates for the master timeline.
     */
    public List<LocalDate> getMasterTimeline() {

        return this.masterTimeline;
    }

    /** Returns the list of stock holdings in this portfolio.
     * @return the list of StockHoldings.
     */
    public List<StockHolding> getHoldings() {
        return this.holdings;
    }

    /** Calculates the total current value of all holdings in the portfolio.
     * @return the total portfolio value as a BigDecimal.
     */
    public BigDecimal calculateTotalPortfolioValue() {
        BigDecimal value = BigDecimal.ZERO;
        for (StockHolding holding: holdings) {
            value = value.add(holding.calculateTotalValue());
        }
        return value;
    }

    /** Calculates the total value of all holdings in the portfolio on a specific date.
     * @param date the date to calculate the portfolio value for.
     * @return the total portfolio value on the given date as a BigDecimal.
     */
    public BigDecimal calculateTotalPortfolioValueOnDate(LocalDate date) {
        BigDecimal value = BigDecimal.ZERO;
        for (StockHolding holding: holdings) {
            value = value.add(holding.calculateTotalValueOnDate(date));
        }
        return value;
    }

    /** Returns a list of all stocks contained within the portfolio's holdings.
     * @return the list of Stock entities.
     */
    public List<Stock> getStocks() {
        List<Stock> stockList = new ArrayList<Stock>();
        List<StockHolding> stockHoldings = this.getHoldings();
        for (int i = 0; i < stockHoldings.size(); i++) {
            stockList.add(stockHoldings.get(i).getStock());
        }
        return stockList;
    }

    /** Returns a map pairing each stock in the portfolio with its historical timeline data.
     * @return a map of Stock to its list of DailyPriceData.
     */
    public Map<Stock, List<DailyPriceData>> getStocksAndTimelines() {
        Map<Stock, List<DailyPriceData>> stocksAndTimelines = new HashMap<>();
        for (Stock stock : getStocks()) {
            stocksAndTimelines.put(stock, stock.getHistoricalTimeline());
        }
        return stocksAndTimelines;
    }


    /** Returns a map pairing each stock holding with its historical timeline data.
     * @return a map of StockHolding to its list of DailyPriceData.
     */
    private Map<StockHolding, List<DailyPriceData>> getHoldingAndTimelines() {
        Map<StockHolding, List<DailyPriceData>> stocksAndTimelines = new HashMap<>();
        for (StockHolding holding : this.holdings) {
            stocksAndTimelines.put(holding, holding.getStock().getHistoricalTimeline());
        }
        return stocksAndTimelines;
    }


    /** Calculates the proportion of the portfolio value represented by a specific holding.
     * @param holding the StockHolding to check.
     * @return the share of the portfolio as a double.
     */
    public double getHoldingShare(StockHolding holding) {
        BigDecimal holdingPrice = holding.calculateTotalValue();
        BigDecimal portfolioValue = this.calculateTotalPortfolioValue();
        if (portfolioValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return holdingPrice.divide(portfolioValue, 12, java.math.RoundingMode.HALF_UP).doubleValue();

    }

    /** Calculates the proportion of the portfolio value represented by a specific holding on a given date.
     * @param holding the StockHolding to check.
     * @param date the date for the calculation.
     * @return the share of the portfolio on that date as a double.
     */
    public double calculateHoldingShareOnDay(StockHolding holding, LocalDate date) {
        BigDecimal holdingPriceOnDate = holding.calculateTotalValueOnDate(date);
        BigDecimal portfolioValueOnDate = this.calculateTotalPortfolioValueOnDate(date);
        if (portfolioValueOnDate.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return holdingPriceOnDate.divide(portfolioValueOnDate, 12, java.math.RoundingMode.HALF_UP).doubleValue();
    }

    /** Calculates the daily return of the portfolio on a specific date.
     * @param date the date to calculate the return for.
     * @return the portfolio daily return as a double.
     */
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

    /** Retrieves a specific stock holding from the portfolio using its ticker symbol.
     * @param ticker the ticker symbol to search for.
     * @return the matching StockHolding, or null if not found.
     */
    public StockHolding getHoldingByTicker(String ticker) {
        for (StockHolding holding: this.holdings) {
            if (holding.getStock().getTickerSymbol().equals(ticker)) {
                return holding;
            }
        }
        return null;
    }


}