package use_case.ticker_search;

import java.io.IOException;

/**
 * Input Boundary for actions which are related to direct ticker search.
 */
public interface TickerSearchInputBoundary {

    /**
     * Executes ticker search use case.
     * @param tickerSearchInputData
     * @throws IOException
     * @throws InterruptedException
     */
    void execute(TickerSearchInputData tickerSearchInputData) throws IOException, InterruptedException;

}
