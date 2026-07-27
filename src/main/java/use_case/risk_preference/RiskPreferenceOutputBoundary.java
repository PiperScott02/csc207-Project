package use_case.risk_preference;

/**
 * Output boundary for saving risk preferences.
 */
public interface RiskPreferenceOutputBoundary {

    /**
     * Prepares a successful result.
     *
     * @param outputData the saved preference data
     */
    void prepareSuccessView(RiskPreferenceOutputData outputData);

    /**
     * Prepares a failed result.
     *
     * @param error the error message
     */
    void prepareFailView(String error);
}