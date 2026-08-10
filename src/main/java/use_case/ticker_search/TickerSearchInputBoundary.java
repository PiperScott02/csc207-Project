package use_case.ticker_search;

/**
 * Input Boundary for actions which are related to direct ticker search.
 */
public interface TickerSearchInputBoundary {

    /**
     * Executes ticker search use case.
     * @param tickerSearchInputData input data for ticker search
     */
    void execute(TickerSearchInputData tickerSearchInputData);

}
