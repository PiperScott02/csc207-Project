package app;

import entity.Portfolio;

import interface_adapter.ViewManagerModel;
import interface_adapter.add_holding.AddHoldingController;
import interface_adapter.add_holding.AddHoldingPresenter;
import interface_adapter.add_holding.AddHoldingViewModel;
import interface_adapter.logged_in.LoggedInViewModel;

import use_case.StockDailyDataAccessInterface;
import use_case.add_holding.AddHoldingInputBoundary;
import use_case.add_holding.AddHoldingInteractor;
import use_case.add_holding.AddHoldingOutputBoundary;

import view.AddHoldingView;

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
            Portfolio portfolio) {

        final AddHoldingController addHoldingController =
                createAddHoldingUseCase(viewManagerModel, addHoldingViewModel, loggedInViewModel, stockDataAccessObject, portfolio);

        return new AddHoldingView(addHoldingViewModel, addHoldingController, viewManagerModel);
    }

    private static AddHoldingController createAddHoldingUseCase(
            ViewManagerModel viewManagerModel,
            AddHoldingViewModel addHoldingViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDailyDataAccessInterface stockDataAccessObject,
            Portfolio portfolio) {

        final AddHoldingOutputBoundary addHoldingOutputBoundary =
                new AddHoldingPresenter(addHoldingViewModel, loggedInViewModel, viewManagerModel);

        final AddHoldingInputBoundary addHoldingInteractor =
                new AddHoldingInteractor(stockDataAccessObject, addHoldingOutputBoundary, portfolio);

        return new AddHoldingController(addHoldingInteractor);
    }
}