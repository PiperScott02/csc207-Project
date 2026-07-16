    import com.fasterxml.jackson.databind.ObjectMapper;
    import java.io.IOException;
    import java.net.URI;
    import java.net.http.HttpClient;
    import java.net.http.HttpRequest;
    import java.net.http.HttpResponse;
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Map;

    public class StockService {
        /* Helper class that, given an API key, builds a Stock object for the correspdonding ticker symbol, and produces
        a timeline of the last 100 DailyPriceData objects sorted by date.
         */

        private final HttpClient httpClient;

        private final ObjectMapper objectMapper;

        private final String apiKey;

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

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonBody = response.body();
            AlphaVantageResponse apiResponse = objectMapper.readValue(response.body(), AlphaVantageResponse.class);
            Map<LocalDate, DailyPriceData> historicalData = apiResponse.getTimeSeries();

            /*System.out.println(jsonBody);

            if (historicalData == null) {
                System.err.println("Error: API returned no data.");
                return null; //
            }*/

            List<DailyPriceData> timeline = new ArrayList<>(historicalData.values());
            timeline.sort(Comparator.comparing(DailyPriceData::getDate));

            Stock stock = new Stock();
            stock.setTickerSymbol(tickerSymbol);
            stock.setHistoricalTimeline(timeline);
            stock.setCurrentPrice(timeline.get(99).getOpen());
            stock.setPreviousClose(timeline.get(98).getClose());
            stock.setDailyChange(stock.getCurrentPrice().subtract(stock.getPreviousClose()));
            return stock;


        }
    }