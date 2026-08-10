package interface_adapter.ticker_search;

import use_case.ticker_search.TickerSearchInputBoundary;
import use_case.ticker_search.TickerSearchInputData;

import java.io.IOException;

public class TickerSearchController {

    private final TickerSearchInputBoundary tickerSearchInputBoundary;

    public TickerSearchController(TickerSearchInputBoundary tickerSearchInputBoundary) {
        this.tickerSearchInputBoundary = tickerSearchInputBoundary;
    }

    public void execute(String tickerSymbol) {
        final TickerSearchInputData tickerSearchInputData = new TickerSearchInputData(tickerSymbol);
        tickerSearchInputBoundary.execute(tickerSearchInputData);
    }
}
