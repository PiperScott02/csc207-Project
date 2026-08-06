package interface_adapter.add_holding;

import use_case.add_holding.AddHoldingInputBoundary;
import use_case.add_holding.AddHoldingInputData;
import java.time.LocalDate;

/**
 * The Controller for the Add Holding use case, handling user input and triggering the interactor.
 */
public class AddHoldingController {
    private final AddHoldingInputBoundary addHoldingUseCaseInteractor;

    public AddHoldingController(AddHoldingInputBoundary addHoldingUseCaseInteractor) {
        this.addHoldingUseCaseInteractor = addHoldingUseCaseInteractor;
    }

    /**
     * Executes the Add Holding use case with user-specified ticker, shares, and estimated purchase price.
     *
     * @param ticker          the stock ticker symbol entered by the user
     * @param shares          the number of shares owned
     * @param purchaseDate    the estimated purchase date
     */
    public void execute(String ticker, double shares, LocalDate purchaseDate) {
        AddHoldingInputData inputData = new AddHoldingInputData(ticker, shares, purchaseDate);
        addHoldingUseCaseInteractor.execute(inputData);
    }
}