package use_case.ticker_search;

import java.io.IOException;

public interface TickerSearchInputBoundary {

    void execute(TickerSearchInputData tickerSearchInputData) throws IOException, InterruptedException;

}
