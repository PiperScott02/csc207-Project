package use_case.black_litterman;

import entity.Stock;
import entity.StockHolding;
import entity.User;
import org.apache.commons.math3.linear.RealMatrix;
import use_case.analysis.AssetUniverseService;
import use_case.analysis.BlackLittermanService;

import java.util.*;

/** The Interactor for the Black-Litterman use case, handling equilibrium returns and adjustments. */
public class BlackLittermanInteractor implements BlackLittermanInputBoundary {
    private final BlackLittermanDataAccessInterface blackLittermanDataAccessObject;
    private final BlackLittermanService blackLittermanService;
    private final BlackLittermanOutputBoundary blackLittermanPresenter;

    public BlackLittermanInteractor(BlackLittermanDataAccessInterface blackLittermanDataAccessObject,
                                    BlackLittermanService blackLittermanService,
                                    BlackLittermanOutputBoundary blackLittermanPresenter) {
        this.blackLittermanDataAccessObject = blackLittermanDataAccessObject;
        this.blackLittermanService = blackLittermanService;
        this.blackLittermanPresenter = blackLittermanPresenter;
    }

    @Override
    public void execute(BlackLittermanInputData inputData) {
        try {
            User user = inputData.getUser();
            if (user == null || user.getPortfolio() == null) {
                blackLittermanPresenter.prepareFailView("User or portfolio cannot be null.");
                return;
            }

            List<StockHolding> holdings = user.getPortfolio().getHoldings();

            // 1. Compute market weight caps and determine top 5 heavily weighted stocks
            Map<String, Double> marketWeightCaps = blackLittermanService.computeMarketWeightCaps(holdings);
            List<Map.Entry<String, Double>> sortedWeights = new ArrayList<>(marketWeightCaps.entrySet());
            sortedWeights.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            List<String> topTickers = new ArrayList<>();
            for (int i = 0; i < Math.min(5, sortedWeights.size()); i++) {
                topTickers.add(sortedWeights.get(i).getKey());
            }

            // 2. Compute implied equilibrium returns (Daily Market Returns Matrix)
            RealMatrix piMatrix = blackLittermanService.impliedEquilibriumExpectedReturn(holdings);

            // Match exact ticker ordering using AssetUniverseService
            AssetUniverseService universe = new AssetUniverseService(holdings);
            List<Stock> orderedStocks = universe.getStocks();

            Map<String, Double> marketReturns = new HashMap<>();
            for (int i = 0; i < orderedStocks.size(); i++) {
                if (i < piMatrix.getRowDimension()) {
                    String ticker = orderedStocks.get(i).getTickerSymbol();
                    double dailyReturn = piMatrix.getEntry(i, 0);

                    // Annualize daily market return geometrically so it matches adjusted returns
                    double annualReturn = Math.pow(1.0 + dailyReturn, 252.0) - 1.0;
                    marketReturns.put(ticker, annualReturn);
                }
            }

            // 3. Compute adjusted returns if user views are provided
            Map<String, Double> adjustedReturns = new HashMap<>();
            if (inputData.getUserViews() != null && !inputData.getUserViews().isEmpty()) {
                adjustedReturns = blackLittermanService.computeAdjustedReturns(
                        holdings,
                        inputData.getUserViews(),
                        inputData.getConfidenceLevels()
                );
            }

            // 4. Package into output data
            BlackLittermanOutputData outputData = new BlackLittermanOutputData(
                    user,
                    topTickers,
                    marketReturns,
                    adjustedReturns,
                    false
            );

            blackLittermanPresenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            blackLittermanPresenter.prepareFailView("Failed to process Black-Litterman model: " + e.getMessage());
        }
    }
}