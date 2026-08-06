package use_case.analysis;

import entity.RiskProfile;

/** Service class to generate detailed, bracket-specific advice using calculated sub-scores directly. */
public class PortfolioAdviceService {

    /**
     * Generates detailed advice for the Sharpe Ratio based on its calculated sub-score (out of 25.0).
     *
     * @param sharpeScore the calculated Sharpe sub-score
     * @return a descriptive advice string
     */
    public static String getSharpeAdvice(Double sharpeScore) {
        if (sharpeScore == null) {
            return "Sharpe score data is unavailable. Ensure metrics have been calculated for your holdings.";
        }
        if (sharpeScore >= 25.0) {
            return "Exceptional performance. Your risk-adjusted returns are outstanding, generating maximum excess for the given risk.";
        }
        if (sharpeScore >= 20.0) {
            return "Strong performance. Your portfolio exhibits great risk efficiency and reliable returns relative to market risk.";
        }
        if (sharpeScore >= 15.0) {
            return "Good performance. Your risk-adjusted returns are stable and meet acceptable investment benchmarks.";
        }
        if (sharpeScore >= 10.0) {
            return "Sub-optimal returns. Your returns are barely compensating for the risk taken. Consider reallocating weaker assets.";
        }
        return "Critical deficiency (Score 0.0): Your portfolio is generating negative excess returns relative to the risk-free rate. Immediate reallocation is heavily advised.";
    }

    /**
     * Generates detailed advice for Risk Alignment based on its calculated sub-score (out of 25.0) and user risk profile.
     *
     * @param riskScore the calculated risk alignment sub-score
     * @param riskProfile the user's risk profile
     * @return a descriptive advice string
     */
    public static String getRiskAlignmentAdvice(Double riskScore, RiskProfile riskProfile) {
        if (riskScore == null || riskProfile == null) {
            return "Risk alignment data is incomplete.";
        }

        String levelStr = riskProfile.getRiskLevel().toString().toLowerCase();

        if (riskScore >= 25.0) {
            return "Perfect risk alignment: Your portfolio's market volatility exposure precisely matches your " + levelStr + " profile.";
        }
        if (riskScore >= 20.0) {
            return "Minor deviation: Your beta is slightly outside your target threshold. Keep an eye on market swings.";
        }
        if (riskScore >= 15.0) {
            return "Moderate misalignment: Noticeable variance from your target risk profile. Consider adjusting asset weights.";
        }
        if (riskScore >= 10.0) {
            return "High misalignment: Your portfolio risk profile is varying away from your " + levelStr + " targets.";
        }
        if (riskScore >= 5.0) {
            return "Significant risk mismatch: Your market sensitivity is poorly aligned with your stated risk tolerance.";
        }
        return "Severe misalignment (Score 0.0): Your portfolio risk profile is completely opposed to your risk preferences. Immediate reallocation is heavily advised.";
    }

    /**
     * Generates detailed advice for Diversification based on its calculated sub-score (out of 25.0).
     *
     * @param divScore the calculated diversification sub-score
     * @return a descriptive advice string
     */
    public static String getDiversificationAdvice(Double divScore) {
        if (divScore == null) {
            return "Diversification metrics are unavailable.";
        }
        if (divScore >= 25.0) {
            return "Outstanding diversification: Your diversification is optimal, fully capturing structural risk mitigation benefits.";
        }
        if (divScore >= 20.0) {
            return "High diversification: Your asset allocation creates strong risk mitigation across holdings.";
        }
        if (divScore >= 15.0) {
            return "Good diversification: Your portfolio shows moderate correlation protection.";
        }
        if (divScore >= 10.0) {
            return "Moderate diversification: Your portfolio is starting to capture risk benefits, but could benefit from adding uncorrelated assets.";
        }
        if (divScore >= 5.0) {
            return "Low diversification: Minimal risk reduction from asset blending. Consider diversifying into broader industry sectors.";
        }
        return "No diversification: Your portfolio acts largely as a single asset or suffers from severe correlation. Diversification benefits are absent.";
    }

    /**
     * Generates detailed advice for news sentiment or market events (placeholder for now).
     *
     * @return a descriptive advice string
     */
    /**
     * Generates bracket-specific advice based on the overall portfolio news sentiment score.
     *
     * @param newsScore the news sub-score (out of 25.0 points)
     * @return actionable advice text explaining the current news sentiment status
     */
    public static String getNewsAdvice(Double newsScore) {
        if (newsScore == null) {
            return "News sentiment data is unavailable for your holdings.";
        }

        if (newsScore >= 20.0) {
            return "Strong Bullish Sentiment: Recent news coverage across your holdings is overwhelmingly positive. Market tailwinds are currently in your favor.";
        } else if (newsScore >= 15.0) {
            return "Moderately Bullish Sentiment: Overall media sentiment is positive with favorable news coverage outweighs negative headlines.";
        } else if (newsScore >= 10.0) {
            return "Neutral Sentiment: Recent news stories are balanced or quiet across your holdings, showing no significant market-moving sentiment bias.";
        } else if (newsScore >= 5.0) {
            return "Cautious/Bearish Sentiment: Several of your holdings face negative news coverage or sector headwinds. Consider monitoring upcoming announcements.";
        } else {
            return "Strong Bearish Sentiment: Headline sentiment across your holdings is significantly negative. Review individual positions for potential risk exposure.";
        }
    }
}