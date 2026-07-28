package use_case.black_litterman;

import entity.User;

import java.util.List;
import java.util.Map;

/** The output data for the Black-Litterman use case, containing top tickers, market returns, adjusted returns, and failure status. */
public class BlackLittermanOutputData {
    private final User user; // Add user reference
    private final List<String> topTickers;
    private final Map<String, Double> marketReturns;
    private final Map<String, Double> adjustedReturns;
    private final boolean useCaseFailed;

    public BlackLittermanOutputData(User user,
                                    List<String> topTickers,
                                    Map<String, Double> marketReturns,
                                    Map<String, Double> adjustedReturns,
                                    boolean useCaseFailed) {
        this.user = user;
        this.topTickers = topTickers;
        this.marketReturns = marketReturns;
        this.adjustedReturns = adjustedReturns;
        this.useCaseFailed = useCaseFailed;
    }

    public User getUser() {
        return user;
    }

    /** Returns the list of top stock tickers.
     * @return a list of top 5 tickers.
     */
    public List<String> getTopTickers() {
        return topTickers;
    }

    /** Returns the map of market equilibrium returns.
     * @return a map of asset tickers to market returns.
     */
    public Map<String, Double> getMarketReturns() {
        return marketReturns;
    }

    /** Returns the map of adjusted expected returns.
     * @return a map of asset tickers to adjusted expected returns.
     */
    public Map<String, Double> getAdjustedReturns() {
        return adjustedReturns;
    }

    /** Returns whether the use case failed.
     * @return true if failed, false otherwise.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}