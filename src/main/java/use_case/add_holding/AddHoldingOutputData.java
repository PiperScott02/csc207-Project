package use_case.add_holding;

import entity.StockHolding;
import java.util.List;

/**
 * The output data for the Add Holding Use Case.
 */
public class AddHoldingOutputData {
    private final String ticker;
    private final double shares;
    private final List<StockHolding> holdings;
    private final boolean useCaseFailed;

    public AddHoldingOutputData(String ticker, double shares, List<StockHolding> holdings,
                                boolean useCaseFailed) {
        this.ticker = ticker;
        this.shares = shares;
        this.holdings = holdings;
        this.useCaseFailed = useCaseFailed;
    }

    public String getTicker() {
        return ticker;
    }

    public double getShares() {
        return shares;
    }

    public List<StockHolding> getHoldings() { return holdings; }

    public boolean isUseCaseFailed() { return useCaseFailed; }
}