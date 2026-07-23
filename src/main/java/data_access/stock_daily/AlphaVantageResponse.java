package data_access.stock_daily;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import entity.DailyPriceData;

import java.time.LocalDate;
import java.util.Map;

/** Response mapper object for parsing Alpha Vantage's JSON daily time series payload. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageResponse {

    @JsonProperty("Time Series (Daily)")
    private Map<LocalDate, DailyPriceData> timeSeries;

    /** Returns the map of daily price data keyed by date.
     * @return the time series map.
     */
    public Map<LocalDate, DailyPriceData> getTimeSeries() {
        return timeSeries;
    }

    /** Sets the time series map and injects the corresponding date into each DailyPriceData value.
     * @param timeSeries the raw time series map parsed from JSON.
     */
    @JsonSetter("Time Series (Daily)")
    public void setTimeSeries(Map<LocalDate, DailyPriceData> timeSeries) {
        this.timeSeries = timeSeries;

        if (timeSeries != null) {
            for (Map.Entry<LocalDate, DailyPriceData> entry : timeSeries.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().setDate(entry.getKey());
                }
            }
        }
    }
}