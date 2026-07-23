package view;

import interface_adapter.similar_search.SimilarSearchController;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.ticker_search.TickerSearchController;
import interface_adapter.ticker_search.TickerSearchViewModel;

import javax.swing.*;

public class SearchView extends JPanel {

    private static final String SEARCH_VIEW_NAME = "search";

    private final SimilarSearchController similarSearchController;
    private final SimilarSearchViewModel similarSearchViewModel;
    private final TickerSearchController tickerSearchController;
    private final TickerSearchViewModel tickerSearchViewModel;

    public SearchView(SimilarSearchController similarSearchController, SimilarSearchViewModel similarSearchViewModel, TickerSearchController tickerSearchController, TickerSearchViewModel tickerSearchViewModel) {
        this.similarSearchController = similarSearchController;
        this.similarSearchViewModel = similarSearchViewModel;
        this.tickerSearchController = tickerSearchController;
        this.tickerSearchViewModel = tickerSearchViewModel;
    }

    // TODO: methods for UI

}