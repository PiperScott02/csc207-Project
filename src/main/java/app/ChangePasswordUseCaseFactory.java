package app;

import data_access.AlphaVantageNewsDataAccessObject;
import data_access.FileStockDataAccessObject;
import entity.CommonUserFactory;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.black_litterman.BlackLittermanViewModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthPresenter;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import use_case.analysis.BlackLittermanService;
import use_case.black_litterman.BlackLittermanDataAccessInterface;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.news.NewsDataAccessInterface;
import use_case.portfolio_health.PortfolioHealthInteractor;
import use_case.portfolio_health.PortfolioHealthOutputBoundary;
import use_case.stock.StockDataAccessInterface;
import view.LoggedInView;

/**
 * This class contains the static factory function for creating the LoggedInView.
 */
public final class ChangePasswordUseCaseFactory {

    private static final String API_KEY = "API_KEY_PLACEHOLDER";

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
        final BlackLittermanViewModel blackLittermanViewModel = new BlackLittermanViewModel();
        final StockDataAccessInterface stockDataAccessObject = new FileStockDataAccessObject();
        final NewsDataAccessInterface newsDataAccessObject = new AlphaVantageNewsDataAccessObject(API_KEY);

        final PortfolioHealthController portfolioHealthController =
                createPortfolioHealthController(
                        viewManagerModel,
                        portfolioHealthViewModel,
                        stockDataAccessObject,
                        newsDataAccessObject
                );
        final BlackLittermanController blackLittermanController =
                BlackLittermanUseCaseFactory.createBlackLittermanUseCase(
                        viewManagerModel,
                        blackLittermanViewModel,
                        (BlackLittermanDataAccessInterface) stockDataAccessObject,
                        new BlackLittermanService()
                );

        return new LoggedInView(loggedInViewModel, viewManagerModel, portfolioHealthController, blackLittermanController);

    }

    private static PortfolioHealthController createPortfolioHealthController(
            ViewManagerModel viewManagerModel,
            PortfolioHealthViewModel portfolioHealthViewModel,
            StockDataAccessInterface stockDataAccessObject,
            NewsDataAccessInterface newsDataAccessObject) {

        final PortfolioHealthOutputBoundary portfolioHealthPresenter =
                new PortfolioHealthPresenter(viewManagerModel, portfolioHealthViewModel);

        // 2. Pass both DAOs directly into the interactor without casting
        final PortfolioHealthInteractor portfolioHealthInteractor = new PortfolioHealthInteractor(
                stockDataAccessObject,
                newsDataAccessObject,
                portfolioHealthPresenter
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