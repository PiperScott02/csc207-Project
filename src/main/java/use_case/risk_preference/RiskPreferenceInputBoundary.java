package use_case.risk_preference;

/**
 * Input boundary for saving a user's risk preferences.
 */
public interface RiskPreferenceInputBoundary {

    /**
     * Saves the selected risk preferences.
     *
     * @param inputData the selected preferences
     */
    void execute(RiskPreferenceInputData inputData);
}