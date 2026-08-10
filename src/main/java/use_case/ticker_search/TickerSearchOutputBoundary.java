package use_case.ticker_search;

/**
 * The output boundary for the Ticker Search Use Case.
 */
public interface TickerSearchOutputBoundary {

    /**
     * Prepares the success view for the Ticker Search Use Case.
     * @param tickerSearchOutputData stock information to be displayed by view
     */
    void prepareSuccessView(TickerSearchOutputData tickerSearchOutputData);

    /**
     * Prepares the success view for the Ticker Search Use Case.
     * @param errorMessage the message to display for the given error
     */
    void prepareFailView(String errorMessage);
}
