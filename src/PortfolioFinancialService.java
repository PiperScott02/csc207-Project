import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class PortfolioFinancialService {
    private double threemonthusbillreturn = 0.0379;

    private final double riskFreeRate = Math.pow(1+threemonthusbillreturn, 1.0/252.0) - 1.0;

    private double calculatePortfolioWeightedBeta(Portfolio portfolio) {
        BigDecimal totalBeta = BigDecimal.ZERO;
        for (StockHolding holding : portfolio.getHoldings()) {
            BigDecimal holdingShare = portfolio.getHoldingShare(holding);
            BigDecimal holdingStockBeta = BigDecimal.valueOf(holding.getStock().getBeta());
            BigDecimal betaContribution = holdingShare.multiply(holdingStockBeta);
            totalBeta = totalBeta.add(betaContribution);
        }
        return totalBeta.doubleValue();
    }

    private double calculatePortfolioTrueBeta(Portfolio portfolio, Stock market) {
        List<Double> portfolioReturns = calculatePortfolioReturns(portfolio);
        List<Double> marketReturns = StockFinancialService.returnRatios(market);
        Double portfolioCovariance = StatisticsService.calculateCovariance(portfolioReturns, marketReturns);
        Double marketVariance = StatisticsService.calculateVariance(marketReturns);
        return portfolioCovariance/marketVariance;
    }

    public List<Double> calculatePortfolioReturns(Portfolio portfolio) {
        List<Double> returns = new ArrayList<>();
        List<LocalDate> timeline = portfolio.getMasterTimeline();
        for (int i = 1; i < timeline.size(); i++) {
            returns.add(portfolio.calculatePortfolioDailyReturn(timeline.get(i)));
        }
        return returns;


    }

    public double calculatePortfolioAlpha(Portfolio portfolio, Stock market) {
        List<Double> returns = calculatePortfolioReturns(portfolio);
        double portfolioReturnsMean = StatisticsService.calculateMean(returns);
        List<Double> marketRatios = StockFinancialService.returnRatios(market);
        double portfolioCovariance = StatisticsService.calculateCovariance(returns, marketRatios);
        double marketMean = StatisticsService.calculateMean(marketRatios);
        return StatisticsService.calculateAlpha(portfolioReturnsMean, marketMean,
                calculatePortfolioTrueBeta(portfolio, market), this.riskFreeRate);
    }
}



