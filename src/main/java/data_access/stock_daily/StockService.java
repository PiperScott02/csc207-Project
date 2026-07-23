package data_access.stock_daily;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.DailyPriceData;
import entity.Stock;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StockService {
    /* Helper class that, given an API key, builds an entity.Stock object for the corresponding ticker symbol,
    and produces a timeline of the last 100 entity.DailyPriceData objects sorted by date.
     */

    private static final String DEFAULT_API_KEY = "1BPENK5QMO8ULOU1";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    /** Constructs a StockService using the default API key. */
    public StockService() {
        this(DEFAULT_API_KEY);
    }

    /** Constructs a StockService using a custom API key.
     * @param apiKey the Alpha Vantage API key to use.
     */
    public StockService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public Stock createStockAndHistory(String tickerSymbol) throws IOException, InterruptedException {
        String url =
                "https://www.alphavantage.co/query" +
                        "?function=TIME_SERIES_DAILY" +
                        "&symbol=" + tickerSymbol +
                        "&apikey=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        TimeUnit.SECONDS.sleep(1);
        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        AlphaVantageResponse apiResponse = objectMapper.readValue(response.body(), AlphaVantageResponse.class);
        Map<LocalDate, DailyPriceData> timeSeries = apiResponse.getTimeSeries();

        List<DailyPriceData> timeline = new ArrayList<>(timeSeries.values());
        timeline.sort(Comparator.comparing(DailyPriceData::getDate));

        Stock stock = new Stock();
        stock.setTickerSymbol(tickerSymbol);
        stock.setHistoricalTimeline(timeline);
        stock.setTimeSeries(timeSeries);
        int size = timeline.size();
        stock.setClose(timeline.get(size - 1).getClose());
        stock.setPreviousClose(timeline.get(size - 2).getClose());
        stock.setDailyChange(stock.getClose().subtract(stock.getPreviousClose()));

        String overviewUrl =
                "https://www.alphavantage.co/query" +
                        "?function=OVERVIEW" +
                        "&symbol=" + tickerSymbol +
                        "&apikey=" + apiKey;

        HttpRequest overviewRequest = HttpRequest.newBuilder().uri(URI.create(overviewUrl)).build();
        TimeUnit.SECONDS.sleep(1);
        HttpResponse<String> overviewResponse =
                httpClient.send(overviewRequest, HttpResponse.BodyHandlers.ofString());

        AlphaVantageOverviewResponse overview = objectMapper.readValue(overviewResponse.body(),
                AlphaVantageOverviewResponse.class);
        if (overview != null && overview.getCompanyName() != null) {
            stock.setCompanyName(overview.getCompanyName());
        } else {
            stock.setCompanyName(tickerSymbol);
        }

        return stock;
    }
}