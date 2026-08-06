package data_access.ticker_search;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Map;

public class TickerSearchDailyJSONResponse {

    @SerializedName("Meta Data")
    private MetaData metaData;

    @SerializedName("Time Series (Daily)")
    private Map<String, DailyInfo> timeSeriesDaily;

    public String getTickerSymbol() {
        return this.metaData.getTickerSymbol();
    }

    public BigDecimal getLastClose() {
        final String lastRefreshed = this.metaData.getLastRefreshed();
        return new BigDecimal(this.timeSeriesDaily.get(lastRefreshed).getClose());
    }

    private static class MetaData {
        @SerializedName("2. Symbol")
        private String tickerSymbol;

        @SerializedName("3. Last Refreshed")
        private String lastRefreshed;

        private String getTickerSymbol() {
            return this.tickerSymbol;
        }

        private String getLastRefreshed() {
            return this.lastRefreshed;
        }
    }

    private static class DailyInfo {
        @SerializedName("4. close")
        private String close;

        private String getClose() {
            return this.close;
        }
    }

}
