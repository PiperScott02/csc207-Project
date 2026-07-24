package interface_adapter.portfolio_health;

import interface_adapter.ViewModel;

/** ViewModel for managing and observing the state of portfolio health information in the user interface. */
public class PortfolioHealthViewModel extends ViewModel<PortfolioHealthState> {

    public static final String TITLE_LABEL = "Portfolio Health";

    /**
     * Constructs a new PortfolioHealthViewModel with the view name matching the view and initializes its default state.
     */
    public PortfolioHealthViewModel() {
        super("portfolioHealth view"); // Must match PortfolioHealthView's viewName string!
        setState(new PortfolioHealthState());
    }
}