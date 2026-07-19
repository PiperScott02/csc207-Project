import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class StockFinancialService {

    /* For alpha and the Sharpe Ratio, we used the yield of a U.S Treasury Bill, converted to a daily rate, using the
 approximate number of trading days. */
    private static double threemonthusbillreturn = 0.0379;

    private static final double riskFreeRate = Math.pow(1+threemonthusbillreturn, 1.0/252.0) - 1.0;

    private static final String defaultMarket = "SPY";

    /*A class which calculates the Alpha, Beta and Sharpe Ratio of the stock corresponding to the given Stock Object*/
    private static double returnRatioOnDate(Stock stock, LocalDate today) {
        BigDecimal diff = stock.getDailyChangeOnDate(today);
        LocalDate yesterday = stock.getPreviousTradingDay(today);
        BigDecimal closeYesterday = stock.getCloseOnDate(yesterday);
        if (diff == null || closeYesterday == null || closeYesterday.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return diff.divide(closeYesterday, 12, RoundingMode.HALF_UP).doubleValue();
    }

    public static List<Double> returnRatios(Stock stock) {
        List<Double> ratiosList = new ArrayList<>();
        List<LocalDate> dates = stock.getDatesSorted();
        for (int i = 1; i < dates.size(); i += 1) {
            LocalDate today = dates.get(i);
            ratiosList.add(returnRatioOnDate(stock, today));
        }
        return ratiosList;
    }

    public static double calculateBeta(Stock stock, Stock market) {
        double covariance = StatisticsService.calculateCovariance(returnRatios(stock), returnRatios(market));
        double marketVariance = StatisticsService.calculateVariance(returnRatios(market));
        if (marketVariance == 0) {
            return 0.0;
        }

        return covariance/marketVariance;
    }


    public static double calculateAlpha(Stock stock, Stock market) {

        List<Double> stockRatios = returnRatios(stock);
        List<Double> marketRatios = returnRatios(market);

        Double stockMean = StatisticsService.calculateMean(stockRatios);
        Double marketMean = StatisticsService.calculateMean(marketRatios);

        double beta = calculateBeta(stock, market);

        return StatisticsService.calculateAlpha(stockMean, marketMean, beta, riskFreeRate);
    }

    public static double calculateSharpeRatio(Stock stock) {
        List<Double> stockRatios = returnRatios(stock);

        Double stockMean = StatisticsService.calculateMean(stockRatios);

        double variance = StatisticsService.calculateVariance(stockRatios);
        if (variance == 0) {
            return 0.0;
        }

        double standardDeviation = Math.sqrt(variance);

        double excessReturn = stockMean - riskFreeRate;

        double sharpeRatio = excessReturn/standardDeviation;

        return sharpeRatio;
    }

    public static void calculateAndAssignMetrics(Stock stock, Stock market) {
        double beta = calculateBeta(stock, market);

        double alpha = calculateAlpha(stock, market);

        double sharpeRatio = calculateSharpeRatio(stock);

        stock.setBeta(beta);

        stock.setAlpha(alpha);

        stock.setSharpeRatio(sharpeRatio);
    }

    public static RealMatrix buildCovarianceMatrix(double[][] covariancesArray) {
        return new Array2DRowRealMatrix(covariancesArray);
    }

    public static double[][] buildCovariancesArray(List<Stock> stockList) {
        int numberOfStocks = stockList.size();
        double [][] covariancesArray = new double[numberOfStocks][numberOfStocks];
        for (int i = 0; i < numberOfStocks; i++) {
            for (int j = i; j < numberOfStocks; j++) {
                double covariance = StatisticsService.calculateCovariance(returnRatios(stockList.get(i)),
                        returnRatios(stockList.get(j)));
                covariancesArray[i][j] = covariance;
            }
        }
        return covariancesArray;
    }
}
