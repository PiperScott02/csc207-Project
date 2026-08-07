package use_case.risk_preference;

import entity.RiskLevel;

/**
 * Input data for saving a user's risk preferences.
 */
public class RiskPreferenceInputData {

    private final RiskLevel riskLevel;

    /**
     * Creates the input data.
     *
     * @param riskLevel selected risk level
     */
    public RiskPreferenceInputData(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }
}