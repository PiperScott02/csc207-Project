package interface_adapter.risk_preference;

import java.util.Set;

import entity.InvestmentGoal;
import entity.RiskLevel;
import entity.TimeHorizon;
import use_case.risk_preference.RiskPreferenceInputBoundary;
import use_case.risk_preference.RiskPreferenceInputData;

/**
 * Controller for saving risk preferences.
 */
public class RiskPreferenceController {

    private final RiskPreferenceInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the risk-preference interactor
     */
    public RiskPreferenceController(
            RiskPreferenceInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Sends the selected preferences to the interactor.
     *
     * @param riskLevel selected risk level
     */
    public void execute(RiskLevel riskLevel) {
        final RiskPreferenceInputData inputData =
                new RiskPreferenceInputData(riskLevel);

        interactor.execute(inputData);
    }

    /**
     * Loads the current user's saved risk preferences.
     */
    public void load() {
        interactor.load();
    }
}