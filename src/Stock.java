import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;




public class Stock {
    private String tickerSymbol;

    private String companyName;

    private BigDecimal currentPrice;

    private BigDecimal previousClose;

    private String country;

    private String currency;

    private BigDecimal dailyChange;

    private BigDecimal dividendYield;

    private Double sharpeRatio;

    private Double beta;

    private Double alpha;

    private List<DailyPriceData> historicalTimeline;

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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public BigDecimal getDailyChange() {
        return dailyChange;
    }

    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    public double getSharpeRatio() {
        return sharpeRatio;
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


}
