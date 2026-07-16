import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDate;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageResponse {

    @JsonProperty("Time Series (Daily)")
    private Map<LocalDate, DailyPriceData> timeSeries;

    public Map<LocalDate,   DailyPriceData> getTimeSeries(){
        return timeSeries;
    }
    @JsonSetter("Time Series (Daily)")
    public void setTimeSeries(Map<LocalDate, DailyPriceData> timeSeries) {
        this.timeSeries = timeSeries;

        if (timeSeries != null) {
            for (Map.Entry<LocalDate, DailyPriceData> entry: timeSeries.entrySet()) {
                entry.getValue().setDate(entry.getKey());
            }
        }

    }}
