package use_case.portfolio_health;

import entity.*;
import use_case.analysis.PortfolioFinancialService;
import use_case.stock.StockDataAccessInterface;

/** The Interactor for the Portfolio Health use case, handling the portfolio health score calculations. */
public class PortfolioHealthInteractor implements PortfolioHealthInputBoundary {
    private final PortfolioHealthDataAccessInterface portfolioHealthDataAccessObject;
    private final StockDataAccessInterface stockDataAccessObject;
    private final PortfolioHealthOutputBoundary portfolioHealthPresenter;

    public PortfolioHealthInteractor(PortfolioHealthDataAccessInterface portfolioHealthDataAccessObject,
                                     StockDataAccessInterface stockDataAccessObject,
                                     PortfolioHealthOutputBoundary portfolioHealthPresenter) {
        this.portfolioHealthDataAccessObject = portfolioHealthDataAccessObject;
        this.stockDataAccessObject = stockDataAccessObject;
        this.portfolioHealthPresenter = portfolioHealthPresenter;
    }

    @Override
    public void execute(PortfolioHealthInputData portfolioHealthInputData) {
        try {

            User user = portfolioHealthInputData.getUser();

            RiskProfile riskProfile = user.getRiskProfile();

            String riskPreference = (riskProfile != null) ? riskProfile.getRiskLevel().toString() : "Unknown";

            Portfolio portfolio = user.getPortfolio();

            Stock marketStock = stockDataAccessObject.get("SPY");

            PortfolioFinancialService.calculateAndAssignMetrics(portfolio, marketStock);

            System.out.println("Interactor");

            System.out.println("Beta: " + portfolio.getTrueBeta());
            System.out.println("Alpha: " + portfolio.getAlpha());
            System.out.println("Sharpe: " + portfolio.getSharpeRatio());

            /* Eventually will replace with an actual formula*/
            String portfolioHealthScore = "85";

            PortfolioHealthOutputData portfolioHealthOutputData = new PortfolioHealthOutputData(
                    riskPreference,
                    portfolioHealthScore,
                    portfolio.getTrueBeta().toString(),
                    portfolio.getAlpha().toString(),
                    portfolio.getSharpeRatio().toString(),
                    false
            );
            portfolioHealthPresenter.prepareSuccessView(portfolioHealthOutputData);

        } catch (Exception e) {
            e.printStackTrace(); //
            portfolioHealthPresenter.
                    prepareFailView("Failed to calculate portfolio health: " + e.getMessage());
        }
    }
}