package interface_adapter.add_holding;

import use_case.add_holding.AddHoldingInputBoundary;
import use_case.add_holding.AddHoldingInputData;

/**
 * The Controller for the Add Holding use case, handling user input and triggering the interactor.
 */
public class AddHoldingController {
    private final AddHoldingInputBoundary addHoldingUseCaseInteractor;

    public AddHoldingController(AddHoldingInputBoundary addHoldingInputBoundary) {
        this.addHoldingUseCaseInteractor = addHoldingInputBoundary;
    }

    /**
     * Executes the Add Holding use case with user-specified ticker, shares, and average buy price.
     *
     * @param ticker          the stock ticker symbol entered by the user
     * @param shares          the number of shares owned
     * @param averageBuyPrice the average price paid per share
     */
    public void execute(String ticker, double shares, double averageBuyPrice) {
        final AddHoldingInputData inputData = new AddHoldingInputData(ticker, shares, averageBuyPrice);
        addHoldingUseCaseInteractor.execute(inputData);
    }
}