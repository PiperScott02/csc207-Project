import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyPriceData {

    private String tickerSymbol;

    @JsonProperty("1. open")
    private BigDecimal open;

    @JsonProperty("2. high")
    private BigDecimal high;

    @JsonProperty("3. low")
    private BigDecimal low;

    @JsonProperty("4. close")
    private BigDecimal close;

    @JsonProperty("5. volume")
    private long volume;
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getOpen() {
        return open;
    }
    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }
    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }
    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }
    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }
    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return  "Company=" + tickerSymbol +
                ", DailyPriceData{" +
                "date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}