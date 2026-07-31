package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.risk_preference.RiskPreferenceController;
import interface_adapter.risk_preference.RiskPreferencePresenter;
import interface_adapter.risk_preference.RiskPreferenceViewModel;
import use_case.risk_preference.RiskPreferenceInputBoundary;
import use_case.risk_preference.RiskPreferenceInteractor;
import use_case.risk_preference.RiskPreferenceOutputBoundary;
import use_case.risk_preference.RiskPreferenceUserDataAccessInterface;
import view.RiskPreferenceView;

/**
 * Factory for creating and wiring the Risk Preference feature.
 */
public final class RiskPreferenceUseCaseFactory {

    /**
     * Prevents this utility class from being instantiated.
     */
    private RiskPreferenceUseCaseFactory() {
    }

    /**
     * Creates the fully wired RiskPreferenceView.
     *
     * @param viewManagerModel controls navigation between views
     * @param riskPreferenceViewModel stores the risk-preference UI state
     * @param userDataAccessObject provides access to the current user
     * @return the completed RiskPreferenceView
     */
    public static RiskPreferenceView create(
            ViewManagerModel viewManagerModel,
            RiskPreferenceViewModel riskPreferenceViewModel,
            RiskPreferenceUserDataAccessInterface userDataAccessObject) {

        final RiskPreferenceController controller =
                createRiskPreferenceUseCase(
                        riskPreferenceViewModel,
                        userDataAccessObject
                );

        return new RiskPreferenceView(
                viewManagerModel,
                controller,
                riskPreferenceViewModel
        );
    }

    /**
     * Creates the presenter, interactor, and controller.
     *
     * @param riskPreferenceViewModel view model updated by the presenter
     * @param userDataAccessObject user data access implementation
     * @return the risk-preference controller
     */
    private static RiskPreferenceController createRiskPreferenceUseCase(
            RiskPreferenceViewModel riskPreferenceViewModel,
            RiskPreferenceUserDataAccessInterface userDataAccessObject) {

        final RiskPreferenceOutputBoundary presenter =
                new RiskPreferencePresenter(
                        riskPreferenceViewModel
                );

        final RiskPreferenceInputBoundary interactor =
                new RiskPreferenceInteractor(
                        userDataAccessObject,
                        presenter
                );

        return new RiskPreferenceController(interactor);
    }
}