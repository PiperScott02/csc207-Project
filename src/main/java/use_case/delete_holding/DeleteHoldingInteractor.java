package use_case.delete_holding;

import entity.User;

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
        String username = dataAccessObject.getCurrentUser();
        User user = dataAccessObject.get(username);

        if (user == null || user.getPortfolio() == null) {
            outputBoundary.prepareFailView("User or portfolio not found.");
            return;
        }

        boolean removed = user.getPortfolio().removeHoldingByTicker(inputData.getTicker());

        if (removed) {
            dataAccessObject.save(user); // Persists changes to CSV via your DAO
            DeleteHoldingOutputData outputData = new DeleteHoldingOutputData(user.getPortfolio(), "Holding deleted successfully.");
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Could not find holding to delete.");
        }
    }
}