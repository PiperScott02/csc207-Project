import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;




public class Stock {
    private String tickerSymbol;

    private String companyName;

    private BigDecimal close;

    private BigDecimal previousClose;

    private String country;

    private String currency;

    private BigDecimal dailyChange;

    private BigDecimal dividendYield;

    private Double sharpeRatio;

    private Double beta;

    private Double alpha;

    private List<DailyPriceData> historicalTimeline;

    private Map<LocalDate, DailyPriceData> timeSeries;

    void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public List<DailyPriceData> getHistoricalTimeline() {
        return historicalTimeline;
    }

    public void setHistoricalTimeline(List<DailyPriceData> historicalTimeline) {
        this.historicalTimeline = historicalTimeline;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getOpenOnDate(LocalDate date) {
        for (DailyPriceData data: historicalTimeline) {
            if (data.getDate().equals(date)) {
                return data.getOpen();
            }
        }
        return null;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public BigDecimal getCloseOnDate(LocalDate date) {
        for (DailyPriceData data: historicalTimeline) {
            if (data.getDate().equals(date)) {
                return data.getClose();
            }
        }
        return null;
    }

    public BigDecimal getDailyChange() {
        return dailyChange;
    }

    public BigDecimal getDailyChangeOnDate(LocalDate date) {
        BigDecimal closeToday = getCloseOnDate(date);
        BigDecimal closeYesterday = getCloseOnDate(this.getPreviousTradingDay(date));
        if (closeToday != null && closeYesterday != null ) {
            return closeToday.subtract(closeYesterday);
        }
        return null;
    }

    public LocalDate getPreviousTradingDay(LocalDate date) {
        List<LocalDate> dates = getDatesSorted();
        int dateIndex = dates.indexOf(date);
        if (dateIndex <= 0 ) {
            return null;
        }
        return dates.get(dateIndex - 1);

    }

    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    public double getSharpeRatio() {
        return sharpeRatio;
    }

    public DailyPriceData getDailyPriceDataOnDate(LocalDate date) {
        return this.timeSeries.get(date);
    }

    public List<LocalDate> getDatesSorted() {
        if (this.timeSeries == null) {
            return Collections.emptyList();
        }
        List<LocalDate> dates = new ArrayList<>(this.timeSeries.keySet());
        Collections.sort(dates);
        return dates;
    }

    public Double getBeta() {
        return beta;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public void setDailyChange(BigDecimal dailyChange) {
        this.dailyChange = dailyChange;
    }

    public void setTimeSeries(Map<LocalDate, DailyPriceData> timeSeries) {
        this.timeSeries = timeSeries;
    }


}
