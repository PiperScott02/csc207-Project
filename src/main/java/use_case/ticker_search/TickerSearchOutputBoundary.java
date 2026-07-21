package use_case.ticker_search;

public interface TickerSearchOutputBoundary {

    void prepareSuccessView(TickerSearchOutputData tickerSearchOutputData);

    void prepareFailView(String errorMessage);

}
