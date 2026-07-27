package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

import java.math.BigDecimal;

/** Represents daily price and volume data for a stock on a specific date. */
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

    /** Returns the date of this price record.
     * @return the LocalDate of the data.
     */
    public LocalDate getDate() {
        return date;
    }

    /** Sets the date for this price record.
     * @param date the LocalDate to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /** Returns the opening price.
     * @return the opening BigDecimal price.
     */
    public BigDecimal getOpen() {
        return open;
    }

    /** Sets the opening price.
     * @param open the opening BigDecimal price to set.
     */
    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    /** Returns the high price of the day.
     * @return the high BigDecimal price.
     */
    public BigDecimal getHigh() {
        return high;
    }

    /** Sets the high price of the day.
     * @param high the high BigDecimal price to set.
     */
    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    /** Returns the low price of the day.
     * @return the low BigDecimal price.
     */
    public BigDecimal getLow() {
        return low;
    }

    /** Sets the low price of the day.
     * @param low the low BigDecimal price to set.
     */
    public void setLow(BigDecimal low) {
        this.low = low;
    }

    /** Returns the closing price.
     * @return the closing BigDecimal price.
     */
    public BigDecimal getClose() {
        return close;
    }

    /** Sets the closing price.
     * @param close the closing BigDecimal price to set.
     */
    public void setClose(BigDecimal close) {
        this.close = close;
    }

    /** Returns the trading volume.
     * @return the volume as a long.
     */
    public long getVolume() {
        return volume;
    }

    /** Sets the trading volume.
     * @param volume the volume to set as a long.
     */
    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return  "Company=" + tickerSymbol +
                ", entity.DailyPriceData{" +
                "date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}