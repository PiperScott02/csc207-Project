package use_case.stock;

/** The input data for the Stock use case. */
public class StockInputData {
    private final String ticker;

    /** Constructs a new StockInputData object with the provided stock ticker.
     * @param ticker the stock ticker symbol.
     */
    public StockInputData(String ticker) {
        this.ticker = ticker;
    }

    /** Returns the stock ticker symbol.
     * @return the ticker string.
     */
    public String getTickerSymbol() {
        return ticker;
    }
}
