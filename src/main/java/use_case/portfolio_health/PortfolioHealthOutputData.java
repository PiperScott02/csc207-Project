package use_case.portfolio_health;

/** The output data for the Portfolio Health use case, containing the formatted results to be presented to the view model. */
public class PortfolioHealthOutputData {
    private final String riskPreference;
    private final String portfolioHealthScore;
    private final String beta;
    private final String alpha;
    private final String sharpeRatio;
    private final boolean useCaseFailed;

    /** Constructs a new PortfolioHealthOutputData object with financial metrics and failure status.
     * @param riskPreference the user's risk preference.
     * @param portfolioHealthScore the portfolio health score.
     * @param beta the beta metric string.
     * @param alpha the alpha metric string.
     * @param sharpeRatio the Sharpe ratio metric string.
     * @param useCaseFailed boolean indicating whether the operation failed.
     */
    public PortfolioHealthOutputData(String riskPreference, String portfolioHealthScore,
                                     String beta, String alpha, String sharpeRatio, boolean useCaseFailed) {
        this.riskPreference = riskPreference;
        this.portfolioHealthScore = portfolioHealthScore;
        this.beta = beta;
        this.alpha = alpha;
        this.sharpeRatio = sharpeRatio;
        this.useCaseFailed = useCaseFailed;
    }

    /** Returns the risk preference.
     * @return the risk preference string.
     */
    public String getRiskPreference() {
        return riskPreference;
    }

    /** Returns the portfolio health score.
     * @return the portfolio health score string.
     */
    public String getPortfolioHealthScore() {
        return portfolioHealthScore;
    }

    /** Returns the beta value string.
     * @return the beta string.
     */
    public String getBeta() {
        return beta;
    }

    /** Returns the alpha value string.
     * @return the alpha string.
     */
    public String getAlpha() {
        return alpha;
    }

    /** Returns the Sharpe ratio value string.
     * @return the Sharpe ratio string.
     */
    public String getSharpeRatio() {
        return sharpeRatio;
    }

    /** Returns whether the use case failed.
     * @return true if failed, false otherwise.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}