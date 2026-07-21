package use_case.ticker_search;

public class TickerSearchInputData {

    private final String tickerSymbol;

    public TickerSearchInputData(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }
}
