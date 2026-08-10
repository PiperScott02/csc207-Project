package interface_adapter.ticker_search;

import interface_adapter.ViewManagerModel;
import use_case.ticker_search.TickerSearchOutputBoundary;
import use_case.ticker_search.TickerSearchOutputData;

public class TickerSearchPresenter implements TickerSearchOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final TickerSearchViewModel tickerSearchViewModel;

    public TickerSearchPresenter(ViewManagerModel viewManagerModel, TickerSearchViewModel tickerSearchViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.tickerSearchViewModel = tickerSearchViewModel;
    }

    @Override
    public void prepareSuccessView(TickerSearchOutputData tickerSearchOutputData) {
        final TickerSearchState state = tickerSearchViewModel.getState();

        state.setTickerSearchOutputData(tickerSearchOutputData);
        state.setUseCaseFailed(false);
        state.setErrorMessage("");

        tickerSearchViewModel.setState(state);
        tickerSearchViewModel.firePropertyChanged("ticker search");

        viewManagerModel.setState(tickerSearchViewModel.getViewName());
        viewManagerModel.firePropertyChanged("ticker search");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final TickerSearchState state = tickerSearchViewModel.getState();

        state.setUseCaseFailed(true);
        state.setErrorMessage(errorMessage);

        tickerSearchViewModel.setState(state);
        tickerSearchViewModel.firePropertyChanged("ticker search");

        viewManagerModel.setState(tickerSearchViewModel.getViewName());
        viewManagerModel.firePropertyChanged("ticker search");
    }

}
