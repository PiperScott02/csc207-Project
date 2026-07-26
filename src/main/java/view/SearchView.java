package view;

import interface_adapter.similar_search.SimilarSearchController;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.ticker_search.TickerSearchController;
import interface_adapter.ticker_search.TickerSearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SearchView extends JPanel {

    private static final String SEARCH_VIEW_NAME = "search";

    private final SimilarSearchController similarSearchController;
    private final SimilarSearchViewModel similarSearchViewModel;
    private final TickerSearchController tickerSearchController;
    private final TickerSearchViewModel tickerSearchViewModel;

    public SearchView(SimilarSearchController similarSearchController,
                      SimilarSearchViewModel similarSearchViewModel,
                      TickerSearchController tickerSearchController,
                      TickerSearchViewModel tickerSearchViewModel) {
        this.similarSearchController = similarSearchController;
        this.similarSearchViewModel = similarSearchViewModel;
        this.tickerSearchController = tickerSearchController;
        this.tickerSearchViewModel = tickerSearchViewModel;

        add(createHeader());
        add(createSearchBar());
    }

    private JPanel createHeader() {
        final JPanel headerPanel = new JPanel();

        final JLabel title = new JLabel("Search", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(title);

        return headerPanel;
    }

    private JPanel createSearchBar() {
        final JTextField searchInputField = new JTextField(50);
        final JLabel searchBarLabel = new JLabel("Search");

        final LabelTextPanel searchBarPanel = new LabelTextPanel(searchBarLabel, searchInputField);

        return searchBarPanel;
    }

    public String getViewName() {
        return SEARCH_VIEW_NAME;
    }

}