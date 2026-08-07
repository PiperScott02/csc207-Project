package interface_adapter.delete_holding;

import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInState;
import use_case.delete_holding.DeleteHoldingOutputBoundary;
import use_case.delete_holding.DeleteHoldingOutputData;

public class DeleteHoldingPresenter implements DeleteHoldingOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;

    public DeleteHoldingPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteHoldingOutputData outputData) {
        // Update the state with the new holdings list
        LoggedInState state = loggedInViewModel.getState();
        state.setHoldings(outputData.getPortfolio().getHoldings());
        loggedInViewModel.setState(state);

        // Fire property change so HoldingsView hears it and re-renders
        loggedInViewModel.firePropertyChanged("state");
    }

    @Override
    public void prepareFailView(String error) {
        // Handle failure if needed
    }
}