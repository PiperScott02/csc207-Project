package entity;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;

/** Represents a user's portfolio containing stock holdings, watchlists, timeline details, and financial metrics. */
public class Portfolio {
    private String portfolioName;

    private String userId;

    private List<StockHolding> holdings = new ArrayList<>();

    private List<WatchlistStockItem> watchlist = new ArrayList<>();

    private List<LocalDate> masterTimeline;

    private double portfolioHealth;

    private double weightedBeta;

    private double trueBeta;

    private double alpha;

    private double sharpeRatio;

    private double annualizedSharpeRatio;

    private double annualizedAlpha;

    private boolean hasCustomViews;

    private Map<String, Double> customViews;

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

    /** Returns the true beta value of this portfolio.
     * @return the true beta as a double.
     */
    public Double getTrueBeta() {
        return this.trueBeta;
    }

    /** Returns the weighted beta value of this portfolio.
     * @return the weighted beta as a double.
     */
    public Double getWeightedBeta() {
        return this.weightedBeta;
    }

    /** Returns the alpha value of this portfolio.
     * @return alpha as a double.
     */
    public Double getAlpha() {
        return this.alpha;
    }

    /** Returns the Sharpe ratio of this portfolio.
     * @return the Sharpe ratio as a double.
     */
    public Double getSharpeRatio() {
        return this.sharpeRatio;
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

    /** Builds the portfolio's master timeline.*/
    public void buildMasterTimeline() {


        Set<LocalDate> dates = new TreeSet<>();

        for (StockHolding holding : holdings) {
            dates.addAll(
                    holding.getStock().getDatesSorted()
            );
        }

        masterTimeline = new ArrayList<>(dates);
        Collections.sort(masterTimeline);
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

        for (StockHolding holding : holdings) {
            BigDecimal holdingValue = holding.calculateTotalValueOnDate(date);

            if (holdingValue != null) {
                value = value.add(holdingValue);
            }
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
    public Double getHoldingShare(StockHolding holding) {
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
    public Double calculateHoldingShareOnDay(
            StockHolding holding,
            LocalDate date) {
        BigDecimal holdingPriceOnDate =
                holding.calculateTotalValueOnDate(date);
        BigDecimal portfolioValueOnDate =
                this.calculateTotalPortfolioValueOnDate(date);
        if (holdingPriceOnDate == null ||
                portfolioValueOnDate.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return holdingPriceOnDate
                .divide(portfolioValueOnDate, 12, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /** Calculates the daily return of the portfolio on a specific date.
     * @param date the date to calculate the return for.
     * @return the portfolio daily return as a double.
     */
    public Double calculatePortfolioDailyReturn(LocalDate date) {

        int dateIndex = this.masterTimeline.indexOf(date);

        if (dateIndex <= 0) {
            return null;
        }

        LocalDate dateBefore = this.masterTimeline.get(dateIndex - 1);

        BigDecimal portfolioValueToday =
                calculateTotalPortfolioValueOnDate(date);

        BigDecimal portfolioValueYesterday =
                calculateTotalPortfolioValueOnDate(dateBefore);

        if (portfolioValueYesterday.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        BigDecimal dailyChange =
                portfolioValueToday.subtract(portfolioValueYesterday);

        return dailyChange.divide(
                portfolioValueYesterday,
                12,
                RoundingMode.HALF_UP
        ).doubleValue();
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

    /** Adds a stock holding to the portfolio.
     * @param holding the StockHolding to add.
     */
    public void addHolding(StockHolding holding) {
        this.holdings.add(holding);
    }

    /**
     * Constructs a Portfolio initialized with an empty collection of stock holdings.
     */

    public Portfolio() {
        this.holdings = new ArrayList<>();
    }

    /**
     * Constructs a Portfolio initialized with a collection of stock holdings.
     * @param holdings the list of stock holdings to initialize the portfolio with
     */

    public Portfolio(List<StockHolding> holdings) {
        this.holdings = new ArrayList<>(holdings);
    }

    /** Removes a stock holding from the portfolio.
     * @param holding the StockHolding to remove.
     */
    public void removeHolding(StockHolding holding) {
        this.holdings.remove(holding);
    }

    /** Removes a stock holding from the portfolio by its ticker symbol.
     * @param ticker the ticker symbol of the holding to remove.
     * @return true if a holding was removed, false otherwise.
     */
    public boolean removeHoldingByTicker(String ticker) {
        StockHolding holding = getHoldingByTicker(ticker);
        if (holding != null) {
            return this.holdings.remove(holding);
        }
        return false;
    }

    /** Removes a stock item from the watchlist by its ticker symbol.
     * @param ticker the ticker symbol of the watchlist item to remove.
     * @return true if an item was removed, false otherwise.
     */
    public boolean removeWatchlistByTicker(String ticker) {
        return watchlist.removeIf(item -> item.ticker().equals(ticker));
    }

    /** Returns whether the user has set custom views regarding their stocks' perfomances.
     * @return a boolean representing whether they have set custom views or not.
     */
    public boolean hasCustomViews() {
        return hasCustomViews;
    }

    /** Sets whether the user has set custom views regarding their stocks' perfomances.
     * @param hasCustomViews a boolean representing whether they have set custom views or not.
     */
    public void setHasCustomViews(boolean hasCustomViews) {
        this.hasCustomViews = hasCustomViews;
    }

    /** Returns the users' custom views on the perfomances of stock.
     * @return a map mapping the ticker symbol of the stock to the adjusted custom expected return.
     */
    public Map<String, Double> getCustomViews() {
        return customViews;
    }

    /** Sets the users' custom views on the perfomances of stock.
     * @param customViews a map mapping the ticker symbol of the stock to the adjusted custom expected return.
     */
    public void setCustomViews(Map<String, Double> customViews) {
        this.customViews = customViews;
    }

    /** Returns the users' watchlist.
     * @return a list of the stocks the user has waitlisted.
     */
    public List<WatchlistStockItem> getWatchlist() {
        return watchlist;
    }

    /** Sets the users' watchlist.
     * @param watchlist  a list of the stocks the user has waitlisted.
     */
    public void setWatchlist(List<WatchlistStockItem> watchlist) {
        this.watchlist = watchlist;
    }

    /** Adds a watchlistItem to the users' watchlist.
     * @param watchlistItem to be added to the watchlist. .
     */
    public void addWatchlist(WatchlistStockItem watchlistItem) {
        this.watchlist.add(watchlistItem);
    }


    /** Sets the annualized Sharpe ratio for this portfolio.
     * @param annualizedSharpeRatio the annualized Sharpe ratio to set.
     */
    public void setAnnualizedSharpeRatio(double annualizedSharpeRatio) {
        this.annualizedSharpeRatio = annualizedSharpeRatio;
    }

    /** Sets the annualized alpha value for this portfolio.
     * @param annualizedAlpha the annualized alpha to set.
     */
    public void setAnnualizedAlpha(double annualizedAlpha) {
        this.annualizedAlpha = annualizedAlpha;
    }

    /** Returns the annualized Sharpe ratio of this portfolio.
     * @return the annualized Sharpe ratio as a double.
     */
    public Double getAnnualizedSharpeRatio() {
        return this.annualizedSharpeRatio;
    }

    /** Returns the annualized alpha value of this portfolio.
     * @return the annualized alpha as a double.
     */
    public Double getAnnualizedAlpha() {
        return this.annualizedAlpha;
    }
}

