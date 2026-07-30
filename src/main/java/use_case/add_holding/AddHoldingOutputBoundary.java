package use_case.add_holding;

/**
 * The output boundary for the Add Holding Use Case.
 */
public interface AddHoldingOutputBoundary {
    /**
     * Prepares the success view for the Add Holding Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AddHoldingOutputData outputData);

    /**
     * Prepares the failure view for the Add Holding Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}