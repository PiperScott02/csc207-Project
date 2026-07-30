package app;

import data_access.FileStockDataAccessObject;
import entity.CommonUserFactory;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthPresenter;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.portfolio_health.PortfolioHealthInteractor;
import use_case.portfolio_health.PortfolioHealthOutputBoundary;
import use_case.stock.StockDataAccessInterface;
import view.LoggedInView;

/**
 * This class contains the static factory function for creating the LoggedInView.
 */
public final class ChangePasswordUseCaseFactory {

    /** Prevent instantiation. */
    private ChangePasswordUseCaseFactory() {

    }

    /**
     * Factory function for creating the LoggedInView.
     * @param viewManagerModel the ViewManagerModel to inject into the LoggedInView
     * @param loggedInViewModel the loggedInViewModel to inject into the LoggedInView
     * @param userDataAccessObject the ChangePasswordUserDataAccessInterface to inject into the LoggedInView
     * @return the LoggedInView created for the provided input classes
     */
    public static LoggedInView create(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            ChangePasswordUserDataAccessInterface userDataAccessObject) {

        final PortfolioHealthViewModel portfolioHealthViewModel = new PortfolioHealthViewModel();
        final StockDataAccessInterface stockDataAccessObject = new FileStockDataAccessObject();

        final PortfolioHealthController portfolioHealthController =
                createPortfolioHealthController(viewManagerModel, portfolioHealthViewModel, stockDataAccessObject);

        return new LoggedInView(loggedInViewModel, viewManagerModel, portfolioHealthController);

    }

    private static PortfolioHealthController createPortfolioHealthController(
            ViewManagerModel viewManagerModel,
            PortfolioHealthViewModel portfolioHealthViewModel,
            StockDataAccessInterface stockDataAccessObject) {

        final PortfolioHealthOutputBoundary portfolioHealthPresenter =
                new PortfolioHealthPresenter(viewManagerModel, portfolioHealthViewModel);

        final PortfolioHealthInteractor portfolioHealthInteractor = new PortfolioHealthInteractor(stockDataAccessObject, portfolioHealthPresenter
        );

        return new PortfolioHealthController(portfolioHealthInteractor);
    }

    private static ChangePasswordController createChangePasswordUseCase(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            ChangePasswordUserDataAccessInterface userDataAccessObject) {

        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel);

        final UserFactory userFactory = new CommonUserFactory();

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        return new ChangePasswordController(changePasswordInteractor);
    }
}