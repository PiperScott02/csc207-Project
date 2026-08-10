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

    /** The coefficient of risk aversion ($\delta$) used in equilibrium return calculations. */
    public final double RISK_AVERSION_COEFFICIENT = 2.5;

    /** The scaling factor ($\tau$) representing the uncertainty of the prior market estimate. */
    public final double DEFAULT_TAU = 0.05;

    /**
     * Computes the market weight caps (market capitalization proportions) for each asset
     * within the asset universe derived from the given holdings.
     *
     * @param holdings the list of stock holdings comprising the portfolio universe
     * @return a map matching each ticker symbol to its proportional market capitalization weight
     */
    public Map<String, Double> computeMarketWeightCaps(List<StockHolding> holdings) {
        Map<String, Double> marketWeightCaps = new HashMap<>();
        AssetUniverseService universe = new AssetUniverseService(holdings);

        // Map to store individual stock market caps (Price * Shares Outstanding)
        Map<String, BigDecimal> stockMarketCaps = new HashMap<>();
        BigDecimal totalMarketCap = BigDecimal.ZERO;

        for (Stock stock : universe.getStocks()) {
            if (stock != null && stock.getClose() != null && stock.getSharesOutstanding() != null) {
                BigDecimal shares = BigDecimal.valueOf(stock.getSharesOutstanding());
                BigDecimal marketCap = stock.getClose().multiply(shares);

                stockMarketCaps.put(stock.getTickerSymbol(), marketCap);
                totalMarketCap = totalMarketCap.add(marketCap);
            }
        }

        if (totalMarketCap.compareTo(BigDecimal.ZERO) == 0) {
            return marketWeightCaps;
        }

        // Compute the true market weight cap for each stock
        for (Stock stock : universe.getStocks()) {
            String ticker = stock.getTickerSymbol();
            BigDecimal marketCap = stockMarketCaps.getOrDefault(ticker, BigDecimal.ZERO);

            double marketWeight = marketCap
                    .divide(totalMarketCap, 12, RoundingMode.HALF_UP)
                    .doubleValue();

            marketWeightCaps.put(ticker, marketWeight);
        }

        return marketWeightCaps;
    }

    /**
     * Computes the implied equilibrium expected return vector ($\Pi$) based on market weights
     * and the asset covariance matrix.
     *
     * @param holdings the list of stock holdings defining the asset universe
     * @return a RealMatrix representing the daily implied equilibrium expected returns
     */
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
        System.out.println("Stock Covariance: " + covarianceMatrix.getEntry(0,0));
        return covarianceMatrix.multiply(weightsMatrix).scalarMultiply(RISK_AVERSION_COEFFICIENT);
    }

    /**
     * Constructs the pick matrix ($P$) that maps the user's views to the assets in the universe.
     *
     * @param holdings the list of stock holdings defining the asset universe
     * @param viewTickers the list of ticker symbols that have active user views
     * @return a RealMatrix linking individual views to their respective stock indices
     */
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

    /**
     * Constructs the diagonal uncertainty matrix ($\Omega$) for the user views based on
     * specified confidence levels and asset variances.
     *
     * @param holdings the list of stock holdings defining the asset universe
     * @param viewTickers the list of ticker symbols with active views
     * @param confidenceLevels a map of ticker symbols to qualitative confidence levels (e.g., "Low", "Medium", "High")
     * @param covarianceMatrix the aligned covariance matrix of asset returns
     * @return a diagonal RealMatrix representing the variance of the error terms in the user views
     */
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

            double calculatedOmega = (1.0 - confidence) * DEFAULT_TAU * assetVariance;

            // Enforce a minimum uncertainty floor to prevent division-by-zero / extreme weighting
            omegaArray[i][i] = Math.max(calculatedOmega, 1e-6);
        }

        return new Array2DRowRealMatrix(omegaArray);
    }

    /**
     * Converts annual percentage user views into daily compounding decimal rate expectations ($Q$).
     *
     * @param userViews a map of ticker symbols to expected annual percentage returns
     * @param viewTickers the list of ordered ticker symbols corresponding to the views
     * @return a RealMatrix containing the daily expected return values for each view
     */
    public RealMatrix userViews(Map<String, Double> userViews, List<String> viewTickers) {
        int numViews = viewTickers.size();
        double[][] qArray = new double[numViews][1];

        for (int i = 0; i < numViews; i++) {
            String ticker = viewTickers.get(i);

            double annualReturnDecimal = userViews.getOrDefault(ticker, 0.0);
            double dailyReturnDecimal = Math.pow(1.0 + annualReturnDecimal, 1.0 / 252.0) - 1.0;

            qArray[i][0] = dailyReturnDecimal;
        }

        return new Array2DRowRealMatrix(qArray);
    }

    /**
     * Placeholder method to compute market estimate returns for a given user.
     *
     * @param user the user context for estimation
     * @return an empty map reserved for future implementation
     */
    public Map<String, Double> computeMarketEstimateReturns(User user) {
        return new HashMap<>();
    }

    /**
     * Builds and aligns the covariance matrix for all stocks in the portfolio universe.
     *
     * @param holdings the list of stock holdings defining the asset universe
     * @return a RealMatrix containing the covariance values across all universe assets
     */
    public RealMatrix buildAlignedCovarianceMatrix(List<StockHolding> holdings) {
        AssetUniverseService universe = new AssetUniverseService(holdings);
        List<Stock> orderedStocks = universe.getStocks();

        double[][] covariancesArray = StockFinancialService.buildCovariancesArray(orderedStocks);
        return StockFinancialService.buildCovarianceMatrix(covariancesArray);
    }

    /**
     * Computes the final Black-Litterman adjusted expected annual returns by blending
     * market equilibrium priors with active user views and confidence weights.
     *
     * @param holdings the list of stock holdings defining the asset universe
     * @param userViewsMap a map of ticker symbols to expected annual percentage returns
     * @param confidenceLevels a map of ticker symbols to confidence designations
     * @return a map of ticker symbols to their newly blended, annualized expected returns
     */
    public Map<String, Double> computeAdjustedReturns(List<StockHolding> holdings,
                                                      Map<String, Double> userViewsMap,
                                                      Map<String, String> confidenceLevels) {
        Map<String, Double> adjustedReturns = new HashMap<>();

        AssetUniverseService universe = new AssetUniverseService(holdings);
        List<Stock> orderedStocks = universe.getStocks();

        // Filter out any ticker that has "None" or missing confidence
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

        // Annualize final expected returns for each stock in universe using daily compounding
        for (int i = 0; i < orderedStocks.size(); i++) {
            String ticker = orderedStocks.get(i).getTickerSymbol();
            double dailyReturn = blReturnsDaily.getEntry(i, 0);

            double annualReturn = Math.pow(1.0 + dailyReturn, 252.0) - 1.0;
            adjustedReturns.put(ticker, annualReturn);
        }

        return adjustedReturns;
    }
}