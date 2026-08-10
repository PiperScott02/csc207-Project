package data_access.ticker_search;

import entity.Stock;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class TickerSearchDailyDataAccessObject {
    private static final String function = "TIME_SERIES_DAILY";
    private static final HttpClient client = HttpClient.newBuilder().build();

    private final String api_key;

    public TickerSearchDailyDataAccessObject(String api_key) {
        this.api_key = api_key;
    }

    public Stock createBasicStock(String tickerSymbol) {
        final String query = "?function=" + function +
                "&symbol=" + tickerSymbol +
                "&apikey=" + api_key;
        final String location = "https://www.alphavantage.co/query" + query;

        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(location))
                    .build();
            TimeUnit.SECONDS.sleep(1);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            final JSONObject responseBody = new JSONObject(response.body());

            checkCommonApiErrors(responseBody);

            return parseJSON(responseBody);
        } catch (IOException e) {
            throw new RuntimeException("Unable to Connect to AlphaVantage API", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Search Interrupted", e);
        }
    }

    private Stock parseJSON(JSONObject responseBody) {
        final Stock stock = new Stock();

        final JSONObject metaData = responseBody.optJSONObject("Meta Data");

        if (metaData == null) {
            throw new RuntimeException("Time Series Daily Missing Information");
        }

        final String tickerSymbol = metaData.optString("2. Symbol");
        final String lastRefreshed = metaData.optString("3. Last Refreshed");

        if (tickerSymbol == null || lastRefreshed == null) {
            throw new RuntimeException("Time Series Daily Missing Important Information");
        }

        final JSONObject timeSeriesDaily = responseBody.optJSONObject("Time Series (Daily)");

        if (timeSeriesDaily == null || timeSeriesDaily.isEmpty()) {
            throw new RuntimeException("Time Series Daily Missing time series Important Information");
        }

        final JSONObject lastRefreshedInfo = timeSeriesDaily.optJSONObject(lastRefreshed);

        if (lastRefreshedInfo == null || lastRefreshedInfo.isEmpty()) {
            throw new RuntimeException(tickerSymbol + "Time Series Daily Missing last refreshed info Important Information");
        }

        final BigDecimal previousClose = lastRefreshedInfo.optBigDecimal("4. close", null);

        if (previousClose == null) {
            throw new RuntimeException("Time Series Daily Missing previous close Important Information");
        }

        stock.setTickerSymbol(tickerSymbol);
        stock.setPreviousClose(previousClose);
        stock.setCountry(null);
        stock.setCompanyName(null);
        stock.setIndustry(null);

        return stock;
    }

    private void checkCommonApiErrors(JSONObject responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("No Time Series Daily Results");
        }

        if (responseBody.has("Error Message")) {
            throw new RuntimeException(responseBody.getString("Error Message"));
        }

        if (responseBody.has("Information")) {
            throw new RuntimeException(responseBody.getString("Information"));
        }

        if (responseBody.has("Note")) {
            throw new RuntimeException(responseBody.getString("Note"));
        }
    }
}
