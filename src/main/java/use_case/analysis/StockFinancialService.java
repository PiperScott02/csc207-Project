package use_case.analysis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import entity.Stock;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/** Service class for calculating financial analytics and risk metrics for stocks. */
public class StockFinancialService {

    /* For alpha and the Sharpe Ratio, we used the yield of a U.S Treasury Bill, converted to a daily rate, using the
 approximate number of trading days. */
    private static final double THREE_MONTH_US_BILL_RETURN = 0.0379;

    private static final double RISK_FREE_RATE = Math.pow(1 + THREE_MONTH_US_BILL_RETURN, 1.0 / 252.0) - 1.0;

    private static final String DEFAULT_MARKET = "SPY";

    /** Calculates the return ratio of a stock on a specific date.
     * @param stock the Stock entity.
     * @param today the LocalDate to calculate for.
     * @return the return ratio as a double.
     */
    private static double returnRatioOnDate(Stock stock, LocalDate today) {
        BigDecimal diff = stock.getDailyChangeOnDate(today);
        LocalDate yesterday = stock.getPreviousTradingDay(today);
        BigDecimal closeYesterday = stock.getCloseOnDate(yesterday);
        if (diff == null || closeYesterday == null || closeYesterday.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return diff.divide(closeYesterday, 12, RoundingMode.HALF_UP).doubleValue();
    }

    /** Generates a list of all historical return ratios for a stock.
     * @param stock the Stock entity.
     * @return a list of return ratio doubles.
     */
    public static List<Double> returnRatios(Stock stock) {
        List<Double> ratiosList = new ArrayList<>();
        List<LocalDate> dates = stock.getDatesSorted();
        for (int i = 1; i < dates.size(); i += 1) {
            LocalDate today = dates.get(i);
            ratiosList.add(returnRatioOnDate(stock, today));
        }
        return ratiosList;
    }

    public static Map<LocalDate, Double> returnDateRatiosMap(Stock stock) {
        Map<LocalDate, Double> dateRatiosList = new TreeMap<>();
        List<LocalDate> dates = stock.getDatesSorted();
        for (int i = 1; i < dates.size(); i += 1) {
            LocalDate today = dates.get(i);
            Double returnToday = returnRatioOnDate(stock, today);
            dateRatiosList.put(today, returnToday);
        }
        return dateRatiosList;
    }

    /** Calculates the beta of a stock relative to a market stock.
     * @param stock the target Stock.
     * @param market the market benchmark Stock.
     * @return the calculated beta as a double.
     */
    public static double calculateBeta(Stock stock, Stock market) {
        double covariance = StatisticsService.calculateCovariance(returnRatios(stock), returnRatios(market));
        double marketVariance = StatisticsService.calculateVariance(returnRatios(market));
        if (marketVariance == 0) {
            return 0.0;
        }

        return covariance/marketVariance;
    }


    /** Calculates the alpha of a stock relative to a market stock.
     * @param stock the target Stock.
     * @param market the market benchmark Stock.
     * @return the calculated alpha as a double.
     */
    public static double calculateAlpha(Stock stock, Stock market) {

        List<Double> stockRatios = returnRatios(stock);
        List<Double> marketRatios = returnRatios(market);

        Double stockMean = StatisticsService.calculateMean(stockRatios);
        Double marketMean = StatisticsService.calculateMean(marketRatios);

        double beta = calculateBeta(stock, market);

        return StatisticsService.calculateAlpha(stockMean, marketMean, beta, RISK_FREE_RATE);
    }

    /** Calculates the Sharpe ratio of a stock.
     * @param stock the target Stock.
     * @return the calculated Sharpe ratio as a double.
     */
    public static double calculateSharpeRatio(Stock stock) {
        List<Double> stockRatios = returnRatios(stock);

        Double stockMean = StatisticsService.calculateMean(stockRatios);

        double variance = StatisticsService.calculateVariance(stockRatios);
        if (variance == 0) {
            return 0.0;
        }

        double standardDeviation = Math.sqrt(variance);

        double excessReturn = stockMean - RISK_FREE_RATE;

        double sharpeRatio = excessReturn/standardDeviation;

        return sharpeRatio;
    }

    /** Computes and assigns metrics (beta, alpha, Sharpe ratio) to a stock.
     * @param stock the target Stock to update.
     * @param market the market benchmark Stock.
     */
    public static void calculateAndAssignMetrics(Stock stock, Stock market) {
        double beta = calculateBeta(stock, market);

        double alpha = calculateAlpha(stock, market);

        double sharpeRatio = calculateSharpeRatio(stock);

        stock.setBeta(beta);

        stock.setAlpha(alpha);

        stock.setSharpeRatio(sharpeRatio);
    }

    /** Converts a covariance 2D array into a RealMatrix.
     * @param covariancesArray the 2D double array of covariances.
     * @return a RealMatrix representation.
     */
    public static RealMatrix buildCovarianceMatrix(double[][] covariancesArray) {
        return new Array2DRowRealMatrix(covariancesArray);
    }

    /** Builds a 2D array of pairwise covariances among a list of stocks.
     * @param stockList the list of Stock entities.
     * @return a 2D double array of covariances.
     */
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