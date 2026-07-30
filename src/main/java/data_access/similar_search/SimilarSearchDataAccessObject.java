package data_access.similar_search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import use_case.similar_search.SimilarSearchDataAccessInterface;
import use_case.similar_search.SimilarSearchOutputData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimilarSearchDataAccessObject implements SimilarSearchDataAccessInterface {
    public static final String function = "SYMBOL_SEARCH";
    public static final HttpClient client = HttpClient.newBuilder().build();

    public final String api_key;

    public SimilarSearchDataAccessObject(String api_key) {
        this.api_key = api_key;
    }

    @Override
    public String[] similarNames(String keywords) throws IOException, InterruptedException {
        if (keywords.contains(" ")) {
            return null;
        }

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

    private String[] parseJSON(String responseBody) {
        SimilarSearchJSONResponse javaResponse =
                GSON.fromJson(responseBody, SimilarSearchJSONResponse.class);

        String[] similarStockTickerSymbols = new String[javaResponse.bestMatches.length];

        for (int i = 0; i < javaResponse.bestMatches.length; i++) {
            similarStockTickerSymbols[i] = javaResponse.bestMatches[i].tickerSymbol;
        }

        return similarStockTickerSymbols;
    }
}
