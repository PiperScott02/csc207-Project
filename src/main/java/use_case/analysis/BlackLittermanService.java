package use_case.analysis;

import entity.Stock;
import entity.StockHolding;
import entity.User;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Service responsible for executing the Black-Litterman model calculations,
 * blending market equilibrium returns with user-defined views and confidence levels.
 */
public class BlackLittermanService {

    public final double RISK_AVERSION_COEFFICIENT = 2.5;
    public final double DEFAULT_TAU = 0.05;

    public Map<String, Double> computeMarketWeightCaps(List<StockHolding> holdings) {
        Map<String, Double> marketWeightCaps = new HashMap<>();
        AssetUniverseService universe = new AssetUniverseService(holdings);
        BigDecimal totalValue = BigDecimal.ZERO;

        for (StockHolding holding : holdings) {
            totalValue = totalValue.add(holding.calculateTotalValue());
        }

        if (totalValue.compareTo(BigDecimal.ZERO) == 0) {
            return marketWeightCaps;
        }

        Map<String, BigDecimal> holdingValues = new HashMap<>();
        for (StockHolding holding : holdings) {
            if (holding.getStock() != null) {
                holdingValues.put(holding.getStock().getTickerSymbol(), holding.calculateTotalValue());
            }
        }

        for (Stock stock : universe.getStocks()) {
            String ticker = stock.getTickerSymbol();
            BigDecimal holdingValue = holdingValues.getOrDefault(ticker, BigDecimal.ZERO);

            double marketCap = holdingValue
                    .divide(totalValue, 12, RoundingMode.HALF_UP)
                    .doubleValue();

            marketWeightCaps.put(ticker, marketCap);
        }

        return marketWeightCaps;
    }

    public RealMatrix impliedEquilibriumExpectedReturn(List<StockHolding> holdings) {
        Map<String, Double> marketWeightCaps = computeMarketWeightCaps(holdings);

        AssetUniverseService universe = new AssetUniverseService(holdings);
        RealMatrix covarianceMatrix = buildAlignedCovarianceMatrix(holdings);

        int n = universe.size();
        double[][] weightsArray = new double[n][1];

        List<Stock> orderedStocks = universe.getStocks();
        for (int i = 0; i < n; i++) {
            String ticker = orderedStocks.get(i).getTickerSymbol();
            Double weight = marketWeightCaps.getOrDefault(ticker, 0.0);
            weightsArray[i][0] = weight;
        }

        RealMatrix weightsMatrix = new Array2DRowRealMatrix(weightsArray);
        return covarianceMatrix.multiply(weightsMatrix).scalarMultiply(RISK_AVERSION_COEFFICIENT);
    }

    public RealMatrix pickMatrix(List<StockHolding> holdings, List<String> viewTickers) {
        AssetUniverseService universe = new AssetUniverseService(holdings);
        int numViews = viewTickers.size();
        int numStocks = universe.size();

        double[][] pArray = new double[numViews][numStocks];

        for (int i = 0; i < numViews; i++) {
            String targetTicker = viewTickers.get(i);
            int stockIndex = universe.indexOf(targetTicker);
            if (stockIndex != -1) {
                pArray[i][stockIndex] = 1.0;
            }
        }

        return new Array2DRowRealMatrix(pArray);
    }

    public RealMatrix omegaMatrix(List<StockHolding> holdings,
                                  List<String> viewTickers,
                                  Map<String, String> confidenceLevels,
                                  RealMatrix covarianceMatrix) {

        AssetUniverseService universe = new AssetUniverseService(holdings);
        int numViews = viewTickers.size();
        double[][] omegaArray = new double[numViews][numViews];

        Map<String, Double> confidenceMap = Map.of(
                "Low", 0.25,
                "Medium", 0.50,
                "High", 0.75,
                "Very High", 0.90
        );

        for (int i = 0; i < numViews; i++) {
            String ticker = viewTickers.get(i);
            String level = confidenceLevels.getOrDefault(ticker, "Medium");

            double confidence = confidenceMap.getOrDefault(level, 0.50);

            int stockIndex = universe.indexOf(ticker);
            double assetVariance = covarianceMatrix.getEntry(stockIndex, stockIndex);

            omegaArray[i][i] = (1.0 - confidence) * DEFAULT_TAU * assetVariance;
        }

        return new Array2DRowRealMatrix(omegaArray);
    }

    public RealMatrix userViews(Map<String, Double> userViews, List<String> viewTickers) {
        int numViews = viewTickers.size();
        double[][] qArray = new double[numViews][1];

        for (int i = 0; i < numViews; i++) {
            String ticker = viewTickers.get(i);

            double annualReturnDecimal = userViews.getOrDefault(ticker, 0.0) / 100.0;
            double dailyReturnDecimal = Math.pow(1.0 + annualReturnDecimal, 1.0 / 252.0) - 1.0;

            qArray[i][0] = dailyReturnDecimal;
        }

        return new Array2DRowRealMatrix(qArray);
    }

    public Map<String, Double> computeMarketEstimateReturns(User user) {
        return new HashMap<>();
    }

    public RealMatrix buildAlignedCovarianceMatrix(List<StockHolding> holdings) {
        AssetUniverseService universe = new AssetUniverseService(holdings);
        List<Stock> orderedStocks = universe.getStocks();

        double[][] covariancesArray = StockFinancialService.buildCovariancesArray(orderedStocks);
        return StockFinancialService.buildCovarianceMatrix(covariancesArray);
    }

    public Map<String, Double> computeAdjustedReturns(List<StockHolding> holdings,
                                                      Map<String, Double> userViewsMap,
                                                      Map<String, String> confidenceLevels) {
        Map<String, Double> adjustedReturns = new HashMap<>();

        AssetUniverseService universe = new AssetUniverseService(holdings);
        List<Stock> orderedStocks = universe.getStocks();

        // FIX: Filter out any ticker that has "None" or missing confidence
        List<String> activeViewTickers = new ArrayList<>();
        for (String ticker : userViewsMap.keySet()) {
            String confidence = confidenceLevels.getOrDefault(ticker, "None");
            if (!"None".equalsIgnoreCase(confidence)) {
                activeViewTickers.add(ticker);
            }
        }
        Collections.sort(activeViewTickers);

        RealMatrix pi = impliedEquilibriumExpectedReturn(holdings);

        // If no active views exist (all are "None"), return the market equilibrium returns as adjusted returns
        if (activeViewTickers.isEmpty()) {
            for (int i = 0; i < orderedStocks.size(); i++) {
                String ticker = orderedStocks.get(i).getTickerSymbol();
                double dailyReturn = pi.getEntry(i, 0);
                double annualReturn = Math.pow(1.0 + dailyReturn, 252.0) - 1.0;
                adjustedReturns.put(ticker, annualReturn);
            }
            return adjustedReturns;
        }

        RealMatrix sigma = buildAlignedCovarianceMatrix(holdings);
        RealMatrix p = pickMatrix(holdings, activeViewTickers);
        RealMatrix omega = omegaMatrix(holdings, activeViewTickers, confidenceLevels, sigma);
        RealMatrix q = userViews(userViewsMap, activeViewTickers);

        // Black-Litterman Matrix Calculations
        RealMatrix tauSigma = sigma.scalarMultiply(DEFAULT_TAU);
        RealMatrix tauSigmaInv = new LUDecomposition(tauSigma).getSolver().getInverse();
        RealMatrix omegaInv = new LUDecomposition(omega).getSolver().getInverse();
        RealMatrix pTranspose = p.transpose();

        RealMatrix middleTerm = pTranspose.multiply(omegaInv).multiply(p);
        RealMatrix bracketLeft = tauSigmaInv.add(middleTerm);

        RealMatrix mainInverse = new LUDecomposition(bracketLeft).getSolver().getInverse();

        RealMatrix termA = tauSigmaInv.multiply(pi);
        RealMatrix termB = pTranspose.multiply(omegaInv).multiply(q);
        RealMatrix bracketRight = termA.add(termB);

        RealMatrix blReturnsDaily = mainInverse.multiply(bracketRight);

        // Annualize final expected returns for each stock in universe
        for (int i = 0; i < orderedStocks.size(); i++) {
            String ticker = orderedStocks.get(i).getTickerSymbol();
            double dailyReturn = blReturnsDaily.getEntry(i, 0);

            double annualReturn = Math.pow(1.0 + dailyReturn, 252.0) - 1.0;
            adjustedReturns.put(ticker, annualReturn);
        }

        return adjustedReturns;
    }
}