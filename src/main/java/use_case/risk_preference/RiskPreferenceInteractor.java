package use_case.risk_preference;

import entity.RiskProfile;
import entity.User;

/**
 * Handles saving a user's risk preferences.
 */
public class RiskPreferenceInteractor
        implements RiskPreferenceInputBoundary {

    private final RiskPreferenceUserDataAccessInterface userDataAccessObject;
    private final RiskPreferenceOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param userDataAccessObject user data access
     * @param presenter output presenter
     */
    public RiskPreferenceInteractor(
            RiskPreferenceUserDataAccessInterface userDataAccessObject,
            RiskPreferenceOutputBoundary presenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(RiskPreferenceInputData inputData) {
        if (inputData.getRiskLevel() == null) {
            presenter.prepareFailView("Please select a risk level.");
            return;
        }

        if (inputData.getTimeHorizon() == null) {
            presenter.prepareFailView("Please select a time horizon.");
            return;
        }

        final String username = userDataAccessObject.getCurrentUser();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("No logged-in user was found.");
            return;
        }

        final User user = userDataAccessObject.get(username);

        if (user == null) {
            presenter.prepareFailView("User account was not found.");
            return;
        }

        final RiskProfile riskProfile = new RiskProfile(
                inputData.getRiskLevel(),
                inputData.getInvestmentGoals(),
                inputData.getTimeHorizon()
        );

        user.setRiskProfile(riskProfile);
        userDataAccessObject.save(user);

        final RiskPreferenceOutputData outputData =
                new RiskPreferenceOutputData(
                        riskProfile.getRiskLevel(),
                        riskProfile.getInvestmentGoals(),
                        riskProfile.getTimeHorizon(),
                        riskProfile.getLastUpdated()
                );

        presenter.prepareSuccessView(outputData);
    }
}