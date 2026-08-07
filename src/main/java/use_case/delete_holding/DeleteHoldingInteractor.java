package use_case.delete_holding;

import entity.User;
import entity.Portfolio;

public class DeleteHoldingInteractor implements DeleteHoldingInputBoundary {
    private final DeleteHoldingUserDataAccessInterface dataAccessObject;
    private final DeleteHoldingOutputBoundary outputBoundary;

    public DeleteHoldingInteractor(DeleteHoldingUserDataAccessInterface dataAccessObject,
                                   DeleteHoldingOutputBoundary outputBoundary) {
        this.dataAccessObject = dataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteHoldingInputData inputData) {
        String tickerToDelete = inputData.getTicker();

        // Get current username, then fetch the User object using dataAccessObject.get(...)
        String username = dataAccessObject.getCurrentUser();
        User currentUser = dataAccessObject.get(username);

        if (currentUser != null && currentUser.getPortfolio() != null) {
            Portfolio portfolio = currentUser.getPortfolio();

            // Remove the holding matching the ticker symbol
            portfolio.getHoldings().removeIf(holding ->
                    holding.getStock() != null &&
                            holding.getStock().getTickerSymbol().equalsIgnoreCase(tickerToDelete)
            );

            // Save the updated user data to persistent storage (CSV)
            dataAccessObject.save(currentUser);

            // Pass the updated portfolio to output data
            DeleteHoldingOutputData outputData = new DeleteHoldingOutputData(portfolio, "Holding deleted successfully.");
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Could not find current user or portfolio.");
        }
    }
}