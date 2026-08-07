package data_access.similar_search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import use_case.similar_search.SimilarSearchDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class SimilarSearchDataAccessObject implements SimilarSearchDataAccessInterface {
    public static final String function = "SYMBOL_SEARCH";
    public static final HttpClient client = HttpClient.newBuilder().build();

    public final String api_key;

    public SimilarSearchDataAccessObject(String api_key) {
        this.api_key = api_key;
    }

    @Override
    public String[][] similarStockInfo(String keywords) throws IOException, InterruptedException {
        final String query = "?function=" + function +
                "&keywords=" + keywords +
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

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private String[][] parseJSON(String responseBody) {
        SimilarSearchJSONResponse javaResponse =
                GSON.fromJson(responseBody, SimilarSearchJSONResponse.class);

        String[][] similarStocks = new String[javaResponse.getLength()][3];

        for (int i = 0; i < javaResponse.getLength(); i++) {
            similarStocks[i][0] = javaResponse.getTickerSymbol(i);
            similarStocks[i][1] = javaResponse.getCompanyName(i);
            similarStocks[i][2] = javaResponse.getRegion(i);
        }

        return similarStocks;
    }
}
