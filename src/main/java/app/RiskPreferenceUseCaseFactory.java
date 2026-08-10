package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.risk_preference.RiskPreferenceController;
import interface_adapter.risk_preference.RiskPreferenceViewModel;
import use_case.risk_preference.RiskPreferenceUserDataAccessInterface;
import use_case.risk_preference.RiskPreferenceInputBoundary;
import use_case.risk_preference.RiskPreferenceInteractor;
import use_case.risk_preference.RiskPreferenceOutputBoundary;
import interface_adapter.risk_preference.RiskPreferencePresenter;
import view.RiskPreferenceView;

/**
 * Class for building the Risk Preference use case.
 */
public final class RiskPreferenceUseCaseFactory {

    private RiskPreferenceUseCaseFactory() {
        // Prevent instantiation
    }

    /**
     * Creates the RiskPreferenceView and its dependencies.
     *
     * @param viewManagerModel the view manager model
     * @param riskPreferenceViewModel the risk preference view model
     * @param dataAccessInterface the data access object
     * @param loggedInViewModel the logged in view model
     * @param blackLittermanController the black litterman controller
     * @param portfolioHealthController the portfolio health controller
     * @return the RiskPreferenceView
     */
    public static RiskPreferenceView create(
            ViewManagerModel viewManagerModel,
            RiskPreferenceViewModel riskPreferenceViewModel,
            RiskPreferenceUserDataAccessInterface dataAccessInterface,
            LoggedInViewModel loggedInViewModel,
            BlackLittermanController blackLittermanController,
            PortfolioHealthController portfolioHealthController) {

        final RiskPreferenceOutputBoundary outputBoundary =
                new RiskPreferencePresenter(riskPreferenceViewModel);

        final RiskPreferenceInputBoundary interactor =
                new RiskPreferenceInteractor(dataAccessInterface, outputBoundary);

        final RiskPreferenceController controller =
                new RiskPreferenceController(interactor);

        return new RiskPreferenceView(
                viewManagerModel,
                controller,
                riskPreferenceViewModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController
        );
    }
}