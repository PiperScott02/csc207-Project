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
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setHoldings(outputData.getHoldings());
        loggedInViewModel.setState(loggedInState);
        loggedInViewModel.firePropertyChanged();

        viewManagerModel.setState("holdings");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        AddHoldingState currentState = addHoldingViewModel.getState();
        currentState.setAddHoldingError(errorMessage);
        addHoldingViewModel.setState(currentState);
        addHoldingViewModel.firePropertyChanged();
    }
}