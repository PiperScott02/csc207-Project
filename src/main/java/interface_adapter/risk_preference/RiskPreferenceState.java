package interface_adapter.risk_preference;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

import entity.InvestmentGoal;
import entity.RiskLevel;
import entity.TimeHorizon;

/**
 * Stores the data displayed by the risk-preference view.
 */
public class RiskPreferenceState {

    private RiskLevel riskLevel = RiskLevel.MODERATE;

    private Set<InvestmentGoal> investmentGoals =
            EnumSet.noneOf(InvestmentGoal.class);

    private TimeHorizon timeHorizon =
            TimeHorizon.FIVE_TO_TEN_YEARS;

    private LocalDateTime lastUpdated;
    private String message = "";
    private String error = "";

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Set<InvestmentGoal> getInvestmentGoals() {
        return investmentGoals;
    }

    public void setInvestmentGoals(
            Set<InvestmentGoal> investmentGoals) {
        this.investmentGoals = investmentGoals;
    }

    public TimeHorizon getTimeHorizon() {
        return timeHorizon;
    }

    public void setTimeHorizon(TimeHorizon timeHorizon) {
        this.timeHorizon = timeHorizon;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}