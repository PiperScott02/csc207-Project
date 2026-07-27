package interface_adapter.risk_preference;

import interface_adapter.ViewModel;

/**
 * View model for the risk-preference screen.
 */
public class RiskPreferenceViewModel
        extends ViewModel<RiskPreferenceState> {

    public RiskPreferenceViewModel() {
        super("risk preference");
        setState(new RiskPreferenceState());
    }
}