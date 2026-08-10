package use_case.delete_holding;

public interface DeleteHoldingOutputBoundary {
    void prepareSuccessView(DeleteHoldingOutputData outputData);
    void prepareFailView(String errorMessage);
}