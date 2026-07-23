package use_case.risk_preference;

import java.util.Set;

import entity.InvestmentGoal;
import entity.RiskLevel;
import entity.TimeHorizon;

/**
 * Input data for saving a user's risk preferences.
 */
public class RiskPreferenceInputData {

    private final RiskLevel riskLevel;
    private final Set<InvestmentGoal> investmentGoals;
    private final TimeHorizon timeHorizon;

    /**
     * Creates the input data.
     *
     * @param riskLevel selected risk level
     * @param investmentGoals selected investment goals
     * @param timeHorizon selected time horizon
     */
    public RiskPreferenceInputData(
            RiskLevel riskLevel,
            Set<InvestmentGoal> investmentGoals,
            TimeHorizon timeHorizon) {
        this.riskLevel = riskLevel;
        this.investmentGoals = investmentGoals;
        this.timeHorizon = timeHorizon;
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
}