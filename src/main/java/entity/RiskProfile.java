package entity;

import java.time.LocalDateTime;

/**
 * Stores a user's selected investment risk preferences.
 */
public class RiskProfile {

    private RiskLevel riskLevel;
    private LocalDateTime lastUpdated;

    /**
     * Creates a profile with the default settings shown in the mockup.
     */
    public RiskProfile() {
        riskLevel = RiskLevel.MODERATE;
        lastUpdated = null;
    }

    /**
     * Creates a profile with the given settings.
     *
     * @param riskLevel selected risk level
     */
    public RiskProfile(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
        this.lastUpdated = LocalDateTime.now();
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
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