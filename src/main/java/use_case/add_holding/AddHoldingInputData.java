package use_case.add_holding;

/**
 * The input data for the Add Holding Use Case.
 */
public class AddHoldingInputData {
    private final String ticker;
    private final double shares;
    private final double averageBuyPrice;

    public AddHoldingInputData(String ticker, double shares, double averageBuyPrice) {
        this.ticker = ticker;
        this.shares = shares;
        this.averageBuyPrice = averageBuyPrice;
    }

    public String getTicker() { return ticker; }

    public double getShares() { return shares; }

    public double getAverageBuyPrice() { return averageBuyPrice; }
}
