package use_case.add_holding;

import java.time.LocalDate;

/**
 * The input data for the Add Holding Use Case.
 */
public class AddHoldingInputData {
    private final String ticker;
    private final double shares;
    private final LocalDate purchaseDate;

    public AddHoldingInputData(String ticker, double shares, LocalDate purchaseDate) {
        this.ticker = ticker;
        this.shares = shares;
        this.purchaseDate = purchaseDate;
    }

    public String getTicker() { return ticker; }

    public double getShares() { return shares; }

    public LocalDate getPurchaseDate () { return purchaseDate; }
}
