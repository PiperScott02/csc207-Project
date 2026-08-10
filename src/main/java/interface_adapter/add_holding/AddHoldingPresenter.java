package interface_adapter.add_holding;

import use_case.add_holding.AddHoldingOutputBoundary;
import use_case.add_holding.AddHoldingOutputData;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;

public class AddHoldingPresenter implements AddHoldingOutputBoundary {

    private final AddHoldingViewModel addHoldingViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public AddHoldingPresenter(AddHoldingViewModel addHoldingViewModel,
                               LoggedInViewModel loggedInViewModel,
                               ViewManagerModel viewManagerModel) {
        this.addHoldingViewModel = addHoldingViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(AddHoldingOutputData outputData) {
        // 1. Update the logged-in portfolio state with the new holdings
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setHoldings(outputData.getHoldings());
        loggedInViewModel.setState(loggedInState);
        loggedInViewModel.firePropertyChanged();

        // 2. Clear the Add Holding form input fields and any old errors in the state
        AddHoldingState addHoldingState = addHoldingViewModel.getState();
        addHoldingState.setTicker("");
        addHoldingState.setShares("");
        addHoldingState.setPurchaseDate("");
        addHoldingState.setAddHoldingError(null);
        addHoldingViewModel.setState(addHoldingState);
        addHoldingViewModel.firePropertyChanged();

        // 3. Switch back to the holdings view
        viewManagerModel.setState("holdings");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        AddHoldingState addHoldingState = addHoldingViewModel.getState();
        addHoldingState.setAddHoldingError(errorMessage);
        addHoldingViewModel.setState(addHoldingState);
        addHoldingViewModel.firePropertyChanged();
    }
}