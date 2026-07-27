package interface_adapter.risk_preference;

import use_case.risk_preference.RiskPreferenceOutputBoundary;
import use_case.risk_preference.RiskPreferenceOutputData;

/**
 * Presenter for the risk-preference use case.
 */
public class RiskPreferencePresenter
        implements RiskPreferenceOutputBoundary {

    private final RiskPreferenceViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the risk-preference view model
     */
    public RiskPreferencePresenter(
            RiskPreferenceViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(
            RiskPreferenceOutputData outputData) {

        final RiskPreferenceState state =
                viewModel.getState();

        state.setRiskLevel(outputData.getRiskLevel());
        state.setInvestmentGoals(
                outputData.getInvestmentGoals());
        state.setTimeHorizon(
                outputData.getTimeHorizon());
        state.setLastUpdated(
                outputData.getLastUpdated());
        state.setError("");
        state.setMessage(
                "Preferences saved successfully.");

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final RiskPreferenceState state =
                viewModel.getState();

        state.setError(error);
        state.setMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}