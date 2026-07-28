package interface_adapter.stock;

/** Represents the view state for stock information, holding fields like ticker, company name,
 * close price, metrics, and error messages. */
public class StockState {
    private String ticker = "";
    private String companyName = "";
    private String close = "";
    private String dailyPriceChange = "";
    private String beta = "";
    private String alpha = "";
    private String sharpeRatio = "";
    private String errorMessage = "";

    /** Copy constructor to create a new StockState from an existing one.
     * @param copy the StockState to copy from.
     */
    public StockState(StockState copy) {
        this.ticker = copy.ticker;
        this.companyName = copy.companyName;
        this.close = copy.close;
        this.dailyPriceChange = copy.dailyPriceChange;
        this.beta = copy.beta;
        this.alpha = copy.alpha;
        this.sharpeRatio = copy.sharpeRatio;
        this.errorMessage = copy.errorMessage;
    }

    /** Default constructor to create an empty StockState. */
    public StockState() {
    }

    /** Returns the ticker symbol.
     * @return the ticker string.
     */
    public String getTicker() {
        return ticker;
    }

    /** Returns the company name.
     * @return the company name string.
     */
    public String getCompanyName() {
        return companyName;
    }

    /** Returns the closing price as a string.
     * @return the close price string.
     */
    public String getClose() {
        return close;
    }

    /** Returns the beta value as a string.
     * @return the beta string.
     */
    public String getBeta() {
        return beta;
    }

    /** Returns the alpha value as a string.
     * @return the alpha string.
     */
    public String getAlpha() {
        return alpha;
    }

    /** Returns the Sharpe ratio value as a string.
     * @return the Sharpe ratio string.
     */
    public String getSharpeRatio() {
        return sharpeRatio;
    }

    /** Returns the error message.
     * @return the error message string.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /** Returns the Daily Price Change.
     * @return the Daily Price Change string.
     */
    public String getDailyPriceChange() { return dailyPriceChange;}

    /**  Sets the Daily Price Change.
     * @param dailyPriceChange the daily Price Change to set.
     */
    public void setDailyPriceChange(String dailyPriceChange) { this.dailyPriceChange = dailyPriceChange;}

    /** Sets the ticker symbol.
     * @param ticker the ticker string to set.
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    /** Sets the company name.
     * @param companyName the company name string to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /** Sets the closing price string.
     * @param close the close price string to set.
     */
    public void setClose(String close) {
        this.close = close;
    }

    /** Sets the beta value string.
     * @param beta the beta string to set.
     */
    public void setBeta(String beta) {
        this.beta = beta;
    }

    /** Sets the alpha value string.
     * @param alpha the alpha string to set.
     */
    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    /** Sets the Sharpe ratio value string.
     * @param sharpeRatio the Sharpe ratio string to set.
     */
    public void setSharpeRatio(String sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    /** Sets the error message.
     * @param errorMessage the error message string to set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}