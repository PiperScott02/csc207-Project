package use_case.delete_holding;

import entity.Portfolio;

public class DeleteHoldingOutputData {
    private final Portfolio portfolio;
    private final String message;

    public DeleteHoldingOutputData(Portfolio portfolio, String message) {
        this.portfolio = portfolio;
        this.message = message;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public String getMessage() {
        return message;
    }
}