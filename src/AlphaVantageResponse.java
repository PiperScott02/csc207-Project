import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageResponse {

    @JsonProperty("Time Series (Daily)")
    private Map<String, DailyPriceData> timeSeries;

    public Map<String,   DailyPriceData> getTimeSeries(){
        return timeSeries;
    }
    @JsonSetter("Time Series (Daily)")
    public void setTimeSeries(Map<String, DailyPriceData> timeSeries) {
        this.timeSeries = timeSeries;

        if (timeSeries != null) {
            for (Map.Entry<String, DailyPriceData> entry: timeSeries.entrySet()) {
                entry.getValue().setDate(entry.getKey());
            }
        }

    }}
