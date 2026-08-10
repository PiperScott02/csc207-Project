package interface_adapter.ticker_search;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Ticker Search Use Case.
 */
public class TickerSearchViewModel extends ViewModel<TickerSearchState> {

    public TickerSearchViewModel() {
        super("ticker search");
        setState(new TickerSearchState());
    }

}
