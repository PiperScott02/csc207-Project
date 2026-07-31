package data_access;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import use_case.currency_conversion.CurrencyConversionDataAccessInterface;

/**
 * Retrieves live currency exchange rates from the Frankfurter API.
 */
public class CurrencyConversionDataAccessObject
        implements CurrencyConversionDataAccessInterface {

    private static final String API_BASE_URL =
            "https://api.frankfurter.dev/v2/rate/";

    private static final int HTTP_OK = 200;

    private final HttpClient httpClient;

    /**
     * Creates the currency data-access object.
     */
    public CurrencyConversionDataAccessObject() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public BigDecimal getExchangeRate(
            String fromCurrency,
            String toCurrency) throws IOException, InterruptedException {

        if (fromCurrency == null || toCurrency == null) {
            throw new IllegalArgumentException(
                    "Currency codes cannot be null."
            );
        }

        final String normalizedFrom =
                fromCurrency.trim().toUpperCase();

        final String normalizedTo =
                toCurrency.trim().toUpperCase();

        if (normalizedFrom.equals(normalizedTo)) {
            return BigDecimal.ONE;
        }

        final String url =
                API_BASE_URL
                        + normalizedFrom
                        + "/"
                        + normalizedTo;

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        final HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != HTTP_OK) {
            throw new IOException(
                    "Exchange-rate API returned HTTP "
                            + response.statusCode()
                            + ": "
                            + response.body()
            );
        }

        final JSONObject responseBody =
                new JSONObject(response.body());

        if (!responseBody.has("rate")) {
            throw new IOException(
                    "Exchange-rate response did not contain a rate."
            );
        }

        return responseBody.getBigDecimal("rate");
    }
}