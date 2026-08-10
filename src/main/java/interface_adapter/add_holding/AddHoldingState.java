package interface_adapter.add_holding;

/**
 * The state for the Add Holding View Model.
 */
public class AddHoldingState {
    private String ticker = "";
    private String shares = "";
    private String purchaseDate = "";
    private String addHoldingError = null;

    public AddHoldingState(AddHoldingState copy) {
        this.ticker = copy.ticker;
        this.shares = copy.shares;
        this.purchaseDate = copy.purchaseDate;
        this.addHoldingError = copy.addHoldingError;
    }

    public AddHoldingState() {}

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getAddHoldingError() {
        return addHoldingError;
    }

    public void setAddHoldingError(String addHoldingError) {
        this.addHoldingError = addHoldingError;
    }
}