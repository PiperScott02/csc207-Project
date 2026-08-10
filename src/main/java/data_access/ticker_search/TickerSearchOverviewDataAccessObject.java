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

public class TickerSearchOverviewDataAccessObject {
    private static final String function = "OVERVIEW";
    private static final HttpClient client = HttpClient.newBuilder().build();

    private final String api_key;

    public TickerSearchOverviewDataAccessObject(String api_key) {
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

            if (response.statusCode() != 200) {
                throw new RuntimeException("Alpha Vantage Returned Status Code " + response.statusCode());
            }

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

        final String tickerSymbol = responseBody.optString("Symbol");
        final String companyName = responseBody.optString("Name");
        final String country = responseBody.optString("Country");
        final String industry = responseBody.optString("Industry");
        final BigDecimal PERatio = responseBody.optBigDecimal("PERatio", null);
        final BigDecimal EPS = responseBody.optBigDecimal("EPS", null);

        if (tickerSymbol == null || PERatio == null || EPS == null) {
            throw new RuntimeException("Overview Missing Important Information");
        }

        stock.setTickerSymbol(tickerSymbol);
        stock.setCompanyName(companyName);
        stock.setPreviousClose(EPS.multiply(PERatio));
        stock.setCountry(country);
        stock.setIndustry(industry);

        return stock;
    }

    private void checkCommonApiErrors(JSONObject responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("No Overview Results");
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
