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
    public void prepareSuccessView(DeleteHoldingOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();

        if (response.getPortfolio() != null) {
            loggedInState.setHoldings(response.getPortfolio().getHoldings());
        }

        this.loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChanged("state");
    }

    @Override
    public void prepareFailView(String error) {
        // Handle failure if needed
    }
}