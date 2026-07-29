package interface_adapter.news;

import interface_adapter.ViewModel;

/**
 * The view model for the stock news page.
 */
public class NewsViewModel extends ViewModel<NewsState> {

    public static final String TITLE_LABEL = "Stock News Sentiment";
    public static final String SEARCH_BUTTON_LABEL = "Search";
    public static final String TICKER_LABEL = "Ticker";

    public NewsViewModel() {
        super("news");
        setState(new NewsState());
    }
}