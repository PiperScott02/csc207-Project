package use_case.portfolio_health;

/** Output boundary interface for the Portfolio Health use case. */
public interface PortfolioHealthOutputBoundary {

    /** Prepares the success view for the Portfolio Health use case.
     * @param outputData the output data containing portfolio health details.
     */
    void prepareSuccessView(PortfolioHealthOutputData outputData);

    /** Prepares the failure view with an error message.
     * @param errorMessage the error description string.
     */
    void prepareFailView(String errorMessage);
}