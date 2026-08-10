package use_case.stress_test;

import entity.Portfolio;
import entity.StockHolding;
import entity.StressScenario;
import entity.User;
import interface_adapter.logged_in.LoggedInViewModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class StressTestInteractor implements StressTestInputBoundary {
    private final StressTestOutputBoundary outputBoundary;
    private final LoggedInViewModel loggedInViewModel;

    public StressTestInteractor(StressTestOutputBoundary outputBoundary,
                                LoggedInViewModel loggedInViewModel) {
        this.outputBoundary = outputBoundary;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void execute(StressTestInputData inputData) {
        // 1. Check for active user session (just like AddHoldingInteractor)
        if (loggedInViewModel.getState() == null || loggedInViewModel.getState().getUser() == null) {
            outputBoundary.prepareFailView("No active user session found.");
            return;
        }

        User currentUser = loggedInViewModel.getState().getUser();
        Portfolio portfolio = currentUser.getPortfolio();
        List<StockHolding> holdings = portfolio.getHoldings();

        StressScenario scenario = inputData.getScenario();

        // 2. Calculate current total value across all holdings
        BigDecimal totalCurrent = BigDecimal.ZERO;
        for (StockHolding holding : holdings) {
            BigDecimal holdingValue = holding.calculateTotalValue(); // Returns BigDecimal
            if (holdingValue != null) {
                totalCurrent = totalCurrent.add(holdingValue);
            }
        }

        // Handle empty portfolio edge case cleanly
        if (totalCurrent.compareTo(BigDecimal.ZERO) == 0) {
            outputBoundary.prepareFailView("Your portfolio has no holdings to stress test.");
            return;
        }

        // 3. Apply shock percentage (e.g., multiplier = 1 + (-0.34) = 0.66 for COVID-19)
        BigDecimal multiplier = BigDecimal.ONE.add(scenario.getShockPercentage());
        BigDecimal totalStressed = totalCurrent.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        BigDecimal loss = totalStressed.subtract(totalCurrent);

        // 4. Package data and return success view
        StressTestOutputData outputData = new StressTestOutputData(
                scenario.getName(),
                totalCurrent,
                totalStressed,
                loss,
                scenario.getShockPercentage().multiply(BigDecimal.valueOf(100))
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}