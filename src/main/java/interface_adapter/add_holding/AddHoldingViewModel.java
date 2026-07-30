package interface_adapter.add_holding;

import interface_adapter.ViewModel; // Adjust import based on your project structure

public class AddHoldingViewModel extends ViewModel {

    public static final String TITLE_LABEL = "Add New Holding";
    public static final String TICKER_LABEL = "Ticker Symbol:";
    public static final String SHARES_LABEL = "Number of Shares:";
    public static final String PRICE_LABEL = "Average Buy Price ($):";

    public static final String ADD_BUTTON_LABEL = "Add Holding";
    public static final String BACK_BUTTON_LABEL = "Back to Dashboard";
    public static final String CLEAR_BUTTON_LABEL = "Clear";

    private AddHoldingState state = new AddHoldingState();

    public AddHoldingViewModel() {
        super("add holding");
    }

    public void setState(AddHoldingState state) {
        this.state = state;
    }

    public AddHoldingState getState() {
        return state;
    }
}