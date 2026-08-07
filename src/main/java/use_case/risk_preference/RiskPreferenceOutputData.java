package use_case.risk_preference;

import java.time.LocalDateTime;

import entity.RiskLevel;

/**
 * Output data after saving risk preferences.
 */
public class RiskPreferenceOutputData {

    private final RiskLevel riskLevel;
    private final LocalDateTime lastUpdated;

    /**
     * Creates the output data.
     *
     * @param riskLevel saved risk level
     * @param lastUpdated time of the latest update
     */
    public RiskPreferenceOutputData(
            RiskLevel riskLevel,
            LocalDateTime lastUpdated) {
        this.riskLevel = riskLevel;
        this.lastUpdated = lastUpdated;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}