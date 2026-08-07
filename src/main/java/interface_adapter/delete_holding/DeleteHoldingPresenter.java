package interface_adapter.delete_holding;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.delete_holding.DeleteHoldingOutputBoundary;
import use_case.delete_holding.DeleteHoldingOutputData;

public class DeleteHoldingPresenter implements DeleteHoldingOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;

    public DeleteHoldingPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteHoldingOutputData outputData) {
        LoggedInState currentState = loggedInViewModel.getState();

        currentState.setHoldings(outputData.getPortfolio().getHoldings());

        loggedInViewModel.setState(currentState);
        loggedInViewModel.firePropertyChanged("state");

        System.out.println("Holding deleted successfully!");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println(errorMessage);
    }
}