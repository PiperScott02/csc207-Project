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
    private final String annualizedAlpha;
    private final String annualizedSharpeRatio;
    private final String sharpeAdvice;
    private final String riskAlignmentAdvice;
    private final String diversificationAdvice;
    private final String newsAdvice;
    private final boolean useCaseFailed;

    /** Constructs a new PortfolioHealthOutputData object with financial metrics, advice strings, and failure status.
     * @param riskPreference the user's risk preference.
     * @param portfolioHealthScore the portfolio health score.
     * @param beta the beta metric string.
     * @param annualizedAlpha the annualized alpha metric string.
     * @param annualizedSharpeRatio the annualized Sharpe ratio metric string.
     * @param sharpeAdvice the advice string for Sharpe ratio performance.
     * @param riskAlignmentAdvice the advice string for risk alignment.
     * @param diversificationAdvice the advice string for portfolio diversification.
     * @param newsAdvice the advice string for news sentiment.
     * @param useCaseFailed boolean indicating whether the operation failed.
     */
    public PortfolioHealthOutputData(String riskPreference, String portfolioHealthScore,
                                     String beta, String annualizedAlpha, String annualizedSharpeRatio,
                                     String sharpeAdvice, String riskAlignmentAdvice,
                                     String diversificationAdvice, String newsAdvice, boolean useCaseFailed) {
        this.riskPreference = riskPreference;
        this.portfolioHealthScore = portfolioHealthScore;
        this.beta = beta;
        this.annualizedAlpha = annualizedAlpha;
        this.annualizedSharpeRatio = annualizedSharpeRatio;
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

    /** Returns the annualized alpha value string.
     * @return the annualized alpha string.
     */
    public String getAnnualizedAlpha() {
        return annualizedAlpha;
    }

    /** Returns the annualized Sharpe ratio value string.
     * @return the annualized Sharpe ratio string.
     */
    public String getAnnualizedSharpeRatio() {
        return annualizedSharpeRatio;
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