package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

/** Represents a Stock entity containing historical prices, financial metrics and a time series object.*/


public class Stock {
    private String tickerSymbol;

    private String companyName;

    private BigDecimal close;

    private BigDecimal previousClose;

    private Double sharesOutstanding;

    private String country;

    private String currency;

    private String industry;

    private BigDecimal dailyChange;

    private BigDecimal dividendYield;

    private Double sharpeRatio;

    private Double beta;

    private Double alpha;

    private List<DailyPriceData> historicalTimeline;

    private Map<LocalDate, DailyPriceData> timeSeries;


    /** Sets the ticker symbol for this stock.
     * @param tickerSymbol the ticker symbol to set.
     */
    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    /** Returns the list of daily historical price data.
     * @return historical price data timeline list.
     */

    /** Gets the country for this stock.
     * @return the country this stock is based at.
     */
    public String getCountry() {
        return country;
    }

    /** Sets the country for this stock.
     * @param country the country this is based at.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /** Gets the currency for this stock.
     * @return the string for the currency this stock uses.
     */
    public String getCurrency() {
        return currency;
    }

    /** Sets the currency for this stock.
     * @return the string for the currency this stock uses.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public List<DailyPriceData> getHistoricalTimeline() {
        return historicalTimeline;
    }

    /** Returns the map of Local Date and their respective Daily Price Data.
     * @return timeSeries map of Local Date and Daily Price Data.
     */
    public Map<LocalDate, DailyPriceData> getTimeSeries() {
        return timeSeries;
    }

    /** Sets the list of daily historical price data.
     * @param historicalTimeline the historical price data timeline to be set.
     */
    public void setHistoricalTimeline(List<DailyPriceData> historicalTimeline) {
        this.historicalTimeline = historicalTimeline;
    }

    /** Returns the shares outstanding of the stock.
     * @return a double of the number of shares.
     */
    public Double getSharesOutstanding() {
        return sharesOutstanding;
    }

    /** Sets the shares outstanding of the stock.
     * @param sharesOutstanding a double of the number of shares outstanding.
     */
    public void setSharesOutstanding(Double sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    /** Returns the ticker symbol of this stock.
     * @return the ticker symbol.
     */
    public String getTickerSymbol() {
        return tickerSymbol;
    }

    /** Returns the company name of this stock.
     * @return the company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /** Sets the company name of this stock.
     * @param companyName the name of the company.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /** Returns the closing price of this stock.
     * @return the close price.
     */
    public BigDecimal getClose() {
        return close;
    }

    /** Returns the opening price on a specific date.
     * @param date the date to look up.
     * @return the opening price on the given date, or null if not found.
     */
    public BigDecimal getOpenOnDate(LocalDate date) {
        for (DailyPriceData data: historicalTimeline) {
            if (data.getDate().equals(date)) {
                return data.getOpen();
            }
        }
        return null;
    }

    /** Returns the previous close price of this stock.
     * @return the previous close price.
     */
    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    /** Returns the closing price on a specific date.
     * @param date the date to look up.
     * @return the closing price on the given date, or null if not found.
     */
    public BigDecimal getCloseOnDate(LocalDate date) {
        for (DailyPriceData data: historicalTimeline) {
            if (data.getDate().equals(date)) {
                return data.getClose();
            }
        }
        return null;
    }

    /** Returns the closing price on the closest available trading day to a given date,
     * searching through all history if necessary to handle random or unlisted dates (non-trading days or perhaps
     * the company had not been publicly traded yet at that specified time, in this case, the closest closing price
     * is returned which would be on the day of the company's IPO).
     * @param targetDate the target date to look up.
     * @return the closing price on the closest trading day, or null if time series is empty.
     */
    public BigDecimal getClosestPrice(LocalDate targetDate) {
        if (timeSeries == null || timeSeries.isEmpty()) {
            return null;
        }

        // If exact date exists, return it immediately
        if (timeSeries.containsKey(targetDate)) {
            return timeSeries.get(targetDate).getClose();
        }

        LocalDate closestDate = null;
        long minDaysDifference = Long.MAX_VALUE;

        // Scan through all available dates in the time series to find the closest one
        for (LocalDate date : timeSeries.keySet()) {
            long diff = Math.abs(java.time.temporal.ChronoUnit.DAYS.between(date, targetDate));
            if (diff < minDaysDifference) {
                minDaysDifference = diff;
                closestDate = date;
            }
        }

        if (closestDate != null) {
            return timeSeries.get(closestDate).getClose();
        }

        return null;
    }

    /** Returns the daily change in price.
     * @return the daily change.
     */
    public BigDecimal getDailyPriceChange() {
        return dailyChange;
    }

    /** Calculates the daily price change on a specific date.
     * @param date the date to calculate change for.
     * @return the price difference between the date and the previous trading day, or null if data is missing.
     */
    public BigDecimal getDailyChangeOnDate(LocalDate date) {
        BigDecimal closeToday = getCloseOnDate(date);
        BigDecimal closeYesterday = getCloseOnDate(this.getPreviousTradingDay(date));
        if (closeToday != null && closeYesterday != null ) {
            return closeToday.subtract(closeYesterday);
        }
        return null;
    }

    /** Finds the previous trading day relative to a given date.
     * @param date the reference date.
     * @return the previous trading day's LocalDate, or null if none exists.
     */
    public LocalDate getPreviousTradingDay(LocalDate date) {
        List<LocalDate> dates = getDatesSorted();
        int dateIndex = dates.indexOf(date);
        if (dateIndex <= 0 ) {
            return null;
        }
        return dates.get(dateIndex - 1);

    }

    /** Returns the last Trading Day for this stock.
     * @return the last entry in the getDatesSorted list.
     */
    public LocalDate getLastTradingDay() {
        List<LocalDate> dates = getDatesSorted();
        if (dates.isEmpty()) {
            return null;
        }
        int listLength = dates.size();
        return dates.get(listLength - 1);
    }

    /** Returns the dividend yield of this stock.
     * @return the dividend yield.
     */
    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    /** Returns the Sharpe ratio of this stock.
     * @return the Sharpe ratio.
     */
    public Double getSharpeRatio() {
        return sharpeRatio;
    }

    /** Returns daily price data for a specific date from the time series.
     * @param date the date to look up.
     * @return the DailyPriceData object for that date.
     */
    public DailyPriceData getDailyPriceDataOnDate(LocalDate date) {
        return this.timeSeries.get(date);
    }

    /** Returns a sorted list of all dates available in the time series.
     * @return a sorted list of LocalDates.
     */
    public List<LocalDate> getDatesSorted() {
        if (this.timeSeries == null) {
            return Collections.emptyList();
        }
        List<LocalDate> dates = new ArrayList<>(this.timeSeries.keySet());
        Collections.sort(dates);
        return dates;
    }

    /** Returns the beta value of this stock.
     * @return the beta value.
     */
    public Double getBeta() {
        return beta;
    }

    /** Returns the alpha value of this stock.
     * @return the alpha value.
     */
    public Double getAlpha() {
        return alpha;
    }

    /** Sets the beta value for this stock.
     * @param beta the beta value to set.
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }

    /** Sets the alpha value for this stock.
     * @param alpha the alpha value to set.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /** Sets the Sharpe ratio for this stock.
     * @param sharpeRatio the Sharpe ratio to set.
     */
    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    /** Sets the closing price for this stock.
     * @param close the close price to set.
     */
    public void setClose(BigDecimal close) {
        this.close = close;
    }

    /** Sets the previous closing price for this stock.
     * @param previousClose the previous close price to set.
     */
    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    /** Sets the daily change for this stock.
     * @param dailyChange the daily change to set.
     */
    public void setDailyChange(BigDecimal dailyChange) {
        this.dailyChange = dailyChange;
    }

    /** Sets the time series map for this stock.
     * @param timeSeries the time series map to set.
     */
    public void setTimeSeries(Map<LocalDate, DailyPriceData> timeSeries) {
        this.timeSeries = timeSeries;
    }

    /** Returns whether this stock object has had its financial metrics calculated.
     * @return True or false depending on whether it has.
     */
    public boolean hasCalculatedMetrics() {
        return beta != null
                && alpha != null
                && sharpeRatio != null;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustry() {
        return industry;
    }
}