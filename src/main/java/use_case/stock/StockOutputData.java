package use_case.stock;

/** The output data for the Stock use case, containing the formatted results to be presented to the view model. */
public class StockOutputData {
    private final String ticker;
    private final String companyName;
    private final String close;
    private final String dailyPriceChange;
    private final String beta;
    private final String annualizedAlpha;
    private final String annualizedSharpeRatio;
    private final boolean useCaseFailed;

    /** Constructs a new StockOutputData object with stock details, financial metrics, and failure status.
     * @param ticker the stock ticker symbol.
     * @param companyName the name of the company.
     * @param close the closing price string.
     * @param dailyPriceChange the daily price change string.
     * @param beta the beta metric string.
     * @param annualizedAlpha the annualized alpha metric string.
     * @param annualizedSharpeRatio the annualized Sharpe ratio metric string.
     * @param useCaseFailed boolean indicating whether the operation failed.
     */
    public StockOutputData(String ticker, String companyName, String close, String dailyPriceChange,
                           String beta, String annualizedAlpha, String annualizedSharpeRatio, boolean useCaseFailed) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.close = close;
        this.dailyPriceChange = dailyPriceChange;
        this.beta = beta;
        this.annualizedAlpha = annualizedAlpha;
        this.annualizedSharpeRatio = annualizedSharpeRatio;
        this.useCaseFailed = useCaseFailed;
    }

    /** Returns the stock ticker symbol.
     * @return the ticker string.
     */
    public String getTickerSymbol() {
        return ticker;
    }

    /** Returns the daily price change.
     * @return the daily price change string.
     */
    public String getDailyPriceChange() {
        return dailyPriceChange;
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

    /** Returns the annualized alpha value string.
     * @return the annualized alpha string.
     */
    public String getAnnualizedAlpha() {
        return annualizedAlpha;
    }

    /** Returns the annualized Sharpe ratio value string.
     * @return the annualized Sharpe ratio string.
     */
    public String getAnnualizedSharpeRatio() {
        return annualizedSharpeRatio;
    }

    /** Returns whether the use case failed.
     * @return true if failed, false otherwise.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}