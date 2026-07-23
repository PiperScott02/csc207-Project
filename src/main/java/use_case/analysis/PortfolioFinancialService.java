package use_case.analysis;

import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

/** Service class for calculating financial metrics and analytics for a portfolio. */
public class PortfolioFinancialService {
    private static double THREE_MONTH_US_BILL_RETURN = 0.0379;

    private static final double RISK_FREE_RATE = Math.pow(1+ THREE_MONTH_US_BILL_RETURN, 1.0/252.0) - 1.0;

    /** Calculates the weighted beta of the portfolio based on individual stock holdings.
     * @param portfolio the Portfolio to evaluate.
     * @return the calculated weighted beta as a double.
     */
    private static double calculatePortfolioWeightedBeta(Portfolio portfolio) {
        double totalBeta = 0;
        for (StockHolding holding : portfolio.getHoldings()) {
            double holdingShare = portfolio.getHoldingShare(holding);
            double holdingStockBeta = holding.getStock().getBeta();
            double betaContribution = holdingShare*(holdingStockBeta);
            totalBeta += betaContribution;
        }
        return totalBeta;
    }

    /** Calculates the true beta of the portfolio relative to a market stock.
     * @param portfolio the Portfolio to evaluate.
     * @param market the market Stock benchmark.
     * @return the calculated true beta as a double.
     */
    private static double calculatePortfolioTrueBeta(Portfolio portfolio, Stock market) {
        List<Double> portfolioReturns = calculatePortfolioReturnsList(portfolio);
        List<Double> marketReturns = StockFinancialService.returnRatios(market);
        Double portfolioCovariance = StatisticsService.calculateCovariance(portfolioReturns, marketReturns);
        Double marketVariance = StatisticsService.calculateVariance(marketReturns);
        return portfolioCovariance/marketVariance;
    }

    /** Generates a list of daily portfolio returns over the master timeline.
     * @param portfolio the Portfolio to evaluate.
     * @return a list of daily return doubles.
     */
    public static List<Double> calculatePortfolioReturnsList(Portfolio portfolio) {
        List<Double> returns = new ArrayList<>();
        List<LocalDate> timeline = portfolio.getMasterTimeline();
        for (int i = 1; i < timeline.size(); i++) {
            returns.add(portfolio.calculatePortfolioDailyReturn(timeline.get(i)));
        }
        return returns;


    }

    /** Calculates the alpha of the portfolio relative to a market stock.
     * @param portfolio the Portfolio to evaluate.
     * @param market the market Stock benchmark.
     * @return the calculated alpha as a double.
     */
    public static double calculatePortfolioAlpha(Portfolio portfolio, Stock market) {
        List<Double> returns = calculatePortfolioReturnsList(portfolio);
        double portfolioReturnsMean = StatisticsService.calculateMean(returns);
        List<Double> marketRatios = StockFinancialService.returnRatios(market);
        double portfolioCovariance = StatisticsService.calculateCovariance(returns, marketRatios);
        double marketMean = StatisticsService.calculateMean(marketRatios);
        return StatisticsService.calculateAlpha(portfolioReturnsMean, marketMean,
                calculatePortfolioTrueBeta(portfolio, market), RISK_FREE_RATE);
    }

    /** Computes and assigns financial metrics (weighted beta, alpha, true beta, Sharpe ratio) to the portfolio.
     * @param portfolio the Portfolio to update.
     * @param market the market Stock benchmark.
     */
    public static void calculateAndAssignMetrics(Portfolio portfolio, Stock market) {
        double weightedBeta = calculatePortfolioWeightedBeta(portfolio);

        double alpha = calculatePortfolioAlpha(portfolio, market);

        double trueBeta = calculatePortfolioTrueBeta(portfolio, market);

        double sharpeRatio = calculateSharpeRatio(portfolio);

        portfolio.setWeightedBeta(weightedBeta);

        portfolio.setAlpha(alpha);

        portfolio.setTrueBeta(trueBeta);

        portfolio.setSharpeRatio(sharpeRatio);


    }

    /** Builds a 2D array of holding weights for the portfolio.
     * @param portfolio the Portfolio to evaluate.
     * @return a 2D double array representing weights.
     */
    public static double[][] buildWeightsArray(Portfolio portfolio) {
        List<StockHolding> holdings = portfolio.getHoldings();
        int numberOfHoldings = holdings.size();
        double[][] weightsArray = new double[1][numberOfHoldings];
        for (int i = 0; i < numberOfHoldings; i++) {
            StockHolding holding = holdings.get(i);
            double holdingShare = portfolio.getHoldingShare(holding);
            weightsArray[0][i] = holdingShare;
        }
        return weightsArray;
    }

    /** Converts a weights 2D array into a RealMatrix.
     * @param weightsArray the weights 2D double array.
     * @return a RealMatrix representation of the weights.
     */
    public static RealMatrix buildWeightsMatrix(double[][] weightsArray) {
        return new Array2DRowRealMatrix(weightsArray);
    }

    /** Calculates the variance of the portfolio.
     * @param portfolio the Portfolio to evaluate.
     * @return the portfolio variance as a double.
     */
    public static double calculatePortfolioVariance(Portfolio portfolio) {
        List<Stock> stockList = portfolio.getStocks();
        double[][] weightsArray = buildWeightsArray(portfolio);
        double[][] covariancesArray = StockFinancialService.buildCovariancesArray(stockList);

        RealMatrix weightsMatrix = buildWeightsMatrix(weightsArray);
        RealMatrix stocksCovarianceMatrix = StockFinancialService.
                buildCovarianceMatrix(covariancesArray);
        RealMatrix weightsMatrixTranspose = weightsMatrix.transpose();
        RealMatrix portfolioCovariance = weightsMatrix.multiply(stocksCovarianceMatrix).
                multiply(weightsMatrixTranspose);
        return portfolioCovariance.getEntry(0,0);
    }

    /** Calculates the expected portfolio returns number.
     * @param portfolio the Portfolio to evaluate.
     * @return the expected returns as a double.
     */
    public static double calculatePortfolioReturnsNumber(Portfolio portfolio) {
        double returns = 0.0;
        List<Stock> stockList = portfolio.getStocks();
        List<StockHolding> stockHoldingList = portfolio.getHoldings();
        for (StockHolding stockHolding: stockHoldingList) {
            Stock stock = stockHolding.getStock();
            double stockRatio = StatisticsService.calculateMean(StockFinancialService.returnRatios(stock));
            double holdingWeight = portfolio.getHoldingShare(stockHolding);
            double returnsContribtution = stockRatio*holdingWeight;
            returns += returnsContribtution;
        }
        return returns;
    }

    /** Calculates the Sharpe ratio of the portfolio.
     * @param portfolio the Portfolio to evaluate.
     * @return the Sharpe ratio as a double.
     */
    public static double calculateSharpeRatio(Portfolio portfolio) {
        double portfolioStandardDeviation = Math.sqrt(calculatePortfolioVariance(portfolio));
        double portfolioReturns = calculatePortfolioReturnsNumber(portfolio);
        return (portfolioReturns - RISK_FREE_RATE)/portfolioStandardDeviation;

    }
}