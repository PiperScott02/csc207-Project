import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FinancialServiceTest {

    public static void main(String[] args) {

        List<DailyPriceData> timeline = new ArrayList<>();

        double[] prices = {
                100,
                101,
                100,
                102,
                101,
                103
        };

        for (int i = 0; i < prices.length; i++) {
            DailyPriceData d = new DailyPriceData();
            d.setDate("2026-01-" + String.format("%02d", i + 1));
            d.setClose(BigDecimal.valueOf(prices[i]));
            timeline.add(d);
        }

        Stock stock = new Stock();
        stock.setHistoricalTimeline(timeline);

        FinancialService service = new FinancialService();

        double sharpe = service.calculateSharpeRatio(stock);

        System.out.println("Sharpe = " + sharpe);
    }
}