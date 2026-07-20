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
                StockService stockService = new StockService("1BPENK5QMO8ULOU1");
            StockFinancialService financialService = new StockFinancialService();
            String tickerSymbol = "GOOG";
            String marketTickerSymbol  = "SPY";
            Stock google = stockService.createStockAndHistory(tickerSymbol);
            System.out.println(google.getTickerSymbol());
            System.out.println(google.getHistoricalTimeline().get(99).getDate());
            TimeUnit.SECONDS.sleep(1);
            Stock market = stockService.createStockAndHistory(marketTickerSymbol);
            financialService.calculateAndAssignMetrics(google, market);
            System.out.println(google.getAlpha());
            System.out.println(google.getDailyChange());
            System.out.println(google.getCloseOnDate(LocalDate.now()));
            System.out.println(google.getOpenOnDate(LocalDate.now()));
            System.out.println(google.getCloseOnDate(LocalDate.now().minusDays(1)));
            System.out.println(google.getHistoricalTimeline().size());
        }}














    /*public class app.Main {
        // This is the entry point Java requires
        public static void main(String[] args) {
            // Create an instance of app.Main to call your test method
            app.Main mainInstance = new app.Main();
            mainInstance.testSharpeRatioLogic();
        }
        public void testSharpeRatioLogic() {
            // 1. Setup: Create a entity.Stock with perfectly predictable data
            entity.Stock mockStock = new entity.Stock();
            mockStock.setTickerSymbol("TEST");

            // Create 100 days of data
            List<entity.DailyPriceData> timeline = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                entity.DailyPriceData data = new entity.DailyPriceData();
                data.setDate("2026-01-" + (i + 1));
                data.setOpen(BigDecimal.valueOf(100.0 + i));
                data.setHigh(BigDecimal.valueOf(105.0 + i));
                data.setLow(BigDecimal.valueOf(95.0 + i));
                data.setClose(BigDecimal.valueOf(102.0 + i));
                data.setVolume(1000L);

                timeline.add(data);
            }
            mockStock.setHistoricalTimeline(timeline);

            // 2. Execute: Run your calculation
            FinancialService service = new FinancialService();
            double result = service.calculateSharpeRatio(mockStock);

            // 3. Verify: Check if the result matches your manual calculation
            System.out.println("Calculated Sharpe: " + result);

            // To make this a "real" test, add an assertion
            if (Math.abs(result - 0.0) < 0.001) { // Replace 0.0 with your expected manual value
                System.out.println("Test Passed!");
            } else {
                System.out.println("Test Failed: Expected different result.");
            }
        }
    }*/


