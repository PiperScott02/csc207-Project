package use_case.stock;

/** Output boundary interface for the stock use case. */
public interface StockOutputBoundary {

    /** Prepares the success view for the stock use case.
     * @param outputData the output data containing stock details.
     */
    void prepareSuccessView(StockOutputData outputData);

    /** Prepares the failure view with an error message.
     * @param errorMessage the error description string.
     */
    void prepareFailView(String errorMessage);
}