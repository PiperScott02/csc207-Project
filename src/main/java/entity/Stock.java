package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a Stock entity containing historical prices, financial metrics, and a time series object.
 */
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
    private Double annualizedSharpeRatio;
    private Double annualizedAlpha;
    private List<DailyPriceData> historicalTimeline;
    private Map<LocalDate, DailyPriceData> timeSeries;

    // ==========================================
    // Getters and Setters
    // ==========================================

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public Double getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(Double sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public BigDecimal getDailyPriceChange() {
        return dailyChange;
    }

    public void setDailyChange(BigDecimal dailyChange) {
        this.dailyChange = dailyChange;
    }

    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(BigDecimal dividendYield) {
        this.dividendYield = dividendYield;
    }

    public Double getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public Double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public Double getAnnualizedSharpeRatio() {
        return annualizedSharpeRatio;
    }

    public void setAnnualizedSharpeRatio(double annualizedSharpeRatio) {
        this.annualizedSharpeRatio = annualizedSharpeRatio;
    }

    public Double getAnnualizedAlpha() {
        return annualizedAlpha;
    }

    public void setAnnualizedAlpha(double annualizedAlpha) {
        this.annualizedAlpha = annualizedAlpha;
    }

    public List<DailyPriceData> getHistoricalTimeline() {
        return historicalTimeline;
    }

    public void setHistoricalTimeline(List<DailyPriceData> historicalTimeline) {
        this.historicalTimeline = historicalTimeline;
    }

    public Map<LocalDate, DailyPriceData> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(Map<LocalDate, DailyPriceData> timeSeries) {
        this.timeSeries = timeSeries;
    }

    // ==========================================
    // Utility and Business Logic Methods
    // ==========================================

    /**
     * Returns the opening price on a specific date.
     * @param date the date to look up.
     * @return the opening price on the given date, or null if not found.
     */
    public BigDecimal getOpenOnDate(LocalDate date) {
        if (historicalTimeline == null) {
            return null;
        }
        for (DailyPriceData data : historicalTimeline) {
            if (data.getDate().equals(date)) {
                return data.getOpen();
            }
        }
        return null;
    }

    /**
     * Returns the closing price on a specific date.
     * @param date the date to look up.
     * @return the closing price on the given date, or null if not found.
     */
    public BigDecimal getCloseOnDate(LocalDate date) {
        if (historicalTimeline == null) {
            return null;
        }
        for (DailyPriceData data : historicalTimeline) {
            if (data.getDate().equals(date)) {
                return data.getClose();
            }
        }
        return null;
    }

    /**
     * Calculates the daily price change on a specific date.
     * @param date the date to calculate change for.
     * @return the price difference between the date and the previous trading day, or null if data is missing.
     */
    public BigDecimal getDailyChangeOnDate(LocalDate date) {
        BigDecimal closeToday = getCloseOnDate(date);
        BigDecimal closeYesterday = getCloseOnDate(this.getPreviousTradingDay(date));
        if (closeToday != null && closeYesterday != null) {
            return closeToday.subtract(closeYesterday);
        }
        return null;
    }

    /**
     * Finds the previous trading day relative to a given date.
     * @param date the reference date.
     * @return the previous trading day's LocalDate, or null if none exists.
     */
    public LocalDate getPreviousTradingDay(LocalDate date) {
        List<LocalDate> dates = getDatesSorted();
        int dateIndex = dates.indexOf(date);
        if (dateIndex <= 0) {
            return null;
        }
        return dates.get(dateIndex - 1);
    }

    /**
     * Returns the last Trading Day for this stock.
     * @return the last entry in the getDatesSorted list, or null if empty.
     */
    public LocalDate getLastTradingDay() {
        List<LocalDate> dates = getDatesSorted();
        if (dates.isEmpty()) {
            return null;
        }
        return dates.get(dates.size() - 1);
    }

    /**
     * Returns daily price data for a specific date from the time series.
     * @param date the date to look up.
     * @return the DailyPriceData object for that date.
     */
    public DailyPriceData getDailyPriceDataOnDate(LocalDate date) {
        if (this.timeSeries == null) {
            return null;
        }
        return this.timeSeries.get(date);
    }

    /**
     * Returns a sorted list of all dates available in the time series.
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

    /**
     * Returns whether this stock object has had its financial metrics calculated.
     * @return True or false depending on whether it has.
     */
    public boolean hasCalculatedMetrics() {
        return beta != null
                && alpha != null
                && sharpeRatio != null;
    }
}
