package interface_adapter.delete_holding;

import use_case.delete_holding.DeleteHoldingInputBoundary;
import use_case.delete_holding.DeleteHoldingInputData;

public class DeleteHoldingController {
    private final DeleteHoldingInputBoundary deleteHoldingUseCaseInteractor;

    public DeleteHoldingController(DeleteHoldingInputBoundary deleteHoldingUseCaseInteractor) {
        this.deleteHoldingUseCaseInteractor = deleteHoldingUseCaseInteractor;
    }

    public void execute(String ticker) {
        DeleteHoldingInputData inputData = new DeleteHoldingInputData(ticker);
        deleteHoldingUseCaseInteractor.execute(inputData);
    }
}