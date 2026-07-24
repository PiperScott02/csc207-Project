package interface_adapter.portfolio_health;

import entity.User;
import use_case.portfolio_health.PortfolioHealthInputBoundary;
import use_case.portfolio_health.PortfolioHealthInputData;

public class PortfolioHealthController {
    private final PortfolioHealthInputBoundary portfolioHealthUseCaseInteractor;

    public PortfolioHealthController(PortfolioHealthInputBoundary portfolioHealthInputBoundary) {
        this.portfolioHealthUseCaseInteractor = portfolioHealthInputBoundary;
    }

    /** Executes the Portfolio Health Use Case.
     * @param user the user of the portfolio to be displayed.
     */
    public void execute(User user) {
        final PortfolioHealthInputData portfolioHealthInputData = new PortfolioHealthInputData(user);

        portfolioHealthUseCaseInteractor.execute(portfolioHealthInputData);
    }
}