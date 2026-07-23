package interface_adapter.ticker_search;


import use_case.ticker_search.TickerSearchOutputData;

public class TickerSearchState {
    private TickerSearchOutputData tickerSearchOutputData;
    private boolean useCaseFailed;

    public TickerSearchOutputData getTickerSearchOutputData() {
        return tickerSearchOutputData;
    }

    public void setTickerSearchOutputData(TickerSearchOutputData tickerSearchOutputData) {
        this.tickerSearchOutputData = tickerSearchOutputData;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    public void setUseCaseFailed(boolean useCaseFailed) {
        this.useCaseFailed = useCaseFailed;
    }
}
