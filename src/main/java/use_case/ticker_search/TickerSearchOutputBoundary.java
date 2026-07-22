package use_case.ticker_search;

/**
 * The output boundary for the Ticker Search Use Case.
 */
public interface TickerSearchOutputBoundary {

    /**
     * Prepares the success view for the Ticker Search Use Case.
     * @param tickerSearchOutputData
     */
    void prepareSuccessView(TickerSearchOutputData tickerSearchOutputData);

    /**
     * Prepares the success view for the Ticker Search Use Case.
     * @param errorMessage
     */
    void prepareFailView(String errorMessage);
}
