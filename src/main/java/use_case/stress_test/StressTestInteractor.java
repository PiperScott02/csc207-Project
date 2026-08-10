package use_case.stress_test;

import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import entity.StressScenario;
import entity.User;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.TickerSearchDataAccessInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class StressTestInteractor implements StressTestInputBoundary {
    private final StressTestOutputBoundary outputBoundary;
    private final LoggedInViewModel loggedInViewModel;
    private final TickerSearchDataAccessInterface tickerSearchDataAccessObject;

    public StressTestInteractor(StressTestOutputBoundary outputBoundary,
                                LoggedInViewModel loggedInViewModel,
                                TickerSearchDataAccessInterface tickerSearchDataAccessObject) {
        this.outputBoundary = outputBoundary;
        this.loggedInViewModel = loggedInViewModel;
        this.tickerSearchDataAccessObject = tickerSearchDataAccessObject;
    }

    @Override
    public void execute(StressTestInputData inputData) {
        if (loggedInViewModel.getState() == null || loggedInViewModel.getState().getUser() == null) {
            outputBoundary.prepareFailView("No active user session found.");
            return;
        }

        User currentUser = loggedInViewModel.getState().getUser();
        Portfolio portfolio = currentUser.getPortfolio();
        List<StockHolding> holdings = portfolio.getHoldings();

        if (holdings == null || holdings.isEmpty()) {
            outputBoundary.prepareFailView("Stress test is not possible: portfolio is empty.");
            return;
        }

        StressScenario scenario = inputData.getScenario();
        BigDecimal shock = scenario.getShockPercentage();
        BigDecimal multiplier = BigDecimal.ONE.add(shock);

        BigDecimal totalCurrent = BigDecimal.ZERO;
        BigDecimal totalStressed = BigDecimal.ZERO;

        List<String> tickers = new ArrayList<>();
        List<String> sectors = new ArrayList<>();
        List<BigDecimal> currentPrices = new ArrayList<>();
        List<BigDecimal> stressedPrices = new ArrayList<>();
        List<BigDecimal> currentValues = new ArrayList<>();
        List<BigDecimal> estimatedLosses = new ArrayList<>();

        for (StockHolding holding : holdings) {
            Stock holdingStock = holding.getStock();
            String ticker = (holdingStock != null && holdingStock.getTickerSymbol() != null)
                    ? holdingStock.getTickerSymbol()
                    : "UNKNOWN";

            // Fetch the fully populated stock from API via TickerSearchDataAccessInterface (just like search page)
            String sector = "Unknown";
            try {
                Stock apiStock = tickerSearchDataAccessObject.createBasicStock(ticker);
                if (apiStock != null && apiStock.getIndustry() != null && !apiStock.getIndustry().isBlank()) {
                    sector = apiStock.getIndustry();
                } else if (holdingStock != null && holdingStock.getIndustry() != null) {
                    sector = holdingStock.getIndustry();
                }
            } catch (Exception e) {
                if (holdingStock != null && holdingStock.getIndustry() != null) {
                    sector = holdingStock.getIndustry();
                }
            }

            BigDecimal currentPrice = (holdingStock != null && holdingStock.getClose() != null) ? holdingStock.getClose() : BigDecimal.ZERO;
            BigDecimal quantity = BigDecimal.valueOf(holding.getNumberOfShares());

            BigDecimal holdingCurrentVal = holding.calculateTotalValue();
            BigDecimal holdingStressedPrice = currentPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
            BigDecimal holdingStressedVal = holdingStressedPrice.multiply(quantity);
            BigDecimal holdingLoss = holdingStressedVal.subtract(holdingCurrentVal);

            totalCurrent = totalCurrent.add(holdingCurrentVal);
            totalStressed = totalStressed.add(holdingStressedVal);

            tickers.add(ticker);
            sectors.add(sector);
            currentPrices.add(currentPrice);
            stressedPrices.add(holdingStressedPrice);
            currentValues.add(holdingCurrentVal);
            estimatedLosses.add(holdingLoss);
        }

        if (totalCurrent.compareTo(BigDecimal.ZERO) == 0) {
            outputBoundary.prepareFailView("Stress test is not possible: portfolio is empty.");
            return;
        }

        BigDecimal totalLoss = totalStressed.subtract(totalCurrent);

        StressTestOutputData outputData = new StressTestOutputData(
                scenario.getName(),
                totalCurrent,
                totalStressed,
                totalLoss,
                shock.multiply(BigDecimal.valueOf(100)),
                tickers,
                sectors,
                currentPrices,
                stressedPrices,
                currentValues,
                estimatedLosses
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}