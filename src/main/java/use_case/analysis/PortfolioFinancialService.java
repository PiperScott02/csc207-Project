package use_case.analysis;

import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.RealMatrix;

/** Service class for calculating financial metrics and analytics for a portfolio. */
public class PortfolioFinancialService {
    private static double THREE_MONTH_US_BILL_RETURN = 0.0379;

    private static final double RISK_FREE_RATE = Math.pow(1 + THREE_MONTH_US_BILL_RETURN, 1.0 / 252.0) - 1.0;

    /** Given two Maps of Local Dates and Doubles, it returns a list containing a list of the doubles with the
     * same corresponding Local Date aligned.*/
    static List<List<Double>> getAlignedReturns(
            Map<LocalDate, Double> first,
            Map<LocalDate, Double> second) {

        Set<LocalDate> commonDates =
                first.keySet()
                        .stream()
                        .filter(second::containsKey)
                        .collect(Collectors.toCollection(TreeSet::new));

        System.out.println("Common dates count between stocks: " + commonDates.size());

        List<Double> firstReturns = new ArrayList<>();
        List<Double> secondReturns = new ArrayList<>();

        for (LocalDate date : commonDates) {
            firstReturns.add(first.get(date));
            secondReturns.add(second.get(date));
        }

        return List.of(firstReturns, secondReturns);
    }

    /** Calculates the weighted beta of the portfolio based on individual stock holdings.
     * @param portfolio the Portfolio to evaluate.
     * @return the calculated weighted beta as a double.
     */
    private static double calculatePortfolioWeightedBeta(Portfolio portfolio) {
        double totalBeta = 0;
        for (StockHolding holding : portfolio.getHoldings()) {
            double holdingShare = portfolio.getHoldingShare(holding);
            double holdingStockBeta = holding.getStock().getBeta();
            double betaContribution = holdingShare * (holdingStockBeta);
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
        Map<LocalDate, Double> portfolioReturns =
                calculatePortfolioReturnsMap(portfolio);

        Map<LocalDate, Double> marketReturns =
                StockFinancialService.returnDateRatiosMap(market);

        List<List<Double>> aligned =
                getAlignedReturns(portfolioReturns, marketReturns);

        List<Double> portfolioRatios = aligned.get(0);
        List<Double> marketRatios = aligned.get(1);

        double covariance =
                StatisticsService.calculateCovariance(
                        portfolioRatios,
                        marketRatios);

        double variance =
                StatisticsService.calculateVariance(marketRatios);

        if (variance == 0) {
            return 0.0;
        }

        return covariance / variance;
    }

    /** Generates a Map of dates and their respective daily portfolio returns from the master timeline.
     * @param portfolio the Portfolio to evaluate.
     * @return a map of local dates and daily returns.
     */
    public static Map<LocalDate, Double> calculatePortfolioReturnsMap(Portfolio portfolio) {
        Map<LocalDate, Double> returns = new TreeMap<>();

        List<LocalDate> timeline = portfolio.getMasterTimeline();

        for (int i = 1; i < timeline.size(); i++) {
            LocalDate date = timeline.get(i);

            Double dailyReturn =
                    portfolio.calculatePortfolioDailyReturn(date);

            if (dailyReturn != null) {
                returns.put(date, dailyReturn);
            }
        }

        return returns;
    }

    /** Calculates the alpha of the portfolio relative to a market stock.
     * @param portfolio the Portfolio to evaluate.
     * @param market the market Stock benchmark.
     * @return the calculated alpha as a double.
     */
    public static double calculatePortfolioAlpha(Portfolio portfolio, Stock market) {
        Map<LocalDate, Double> portfolioReturns =
                calculatePortfolioReturnsMap(portfolio);

        Map<LocalDate, Double> marketReturns =
                StockFinancialService.returnDateRatiosMap(market);

        List<List<Double>> aligned =
                getAlignedReturns(portfolioReturns, marketReturns);

        List<Double> portfolioRatios = aligned.get(0);
        List<Double> marketRatios = aligned.get(1);

        double portfolioReturnsMean = StatisticsService.calculateMean(portfolioRatios);
        double marketMean = StatisticsService.calculateMean(marketRatios);
        return StatisticsService.calculateAlpha(portfolioReturnsMean, marketMean,
                calculatePortfolioTrueBeta(portfolio, market), RISK_FREE_RATE);
    }

    /** Ensures that the portfolio's stock holding's stock objects have had their financial metrics calculated.
     * @param portfolio The portfolio we are checking.
     * @param market The market relative to which they are calculated.
     */
    private static void ensureStockMetrics(Portfolio portfolio, Stock market) {
        for (StockHolding holding : portfolio.getHoldings()) {
            Stock stock = holding.getStock();

            if (!stock.hasCalculatedMetrics()) {
                StockFinancialService.calculateAndAssignMetrics(stock, market);
            }
        }
    }

    /** Ensures that the portfolio has a Master Timeline generated.
     * @param portfolio The portfolio we are checking.
     */
    private static void ensurePortfolioTimeline(Portfolio portfolio) {
        if (portfolio.getMasterTimeline() == null) {
            portfolio.buildMasterTimeline();
        }
    }

    /** Calculates the annualized alpha of the portfolio relative to a market stock.
     * @param portfolio the Portfolio to evaluate.
     * @param market the market Stock benchmark.
     * @return the calculated annualized alpha as a double.
     */
    public static double calculateAnnualizedPortfolioAlpha(Portfolio portfolio, Stock market) {
        double dailyAlpha = calculatePortfolioAlpha(portfolio, market);
        return dailyAlpha * 252.0;
    }

    /** Calculates the annualized Sharpe ratio of the portfolio.
     * @param portfolio the Portfolio to evaluate.
     * @return the annualized Sharpe ratio as a double.
     */
    public static double calculateAnnualizedSharpeRatio(Portfolio portfolio) {
        if (Boolean.TRUE.equals(portfolio.hasCustomViews())) {
            double portfolioDailyVolatility = Math.sqrt(calculatePortfolioVariance(portfolio));
            double portfolioAnnualVolatility = portfolioDailyVolatility * Math.sqrt(252.0);

            double annualPortfolioReturn = calculateCustomPortfolioReturnsNumber(portfolio, portfolio.getCustomViews());
            double annualizedRiskFreeRate = Math.pow(1.0 + RISK_FREE_RATE, 252.0) - 1.0;

            return (portfolioAnnualVolatility == 0) ? 0.0 : (annualPortfolioReturn - annualizedRiskFreeRate) / portfolioAnnualVolatility;
        } else {
            double dailySharpe = calculateSharpeRatio(portfolio);
            return dailySharpe * Math.sqrt(252.0);
        }
    }

    /** Computes and assigns financial metrics (weighted beta, alpha, true beta, Sharpe ratio, annualized alpha, annualized Sharpe ratio) to the portfolio.
     * Automatically branches to custom expected returns if the portfolio has active custom views.
     * @param portfolio the Portfolio to update.
     * @param market the market Stock benchmark.
     */
    public static void calculateAndAssignMetrics(Portfolio portfolio, Stock market) {
        ensurePortfolioTimeline(portfolio);
        ensureStockMetrics(portfolio, market);

        double weightedBeta = calculatePortfolioWeightedBeta(portfolio);
        double alpha = calculatePortfolioAlpha(portfolio, market);
        double trueBeta = calculatePortfolioTrueBeta(portfolio, market);
        double sharpeRatio;

        if (Boolean.TRUE.equals(portfolio.hasCustomViews())) {
            double portfolioStandardDeviation = Math.sqrt(calculatePortfolioVariance(portfolio));
            double portfolioReturns = calculateCustomPortfolioReturnsNumber(portfolio, portfolio.getCustomViews());
            sharpeRatio = (portfolioStandardDeviation == 0) ? 0.0 : (portfolioReturns - RISK_FREE_RATE) / portfolioStandardDeviation;
        } else {
            sharpeRatio = calculateSharpeRatio(portfolio);
        }

        double annualizedAlpha = calculateAnnualizedPortfolioAlpha(portfolio, market);
        double annualizedSharpeRatio = calculateAnnualizedSharpeRatio(portfolio);

        portfolio.setWeightedBeta(weightedBeta);
        portfolio.setAlpha(alpha);
        portfolio.setTrueBeta(trueBeta);
        portfolio.setSharpeRatio(sharpeRatio);
        portfolio.setAnnualizedAlpha(annualizedAlpha);
        portfolio.setAnnualizedSharpeRatio(annualizedSharpeRatio);
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
        return portfolioCovariance.getEntry(0, 0);
    }

    /** Calculates the expected portfolio returns number using historical means.
     * @param portfolio the Portfolio to evaluate.
     * @return the expected returns as a double.
     */
    public static double calculatePortfolioReturnsNumber(Portfolio portfolio) {
        double returns = 0.0;
        List<StockHolding> stockHoldingList = portfolio.getHoldings();
        for (StockHolding stockHolding : stockHoldingList) {
            Stock stock = stockHolding.getStock();
            double stockRatio = StatisticsService.calculateMean(StockFinancialService.returnRatios(stock));
            double holdingWeight = portfolio.getHoldingShare(stockHolding);
            double returnsContribution = stockRatio * holdingWeight;
            returns += returnsContribution;
        }
        return returns;
    }

    /** Calculates expected portfolio returns using custom expected returns with a safe historical fallback.
     * @param portfolio the Portfolio to evaluate.
     * @param customExpectedReturns map of tickers to custom expected returns.
     * @return the custom expected returns number as a double.
     */
    public static double calculateCustomPortfolioReturnsNumber(Portfolio portfolio, Map<String, Double> customExpectedReturns) {
        double returns = 0.0;
        List<StockHolding> stockHoldingList = portfolio.getHoldings();
        for (StockHolding stockHolding : stockHoldingList) {
            Stock stock = stockHolding.getStock();
            String ticker = stock.getTickerSymbol();

            double expectedReturn;
            if (customExpectedReturns != null && customExpectedReturns.containsKey(ticker)) {
                expectedReturn = customExpectedReturns.get(ticker);
            } else {
                expectedReturn = StatisticsService.calculateMean(StockFinancialService.returnRatios(stock));
            }

            double holdingWeight = portfolio.getHoldingShare(stockHolding);
            returns += expectedReturn * holdingWeight;
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
        return (portfolioReturns - RISK_FREE_RATE) / portfolioStandardDeviation;
    }

    /** Calculates the Choueifaty Diversification ratio of the portfolio.
     * @param portfolio the Portfolio to evaluate.
     * @return the Choueifaty Diversification ratio as a double.
     */
    public static double calculateCdr(Portfolio portfolio) {
        List<StockHolding> holdings = portfolio.getHoldings();
        int n = holdings.size();
        double weightVolatilitySum = 0.0;
        double portfolioVariance = calculatePortfolioVariance(portfolio);
        double portfolioStandardDeviation = Math.sqrt(portfolioVariance);

        for (int i = 0; i < n; i++) {
            StockHolding holding = holdings.get(i);
            Stock stock = holding.getStock();
            List<Double> stockRatios = StockFinancialService.returnRatios(stock);
            double variance = StatisticsService.calculateVariance(stockRatios);
            Double standardDeviation = Math.sqrt(variance);
            System.out.println("Standard Deviation " + standardDeviation);
            weightVolatilitySum += portfolio.getHoldingShare(holding) * standardDeviation;
            System.out.println("Weight volatility Sum " + weightVolatilitySum);
        }

        if (portfolioStandardDeviation == 0) {
            return 0.0;
        }

        return weightVolatilitySum / portfolioStandardDeviation;
    }
}