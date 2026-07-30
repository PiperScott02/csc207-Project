package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockPresenter;
import interface_adapter.stock.StockViewModel;
import use_case.stock.StockDataAccessInterface;
import use_case.stock.StockInputBoundary;
import use_case.stock.StockInteractor;
import use_case.stock.StockOutputBoundary;
import view.StockView;

public final class StockUseCaseFactory {

    /** Prevent instantiation. */
    private StockUseCaseFactory() {
    }

    /**
     * Creates and returns the fully-wired StockView.
     */
    public static StockView create(
            ViewManagerModel viewManagerModel,
            StockViewModel stockViewModel,
            LoggedInViewModel loggedInViewModel,
            StockDataAccessInterface stockDataAccessObject) {

        final StockController stockController = createStockUseCase(
                viewManagerModel,
                stockViewModel,
                stockDataAccessObject
        );

        return new StockView(
                stockViewModel,
                viewManagerModel,
                loggedInViewModel
        );
    }

    /**
     * Creates and wires the Controller, Interactor, and Presenter for the Stock Use Case.
     */
    public static StockController createStockUseCase(
            ViewManagerModel viewManagerModel,
            StockViewModel stockViewModel,
            StockDataAccessInterface stockDataAccessObject) {

        final StockOutputBoundary stockOutputBoundary =
                new StockPresenter(viewManagerModel, stockViewModel);

        final StockInputBoundary stockInteractor =
                new StockInteractor(stockDataAccessObject, stockOutputBoundary);

        return new StockController(stockInteractor);
    }
}