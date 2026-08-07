package interface_adapter.risk_preference;

import java.time.LocalDateTime;

import entity.RiskLevel;

/**
 * Stores the data displayed by the risk-preference view.
 */
public class RiskPreferenceState {

    private RiskLevel riskLevel = RiskLevel.MODERATE;
    private LocalDateTime lastUpdated;
    private String message = "";
    private String error = "";

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
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