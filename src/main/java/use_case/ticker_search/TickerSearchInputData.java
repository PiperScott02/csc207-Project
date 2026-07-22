package use_case.ticker_search;

/**
 * The Input Data for the Ticker Search Use Case.
 */
public class TickerSearchInputData {

    private final String tickerSymbol;

    public TickerSearchInputData(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }
}
