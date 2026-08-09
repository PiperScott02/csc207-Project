package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.add_holding.AddHoldingController;
import interface_adapter.add_holding.AddHoldingPresenter;
import interface_adapter.add_holding.AddHoldingViewModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import use_case.StockDailyDataAccessInterface;
import use_case.add_holding.AddHoldingInputBoundary;
import use_case.add_holding.AddHoldingInteractor;
import use_case.add_holding.AddHoldingOutputBoundary;
import view.AddHoldingView;
import data_access.FileUserDataAccessObject;

/**
 * Factory for creating the Add Holding use case.
 */
public final class AddHoldingUseCaseFactory {

    private AddHoldingUseCaseFactory() {
        // Prevent instantiation
    }

    public static AddHoldingView create(
            ViewManagerModel viewManagerModel,
            AddHoldingViewModel addHoldingViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDailyDataAccessInterface stockDataAccessObject,
            FileUserDataAccessObject userDataAccessObject,
            BlackLittermanController blackLittermanController,
            PortfolioHealthController portfolioHealthController) {

        AddHoldingController addHoldingController = createAddHoldingUseCase(
                viewManagerModel, addHoldingViewModel, loggedInViewModel,
                stockDataAccessObject, userDataAccessObject
        );

        return new AddHoldingView(
                addHoldingViewModel,
                addHoldingController,
                viewManagerModel,
                loggedInViewModel,
                portfolioHealthController,
                blackLittermanController
        );
    }

    private static AddHoldingController createAddHoldingUseCase(
            ViewManagerModel viewManagerModel,
            AddHoldingViewModel addHoldingViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDailyDataAccessInterface stockDataAccessObject,
            FileUserDataAccessObject userDataAccessObject) {

        final AddHoldingOutputBoundary addHoldingOutputBoundary =
                new AddHoldingPresenter(addHoldingViewModel, loggedInViewModel, viewManagerModel);

        final AddHoldingInputBoundary addHoldingInteractor =
                new AddHoldingInteractor(stockDataAccessObject, addHoldingOutputBoundary, loggedInViewModel, userDataAccessObject);

        return new AddHoldingController(addHoldingInteractor);
    }
}