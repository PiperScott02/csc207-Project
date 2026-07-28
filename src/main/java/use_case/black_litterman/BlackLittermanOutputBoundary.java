package use_case.black_litterman;

/** Output boundary interface for the Black-Litterman use case. */
public interface BlackLittermanOutputBoundary {

    /** Prepares the success view for the Black-Litterman use case.
     * @param outputData the output data containing adjusted expected returns.
     */
    void prepareSuccessView(BlackLittermanOutputData outputData);

    /** Prepares the failure view with an error message.
     * @param errorMessage the error description string.
     */
    void prepareFailView(String errorMessage);
}