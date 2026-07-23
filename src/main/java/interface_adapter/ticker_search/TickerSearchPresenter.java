package interface_adapter.ticker_search;

import use_case.ticker_search.TickerSearchOutputBoundary;
import use_case.ticker_search.TickerSearchOutputData;

public class TickerSearchPresenter implements TickerSearchOutputBoundary {

    private final TickerSearchViewModel tickerSearchViewModel;

    public TickerSearchPresenter(TickerSearchViewModel tickerSearchViewModel) {
        this.tickerSearchViewModel = tickerSearchViewModel;
    }

    @Override
    public void prepareSuccessView(TickerSearchOutputData tickerSearchOutputData) {
        final TickerSearchState state = tickerSearchViewModel.getState();
        state.setTickerSearchOutputData(tickerSearchOutputData);
        state.setUseCaseFailed(false);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final TickerSearchState state = tickerSearchViewModel.getState();
        state.setUseCaseFailed(true);
    }

}
