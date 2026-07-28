package app;

import data_access.stock_daily.StockService;
import entity.Stock;
import use_case.analysis.StockFinancialService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

    /* Access data now
     */

    /*public class app.Main {

        public static void main(String[] args) throws IOException, InterruptedException {

            String apiKey = "1BPENK5QMO8ULOU1";

            String url =
                    "https://www.alphavantage.co/query" +
                            "?function=GLOBAL_QUOTE" +
                            "&symbol=AAPL" +
                            "&apikey=" + apiKey;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
        }
    }*/

    /* Access historical data*/

    public class Main {
        public static void main(String[] args) throws IOException, InterruptedException {
                StockService stockService = new StockService("PTZRDMMS8UYGPQ7G");
            StockFinancialService financialService = new StockFinancialService();
            String tickerSymbol = "GOOG";
            String marketTickerSymbol  = "SPY";
            Stock google = stockService.createStockAndHistory(tickerSymbol);
            System.out.println(google.getTickerSymbol());
            System.out.println(google.getHistoricalTimeline().get(99).getDate());
            TimeUnit.SECONDS.sleep(1);
            Stock market = stockService.createStockAndHistory(marketTickerSymbol);
            financialService.calculateAndAssignMetrics(google, market);
            System.out.println("This is the beta " +     google.getBeta());
            System.out.println(google.getDailyPriceChange());
            System.out.println(google.getCloseOnDate(LocalDate.now()));
            System.out.println(google.getOpenOnDate(LocalDate.now()));
            System.out.println(google.getCloseOnDate(LocalDate.now().minusDays(1)));
            System.out.println(google.getHistoricalTimeline().size());
        }}














