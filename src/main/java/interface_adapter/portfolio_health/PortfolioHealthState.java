package interface_adapter.portfolio_health;

public class PortfolioHealthState {
    private String portfolioHealthScore = "";
    private String riskPreference = "";
    private String beta = "";
    private String alpha = "";
    private String sharpeRatio = "";
    private String errorMessage = "";

    /** Copy constructor to create a new PortfolioHealthState from an existing one.
     * @param copy the PortfolioHealthState to copy from.
     */
    public PortfolioHealthState(PortfolioHealthState copy) {
        this.portfolioHealthScore = copy.portfolioHealthScore;
        this.riskPreference = copy.riskPreference;
        this.beta = copy.beta;
        this.alpha = copy.alpha;
        this.sharpeRatio = copy.sharpeRatio;
        this.errorMessage = copy.errorMessage;
    }

    /** Default constructor to create an empty PortfolioHealthState. */
    public PortfolioHealthState() {
    }

    /** Returns the portfolio health score.
     * @return the portfolio health score string.
     */
    public String getPortfolioHealthScore() {
        return portfolioHealthScore;
    }

    /** Sets the portfolio health score.
     * @param portfolioHealthScore the portfolio health score to set.
     */
    public void setPortfolioHealthScore(String portfolioHealthScore) {
        this.portfolioHealthScore = portfolioHealthScore;
    }

    /** Returns the risk preference.
     * @return the risk preference string.
     */
    public String getRiskPreference() {
        return riskPreference;
    }

    /** Sets the risk preference.
     * @param riskPreference the risk preference to set.
     */
    public void setRiskPreference(String riskPreference) {
        this.riskPreference = riskPreference;
    }

    /** Returns the beta value as a string.
     * @return the beta string.
     */
    public String getBeta() {
        return beta;
    }

    /** Sets the beta value string.
     * @param beta the beta string to set.
     */
    public void setBeta(String beta) {
        this.beta = beta;
    }

    /** Returns the alpha value as a string.
     * @return the alpha string.
     */
    public String getAlpha() {
        return alpha;
    }

    /** Sets the alpha value string.
     * @param alpha the alpha string to set.
     */
    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    /** Returns the Sharpe ratio value as a string.
     * @return the Sharpe ratio string.
     */
    public String getSharpeRatio() {
        return sharpeRatio;
    }

    /** Sets the Sharpe ratio value string.
     * @param sharpeRatio the Sharpe ratio string to set.
     */
    public void setSharpeRatio(String sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    /** Returns the error message.
     * @return the error message string.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /** Sets the error message.
     * @param errorMessage the error message string to set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}