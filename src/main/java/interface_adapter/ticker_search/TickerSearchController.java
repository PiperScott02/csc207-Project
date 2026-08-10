package interface_adapter.ticker_search;

import use_case.ticker_search.TickerSearchInputBoundary;
import use_case.ticker_search.TickerSearchInputData;

import java.io.IOException;

/**
 * The Controller for the Ticker Search Use Case.
 */
public class TickerSearchController {

    private final TickerSearchInputBoundary tickerSearchInputBoundary;

    public TickerSearchController(TickerSearchInputBoundary tickerSearchInputBoundary) {
        this.tickerSearchInputBoundary = tickerSearchInputBoundary;
    }

    /**
     * Executes the Ticker Search Use Case.
     * @param tickerSymbol the ticker symbol to search
     */
    public void execute(String tickerSymbol) {
        final TickerSearchInputData tickerSearchInputData = new TickerSearchInputData(tickerSymbol);
        tickerSearchInputBoundary.execute(tickerSearchInputData);
    }
}
