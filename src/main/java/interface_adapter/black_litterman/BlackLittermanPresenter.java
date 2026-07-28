package interface_adapter.black_litterman;

import entity.User;
import interface_adapter.ViewManagerModel;
import use_case.black_litterman.BlackLittermanOutputBoundary;
import use_case.black_litterman.BlackLittermanOutputData;

/**
 * The Presenter for the Black-Litterman use case, handling success and failure views.
 */
public class BlackLittermanPresenter implements BlackLittermanOutputBoundary {

    private final BlackLittermanViewModel blackLittermanViewModel;
    private final ViewManagerModel viewManagerModel;

    public BlackLittermanPresenter(ViewManagerModel viewManagerModel,
                                   BlackLittermanViewModel blackLittermanViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.blackLittermanViewModel = blackLittermanViewModel;
    }

    @Override
    public void prepareSuccessView(BlackLittermanOutputData outputData) {
        BlackLittermanState currentState = blackLittermanViewModel.getState();

        currentState.setTopTickers(outputData.getTopTickers());
        currentState.setMarketReturns(outputData.getMarketReturns());
        currentState.setAdjustedReturns(outputData.getAdjustedReturns());
        // Keep user attached if outputData carries user reference, or preserve state user
        if (outputData.getUser() != null) {
            currentState.setUser(outputData.getUser());
        }

        blackLittermanViewModel.setState(currentState);
        blackLittermanViewModel.firePropertyChanged();

        viewManagerModel.setState(blackLittermanViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        BlackLittermanState currentState = blackLittermanViewModel.getState();
        currentState.setErrorMessage(errorMessage);

        blackLittermanViewModel.setState(currentState);
        blackLittermanViewModel.firePropertyChanged();
    }
}