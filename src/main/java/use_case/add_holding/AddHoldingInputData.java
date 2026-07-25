package use_case.add_holding;

/**
 * The input data for the Add Holding Use Case.
 */
public class AddHoldingInputData {
    private final String ticker;
    private final double shares;
    private final double purchasePrice;

    public AddHoldingInputData(String ticker, double shares, double purchasePrice) {
        this.ticker = ticker;
        this.shares = shares;
        this.purchasePrice = purchasePrice;
    }

    public String getTicker() { return ticker; }

    public double getShares() { return shares; }

    public double getPurchasePrice() { return purchasePrice; }
}
