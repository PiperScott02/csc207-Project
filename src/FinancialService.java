import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class FinancialService {

    /* For alpha and the Sharpe Ratio, we used the yield of a U.S Treasury Bill, converted to a daily rate, using the
 approximate number of trading days. */
    private double threemonthusbillreturn = 0.0379;

    private final BigDecimal riskFreeRate = BigDecimal.valueOf(pow(1+threemonthusbillreturn, 1.0/252.0) - 1.0);

    /*A method which calculates the Alpha, Beta and Sharpe Ratio of the stock corresponding to the given Stock Object*/
    private List<BigDecimal> returnRatios(List<DailyPriceData> timeline) {
        List<BigDecimal> ratiosList = new ArrayList<>();
        for (int i = 0; i < timeline.size() - 1; i += 1) {
            BigDecimal priceYesterday = timeline.get(i).getClose();
            BigDecimal priceToday = timeline.get(i + 1).getClose();
            BigDecimal change = priceToday.subtract(priceYesterday);
            BigDecimal ratioReturn = change.divide(priceYesterday, 12, RoundingMode.HALF_UP);
            ratiosList.add(ratioReturn);
        }
        return ratiosList;
    }

    private BigDecimal returnMean(List<BigDecimal> returnRatios) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal r: returnRatios) {
            sum = sum.add(r);
        }
        return sum.divide(BigDecimal.valueOf(returnRatios.size()), 12, RoundingMode.HALF_UP);
    }

    public double calculateVariance(List<BigDecimal> ratios) {
        BigDecimal mean = returnMean(ratios);
        BigDecimal sumofdifferencessquared = BigDecimal.ZERO;
         for (int i = 0; i < ratios.size(); i++) {
            BigDecimal differencesquared = ratios.get(i).subtract(mean).pow(2);
            sumofdifferencessquared = sumofdifferencessquared.add(differencesquared);
        }
         Double variance = sumofdifferencessquared.divide(BigDecimal.valueOf(ratios.size() - 1), 12,
                 RoundingMode.HALF_UP).doubleValue();
         return variance;

    }

    public double calculateCovariance(List<BigDecimal> ratios1, List<BigDecimal> ratios2) {
        BigDecimal mean1 = returnMean(ratios1);
        BigDecimal mean2 = returnMean(ratios2);
        BigDecimal sumofdifferencesmultiplied = BigDecimal.ZERO;
        for (int i = 0; i < ratios1.size(); i++) {
            BigDecimal differences1 = ratios1.get(i).subtract(mean1);
            BigDecimal differences2 = ratios2.get(i).subtract(mean2);
            BigDecimal productofdifferences = differences1.multiply(differences2);
            sumofdifferencesmultiplied = sumofdifferencesmultiplied.add(productofdifferences);
        }
        Double covariance = sumofdifferencesmultiplied.divide(BigDecimal.valueOf(ratios1.size() - 1), 12,
                RoundingMode.HALF_UP).doubleValue();
        return covariance;
    }

    public double calculateBeta(Stock stock, Stock market) {
        List<DailyPriceData> stockTimeline = stock.getHistoricalTimeline();
        List<DailyPriceData> marketTimeline = market.getHistoricalTimeline();

        List<BigDecimal> stockRatios = returnRatios(stockTimeline);
        List<BigDecimal> marketRatios = returnRatios(marketTimeline);

        double covariance = calculateCovariance(stockRatios, marketRatios);
        double marketVariance = calculateVariance(marketRatios);
        if (marketVariance == 0) {
            return 0.0;
        }

        return covariance/marketVariance;
    }


    public double calculateAlpha(Stock stock, Stock market) {
        List<DailyPriceData> stockTimeline = stock.getHistoricalTimeline();
        List<DailyPriceData> marketTimeline = market.getHistoricalTimeline();

        List<BigDecimal> stockRatios = returnRatios(stockTimeline);
        List<BigDecimal> marketRatios = returnRatios(marketTimeline);

        BigDecimal stockMean = returnMean(stockRatios);
        BigDecimal marketMean = returnMean(marketRatios);

        double covariance = calculateCovariance(stockRatios, marketRatios);
        double marketVariance = calculateVariance(marketRatios);

        double beta = (marketVariance == 0) ? 0.0 : (covariance / marketVariance);

        double alpha =  stockMean.subtract(riskFreeRate.add(BigDecimal.valueOf(beta)
                .multiply(marketMean.subtract(riskFreeRate)))).doubleValue();

        return alpha;
    }

    public double calculateSharpeRatio(Stock stock) {
        List<DailyPriceData> stockTimeline = stock.getHistoricalTimeline();
        List<BigDecimal> stockRatios = returnRatios(stockTimeline);

        BigDecimal stockMean = returnMean(stockRatios);

        double variance = calculateVariance(stockRatios);
        if (variance == 0) {
            return 0.0;
        }

        double standardDeviation = Math.sqrt(variance);

        double excessReturn = stockMean.subtract(riskFreeRate).doubleValue();

        double sharpeRatio = excessReturn/standardDeviation;

        return sharpeRatio;
    }

    public void calculateAndAssignMetrics(Stock stock, Stock market) {
        double beta = calculateBeta(stock, market);

        double alpha = calculateAlpha(stock, market);

        double sharpeRatio = calculateSharpeRatio(stock);

        stock.setBeta(beta);

        stock.setAlpha(alpha);

        stock.setSharpeRatio(sharpeRatio);
    }
}
