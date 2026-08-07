package interface_adapter.delete_holding;

import use_case.delete_holding.DeleteHoldingOutputBoundary;
import use_case.delete_holding.DeleteHoldingOutputData;

public class DeleteHoldingPresenter implements DeleteHoldingOutputBoundary {

    @Override
    public void prepareSuccessView(DeleteHoldingOutputData outputData) {
        // Here you update your ViewModel or state with the updated portfolio
        System.out.println("Holding deleted successfully!");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println(errorMessage);
    }
}