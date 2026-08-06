package use_case.risk_preference;

/**
 * Input boundary for the risk-preference use case.
 */
public interface RiskPreferenceInputBoundary {

    /**
     * Saves the selected risk preferences.
     *
     * @param inputData selected preferences
     */
    void execute(RiskPreferenceInputData inputData);

    /**
     * Loads the current user's saved risk preferences.
     */
    void load();
}