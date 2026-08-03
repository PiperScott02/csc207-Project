package data_access.ticker_search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Stock;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class TickerSearchDailyDataAccessObject {
    private static final String function = "TIME_SERIES_DAILY";
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final String api_key;

    public TickerSearchDailyDataAccessObject(String api_key) {
        this.api_key = api_key;
    }

    public Stock createBasicStock(String tickerSymbol) throws IOException, InterruptedException {
        final String query = "?function=" + function +
                "&symbol=" + tickerSymbol +
                "&apikey=" + api_key;
        final String location = "https://www.alphavantage.co/query" + query;

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(location))
                .build();
        TimeUnit.SECONDS.sleep(1);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return parseJSON(response.body());
    }

    private Stock parseJSON(String responseBody) {
        TickerSearchDailyJSONResponse javaResponse =
                GSON.fromJson(responseBody, TickerSearchDailyJSONResponse.class);

        final Stock stock = new Stock();

        stock.setTickerSymbol(javaResponse.getTickerSymbol());
        stock.setPreviousClose(javaResponse.getLastClose());
        stock.setCountry(null);
        stock.setCompanyName(null);
        stock.setIndustry(null);

        return stock;
    }
}
