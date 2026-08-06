package interface_adapter.add_holding;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.add_holding.AddHoldingOutputBoundary;
import use_case.add_holding.AddHoldingOutputData;

/**
 * The Presenter for the Add Holding Use Case.
 */
public class AddHoldingPresenter implements AddHoldingOutputBoundary {

    private final AddHoldingViewModel addHoldingViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    public AddHoldingPresenter(AddHoldingViewModel addHoldingViewModel,
                               LoggedInViewModel loggedInViewModel,
                               ViewManagerModel viewManagerModel) {
        this.addHoldingViewModel = addHoldingViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(AddHoldingOutputData outputData) {
        // Update the LoggedInState with the new holdings list
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setHoldings(outputData.getHoldings());
        loggedInViewModel.setState(loggedInState);
        loggedInViewModel.firePropertyChanged();

        // Tell the ViewManager to switch back to the main dashboard ("logged in") view
        viewManagerModel.setState("logged in");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Update the state with the error message and notify the view to display it
        AddHoldingState currentState = addHoldingViewModel.getState();
        currentState.setAddHoldingError(errorMessage);
        addHoldingViewModel.setState(currentState);
        addHoldingViewModel.firePropertyChanged();
    }
}