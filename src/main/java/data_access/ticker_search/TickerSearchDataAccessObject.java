package data_access.ticker_search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Stock;
import use_case.TickerSearchDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class TickerSearchDataAccessObject implements TickerSearchDataAccessInterface {
    private static final String function = "OVERVIEW";
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final String api_key;

    public TickerSearchDataAccessObject(String api_key) {
        this.api_key = api_key;
    }

    @Override
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
        TickerSearchJSONResponse javaResponse =
                GSON.fromJson(responseBody, TickerSearchJSONResponse.class);

        final Stock stock = new Stock();

        stock.setTickerSymbol(javaResponse.getTickerSymbol());
        stock.setCompanyName(javaResponse.getCompanyName());
        stock.setPreviousClose(javaResponse.getStockPrice());
        stock.setCountry(javaResponse.getCountry());
        stock.setIndustry(javaResponse.getIndustry());

        return stock;
    }
}
