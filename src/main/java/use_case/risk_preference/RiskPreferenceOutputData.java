package use_case.risk_preference;

import java.time.LocalDateTime;
import java.util.Set;

import entity.InvestmentGoal;
import entity.RiskLevel;
import entity.TimeHorizon;

/**
 * Output data after saving risk preferences.
 */
public class RiskPreferenceOutputData {

    private final RiskLevel riskLevel;
    private final Set<InvestmentGoal> investmentGoals;
    private final TimeHorizon timeHorizon;
    private final LocalDateTime lastUpdated;

    /**
     * Creates the output data.
     *
     * @param riskLevel saved risk level
     * @param investmentGoals saved investment goals
     * @param timeHorizon saved time horizon
     * @param lastUpdated time of the latest update
     */
    public RiskPreferenceOutputData(
            RiskLevel riskLevel,
            Set<InvestmentGoal> investmentGoals,
            TimeHorizon timeHorizon,
            LocalDateTime lastUpdated) {
        this.riskLevel = riskLevel;
        this.investmentGoals = investmentGoals;
        this.timeHorizon = timeHorizon;
        this.lastUpdated = lastUpdated;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public Set<InvestmentGoal> getInvestmentGoals() {
        return investmentGoals;
    }

    public TimeHorizon getTimeHorizon() {
        return timeHorizon;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}