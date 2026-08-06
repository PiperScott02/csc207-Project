package use_case.portfolio_health;

import entity.*;
import use_case.analysis.PortfolioAdviceService;
import use_case.analysis.PortfolioFinancialService;
import use_case.analysis.PortfolioHealthScoringService;
import use_case.stock.StockDataAccessInterface;

/** The Interactor for the Portfolio Health use case, handling the portfolio health score calculations. */
public class PortfolioHealthInteractor implements PortfolioHealthInputBoundary {
    private final StockDataAccessInterface stockDataAccessObject;
    private final PortfolioHealthOutputBoundary portfolioHealthPresenter;

    // Simplified constructor: Only requires stockDataAccessObject and presenter
    public PortfolioHealthInteractor(StockDataAccessInterface stockDataAccessObject,
                                     PortfolioHealthOutputBoundary portfolioHealthPresenter) {
        this.stockDataAccessObject = stockDataAccessObject;
        this.portfolioHealthPresenter = portfolioHealthPresenter;
    }

    @Override
    public void execute(PortfolioHealthInputData portfolioHealthInputData) {
        try {
            User user = portfolioHealthInputData.getUser();
            Portfolio portfolio = user.getPortfolio();

            // Check if the portfolio has any holdings to prevent matrix errors
            if (portfolio.getHoldings() == null || portfolio.getHoldings().isEmpty()) {
                portfolioHealthPresenter.prepareFailView("Your portfolio has no holdings. Please add a holding first.");
                return;
            }

            RiskProfile riskProfile = user.getRiskProfile();
            String riskPreference = (riskProfile != null) ? riskProfile.getRiskLevel().toString() : "Unknown";

            Stock marketStock = stockDataAccessObject.get("SPY");

            PortfolioFinancialService.calculateAndAssignMetrics(portfolio, marketStock);

            // Calculate quantitative sub-scores using the scoring service
            Double sharpeScore = PortfolioHealthScoringService.calculateSharpeScore(portfolio.getAnnualizedSharpeRatio());
            Double riskScore = PortfolioHealthScoringService.calculateRiskAlignmentScore(portfolio.getTrueBeta(), riskProfile);
            Double cdr = PortfolioFinancialService.calculateCdr(portfolio);
            Double divScore = PortfolioHealthScoringService.calculateDiversificationScore(cdr);
            Double newsScore = PortfolioHealthScoringService.calculateNewsScore();

            System.out.println("CDR " + cdr);
            Double portfolioHealthScore = sharpeScore + riskScore + divScore + newsScore;
            String portfolioHealthScoreString = portfolioHealthScore.toString();

            // Generate bracket-specific advice using the sub-scores directly
            String sharpeAdvice = PortfolioAdviceService.getSharpeAdvice(sharpeScore);
            String riskAlignmentAdvice = PortfolioAdviceService.getRiskAlignmentAdvice(riskScore, riskProfile);
            String diversificationAdvice = PortfolioAdviceService.getDiversificationAdvice(divScore);
            String newsAdvice = PortfolioAdviceService.getNewsAdvice();

            PortfolioHealthOutputData portfolioHealthOutputData = new PortfolioHealthOutputData(
                    riskPreference,
                    portfolioHealthScoreString,
                    portfolio.getTrueBeta() != null ? portfolio.getTrueBeta().toString() : "0.0",
                    portfolio.getAnnualizedAlpha() != null ? portfolio.getAnnualizedAlpha().toString() : "0.0",
                    portfolio.getAnnualizedSharpeRatio() != null ? portfolio.getAnnualizedSharpeRatio().toString() : "0.0",
                    sharpeAdvice,
                    riskAlignmentAdvice,
                    diversificationAdvice,
                    newsAdvice,
                    false
            );
            portfolioHealthPresenter.prepareSuccessView(portfolioHealthOutputData);

        } catch (Exception e) {
            e.printStackTrace();
            portfolioHealthPresenter.
                    prepareFailView("Failed to calculate portfolio health: " + e.getMessage());
        }
    }
}