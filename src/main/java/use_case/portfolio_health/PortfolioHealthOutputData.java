package use_case.portfolio_health;

import entity.Portfolio;
import entity.RiskProfile;
import use_case.analysis.PortfolioAdviceService;
import use_case.analysis.PortfolioFinancialService;

/** The output data for the Portfolio Health use case, containing the formatted results to be presented to the view model. */
public class PortfolioHealthOutputData {
    private final String riskPreference;
    private final String portfolioHealthScore;
    private final String beta;
    private final String alpha;
    private final String sharpeRatio;
    private final String sharpeAdvice;
    private final String riskAlignmentAdvice;
    private final String diversificationAdvice;
    private final String newsAdvice;
    private final boolean useCaseFailed;

    /** Constructs a new PortfolioHealthOutputData object with financial metrics, advice strings, and failure status.
     * @param riskPreference the user's risk preference.
     * @param portfolioHealthScore the portfolio health score.
     * @param beta the beta metric string.
     * @param alpha the alpha metric string.
     * @param sharpeRatio the Sharpe ratio metric string.
     * @param sharpeAdvice the advice string for Sharpe ratio performance.
     * @param riskAlignmentAdvice the advice string for risk alignment.
     * @param diversificationAdvice the advice string for portfolio diversification.
     * @param newsAdvice the advice string for news sentiment.
     * @param useCaseFailed boolean indicating whether the operation failed.
     */
    public PortfolioHealthOutputData(String riskPreference, String portfolioHealthScore,
                                     String beta, String alpha, String sharpeRatio,
                                     String sharpeAdvice, String riskAlignmentAdvice,
                                     String diversificationAdvice, String newsAdvice, boolean useCaseFailed) {
        this.riskPreference = riskPreference;
        this.portfolioHealthScore = portfolioHealthScore;
        this.beta = beta;
        this.alpha = alpha;
        this.sharpeRatio = sharpeRatio;
        this.sharpeAdvice = sharpeAdvice;
        this.riskAlignmentAdvice = riskAlignmentAdvice;
        this.diversificationAdvice = diversificationAdvice;
        this.newsAdvice = newsAdvice;
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

    /** Returns the Sharpe advice string.
     * @return the Sharpe advice.
     */
    public String getSharpeAdvice() {
        return sharpeAdvice;
    }

    /** Returns the risk alignment advice string.
     * @return the risk alignment advice.
     */
    public String getRiskAlignmentAdvice() {
        return riskAlignmentAdvice;
    }

    /** Returns the diversification advice string.
     * @return the diversification advice.
     */
    public String getDiversificationAdvice() {
        return diversificationAdvice;
    }

    /** Returns the news advice string.
     * @return the news advice.
     */
    public String getNewsAdvice() {
        return newsAdvice;
    }

    /** Returns whether the use case failed.
     * @return true if failed, false otherwise.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}