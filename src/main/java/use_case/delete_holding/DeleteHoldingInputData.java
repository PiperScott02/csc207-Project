package use_case.delete_holding;

public class DeleteHoldingInputData {
    private final String ticker;

    public DeleteHoldingInputData(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }
}