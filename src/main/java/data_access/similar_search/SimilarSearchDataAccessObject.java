package data_access.similar_search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
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
    public String[][] similarStockInfo(String keywords) {
        final String query = "?function=" + function +
                "&keywords=" + keywords +
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

    private String[][] parseJSON(JSONObject responseBody) {
        final JSONArray responseArray = responseBody.getJSONArray("bestMatches");

        String[][] similarStocks = new String[responseArray.length()][3];
        JSONObject responseObject;
        String symbol;
        String name;
        String region;

        for (int i = 0; i < responseArray.length(); i++) {
            responseObject = responseArray.optJSONObject(i);

            symbol = responseObject.optString("1. symbol");
            name =  responseObject.optString("2. name");
            region = responseObject.optString("4. region");

            if (symbol == null || name == null || region == null) {
                throw new RuntimeException("Missing Similar Search Information");
            }

            similarStocks[i][0] = symbol;
            similarStocks[i][1] = name;
            similarStocks[i][2] = region;
        }

        return similarStocks;
    }

    private void checkCommonApiErrors(JSONObject responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("No Similar Search Results");
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
