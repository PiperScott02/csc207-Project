package interface_adapter.ticker_search;


import use_case.ticker_search.TickerSearchOutputData;

import java.math.BigDecimal;

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

    public String getTickerSymbol() {
        return tickerSearchOutputData.getTickerSymbol();
    }

    public String getCompanyName() {
        return tickerSearchOutputData.getCompanyName();
    }

    public BigDecimal getPreviousClose() {
        return tickerSearchOutputData.getPreviousClose();
    }

    public String getCountry() {
        return tickerSearchOutputData.getCountry();
    }

    public String getIndustry() {
        return tickerSearchOutputData.getIndustry();
    }
}
