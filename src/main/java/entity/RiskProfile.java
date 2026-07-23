package entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Stores a user's selected investment risk preferences.
 */
public class RiskProfile {

    private RiskLevel riskLevel;
    private Set<InvestmentGoal> investmentGoals;
    private TimeHorizon timeHorizon;
    private LocalDateTime lastUpdated;

    /**
     * Creates a profile with the default settings shown in the mockup.
     */
    public RiskProfile() {
        riskLevel = RiskLevel.MODERATE;
        investmentGoals = EnumSet.noneOf(InvestmentGoal.class);
        timeHorizon = TimeHorizon.FIVE_TO_TEN_YEARS;
        lastUpdated = null;
    }

    /**
     * Creates a profile with the given settings.
     *
     * @param riskLevel selected risk level
     * @param investmentGoals selected investment goals
     * @param timeHorizon selected time horizon
     */
    public RiskProfile(RiskLevel riskLevel,
                       Set<InvestmentGoal> investmentGoals,
                       TimeHorizon timeHorizon) {
        this.riskLevel = riskLevel;
        setInvestmentGoals(investmentGoals);
        this.timeHorizon = timeHorizon;
        this.lastUpdated = LocalDateTime.now();
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Set<InvestmentGoal> getInvestmentGoals() {
        return Collections.unmodifiableSet(investmentGoals);
    }

    public void setInvestmentGoals(Set<InvestmentGoal> investmentGoals) {
        if (investmentGoals == null || investmentGoals.isEmpty()) {
            this.investmentGoals =
                    EnumSet.noneOf(InvestmentGoal.class);
        }
        else {
            this.investmentGoals =
                    EnumSet.copyOf(investmentGoals);
        }
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

    /**
     * Records the current time as the last update time.
     */
    public void updateLastUpdated() {
        lastUpdated = LocalDateTime.now();
    }
}