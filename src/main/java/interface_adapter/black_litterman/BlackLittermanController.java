package interface_adapter.black_litterman;

import entity.User;
import use_case.black_litterman.BlackLittermanInputBoundary;
import use_case.black_litterman.BlackLittermanInputData;

import java.util.Map;

/**
 * The Controller for the Black-Litterman use case, handling user input and triggering the interactor.
 */
public class BlackLittermanController {
    private final BlackLittermanInputBoundary blackLittermanUseCaseInteractor;

    public BlackLittermanController(BlackLittermanInputBoundary blackLittermanInputBoundary) {
        this.blackLittermanUseCaseInteractor = blackLittermanInputBoundary;
    }

    /**
     * Executes the initial load to populate top tickers and market equilibrium returns.
     *
     * @param user the current user
     */
    public void loadMarketData(User user) {
        // Pass empty views/confidence levels to fetch base market returns and top tickers
        final BlackLittermanInputData inputData = new BlackLittermanInputData(user, Map.of(), Map.of());
        blackLittermanUseCaseInteractor.execute(inputData);
    }

    /**
     * Executes the Black-Litterman calculation with user-specified opinions and confidence levels.
     *
     * @param user             the current user
     * @param userViews        map of tickers to expected return opinions
     * @param confidenceLevels map of tickers to confidence level strings
     */
    public void execute(User user, Map<String, Double> userViews, Map<String, String> confidenceLevels) {
        final BlackLittermanInputData inputData = new BlackLittermanInputData(user, userViews, confidenceLevels);
        blackLittermanUseCaseInteractor.execute(inputData);
    }
}