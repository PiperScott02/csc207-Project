import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class PortfolioFinancialService {
    private static double threemonthusbillreturn = 0.0379;

    private static final double riskFreeRate = Math.pow(1+threemonthusbillreturn, 1.0/252.0) - 1.0;

    private static double calculatePortfolioWeightedBeta(Portfolio portfolio) {
        BigDecimal totalBeta = BigDecimal.ZERO;
        for (StockHolding holding : portfolio.getHoldings()) {
            BigDecimal holdingShare = portfolio.getHoldingShare(holding);
            BigDecimal holdingStockBeta = BigDecimal.valueOf(holding.getStock().getBeta());
            BigDecimal betaContribution = holdingShare.multiply(holdingStockBeta);
            totalBeta = totalBeta.add(betaContribution);
        }
        return totalBeta.doubleValue();
    }

    private static double calculatePortfolioTrueBeta(Portfolio portfolio, Stock market) {
        List<Double> portfolioReturns = calculatePortfolioReturns(portfolio);
        List<Double> marketReturns = StockFinancialService.returnRatios(market);
        Double portfolioCovariance = StatisticsService.calculateCovariance(portfolioReturns, marketReturns);
        Double marketVariance = StatisticsService.calculateVariance(marketReturns);
        return portfolioCovariance/marketVariance;
    }

    public static List<Double> calculatePortfolioReturns(Portfolio portfolio) {
        List<Double> returns = new ArrayList<>();
        List<LocalDate> timeline = portfolio.getMasterTimeline();
        for (int i = 1; i < timeline.size(); i++) {
            returns.add(portfolio.calculatePortfolioDailyReturn(timeline.get(i)));
        }
        return returns;


    }

    public static double calculatePortfolioAlpha(Portfolio portfolio, Stock market) {
        List<Double> returns = calculatePortfolioReturns(portfolio);
        double portfolioReturnsMean = StatisticsService.calculateMean(returns);
        List<Double> marketRatios = StockFinancialService.returnRatios(market);
        double portfolioCovariance = StatisticsService.calculateCovariance(returns, marketRatios);
        double marketMean = StatisticsService.calculateMean(marketRatios);
        return StatisticsService.calculateAlpha(portfolioReturnsMean, marketMean,
                calculatePortfolioTrueBeta(portfolio, market), riskFreeRate);
    }

    public static void calculateAndAssignMetrics(Portfolio portfolio, Stock market) {
        double weightedBeta = calculatePortfolioWeightedBeta(portfolio);

        double alpha = calculatePortfolioAlpha(portfolio, market);

        double trueBeta = calculatePortfolioTrueBeta(portfolio, market);

        /*double sharpeRatio = calculateSharpeRatio(stock);*/

        portfolio.setWeightedBeta(weightedBeta);

        portfolio.setAlpha(alpha);

        portfolio.setTrueBeta(trueBeta);

        /*stock.setSharpeRatio(sharpeRatio)*/;


    }
}



