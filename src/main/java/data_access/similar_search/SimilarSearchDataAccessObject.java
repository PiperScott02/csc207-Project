package data_access;

import use_case.similar_search.SimilarSearchDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SimilarSearchDataAccessObject implements SimilarSearchDataAccessInterface {
    public static final String DEFAULT_API_KEY = "PTZRDMMS8UYGPQ7G";
    public static final String function = "SYMBOL_SEARCH";
    public static final HttpClient client = HttpClient.newBuilder().build();

    public final String api_key;

    public SimilarSearchDataAccessObject() {
        this.api_key = DEFAULT_API_KEY;
    }

    @Override
    public List<String> similarNames(String keywords) throws IOException, InterruptedException {
        final String query = "?function=" + function +
                "&keywords=" + keywords +
                "&apikey=" + api_key;
        final String location = "https://www.alphavantage.co/query" + query;

        HttpRequest request = HttpRequest
                        .newBuilder()
                        .uri(URI.create(location))
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            // TODO: make this throw specific error
            // specific info for different error codes found here:
            // https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status
        }

        return parseJSON(response.body());
    }

    private List<String> parseJSON(String responseBody) {
        return List.of(); // TODO: implement
    }
}
