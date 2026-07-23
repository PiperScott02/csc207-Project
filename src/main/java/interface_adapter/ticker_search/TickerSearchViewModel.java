package interface_adapter.ticker_search;

import interface_adapter.ViewModel;

public class TickerSearchViewModel extends ViewModel<TickerSearchState> {

    public TickerSearchViewModel() {
        super("ticker search");
        setState(new TickerSearchState());
    }

}
