package use_case.stock;

/** The output data for the Stock use case, containing the formatted results to be presented to the view model. */
public class StockOutputData {
    private final String ticker;
    private final String companyName;
    private final String close;
    private final String beta;
    private final String alpha;
    private final String sharpeRatio;
    private final boolean useCaseFailed;

    /** Constructs a new StockOutputData object with stock details, financial metrics, and failure status.
     * @param ticker the stock ticker symbol.
     * @param companyName the name of the company.
     * @param close the closing price string.
     * @param beta the beta metric string.
     * @param alpha the alpha metric string.
     * @param sharpeRatio the Sharpe ratio metric string.
     * @param useCaseFailed boolean indicating whether the operation failed.
     */
    public StockOutputData(String ticker, String companyName, String close,
                           String beta, String alpha, String sharpeRatio, boolean useCaseFailed) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.close = close;
        this.beta = beta;
        this.alpha = alpha;
        this.sharpeRatio = sharpeRatio;
        this.useCaseFailed = useCaseFailed;
    }

    /** Returns the stock ticker symbol.
     * @return the ticker string.
     */
    public String getTickerSymbol() {
        return ticker;
    }

    /** Returns the company name.
     * @return the company name string.
     */
    public String getCompanyName() {
        return companyName;
    }

    /** Returns the closing price string.
     * @return the close price string.
     */
    public String getClose() {
        return close;
    }

    /** Returns the beta value string.
     * @return the beta string.
     */
    public String getBeta() {
        return beta;
    }

    /** Returns the alpha value string.
     * @return the alpha string.
     */
    public String getAlpha() {
        return alpha;
    }

    /** Returns the Sharpe ratio value string.
     * @return the Sharpe ratio string.
     */
    public String getSharpeRatio() {
        return sharpeRatio;
    }

    /** Returns whether the use case failed.
     * @return true if failed, false otherwise.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}