package use_case.add_holding;

/**
 * The Add Holding Use Case.
 */
public interface AddHoldingInputBoundary {

    /**
     * Execute the Add Holding Use Case.
     * @param addHoldingInputData the input data for this use case
     */
    void execute(AddHoldingInputData addHoldingInputData);
}
