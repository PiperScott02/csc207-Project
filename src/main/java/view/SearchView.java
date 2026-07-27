package view;

import interface_adapter.similar_search.SimilarSearchController;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.ticker_search.TickerSearchController;
import interface_adapter.ticker_search.TickerSearchState;
import interface_adapter.ticker_search.TickerSearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class SearchView extends JPanel implements PropertyChangeListener {

    private static final String SEARCH_VIEW_NAME = "search";

    // The Display Text Fields for the Ticker Search Result
    private final JButton tickerSearchSymbol = new JButton("N/A");
    private final JLabel tickerSearchCompanyName = new JLabel("N/A");
    private final JLabel tickerSearchCountry = new JLabel("N/A");
    private final JLabel tickerSearchIndustry = new JLabel("N/A");
    private final JLabel tickerSearchPreviousClose = new JLabel("N/A");

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
        tickerSearchViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(15, 15));

        add(createHeader(), BorderLayout.NORTH);

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(createSearchBar());
        centerPanel.add(tickerSearchResult());
        add(centerPanel, BorderLayout.CENTER);

        // Adding Buffer around Left, Right, and Bottom of Search View
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        final JPanel headerPanel = new JPanel();

        final JLabel title = new JLabel("Search", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(title);

        return headerPanel;
    }

    private JPanel createSearchBar() {
        final JTextField searchInputField = new JTextField(50);

        final JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tickerSearchController.execute(searchInputField.getText());
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException");
                } catch (IOException ex) {
                    System.out.println("IOException");
                }
                System.out.println("YOU SEARCHED: " + searchInputField.getText());
            }
        });

        final JPanel searchPanel = new JPanel();
        searchPanel.add(searchButton);
        searchPanel.add(searchInputField);

        return searchPanel;
    }

    public JPanel tickerSearchResult() {
        final JPanel tickerSearchResultPanel = new JPanel();
        tickerSearchResultPanel.setLayout(
                new BoxLayout(tickerSearchResultPanel, BoxLayout.Y_AXIS));

        final JPanel tickerSearchValuesPanel = new JPanel();
        tickerSearchValuesPanel.setLayout(new GridLayout(2, 5));

        tickerSearchValuesPanel.add(new JLabel("Ticker Symbol"));
        tickerSearchValuesPanel.add(new JLabel("Country"));
        tickerSearchValuesPanel.add(new JLabel("Company Name"));
        tickerSearchValuesPanel.add(new JLabel("Industry"));
        tickerSearchValuesPanel.add(new JLabel("Previous Close"));

        tickerSearchValuesPanel.add(tickerSearchSymbol);
        tickerSearchValuesPanel.add(tickerSearchCountry);
        tickerSearchValuesPanel.add(tickerSearchCompanyName);
        tickerSearchValuesPanel.add(tickerSearchIndustry);
        tickerSearchValuesPanel.add(tickerSearchPreviousClose);

        tickerSearchValuesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        final JLabel tickerSearchLabel = new JLabel("Ticker Search Results");

        tickerSearchResultPanel.add(tickerSearchLabel);
        tickerSearchResultPanel.add(tickerSearchValuesPanel);

        return tickerSearchResultPanel;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        final TickerSearchState state = (TickerSearchState) evt.getNewValue();

        tickerSearchSymbol.setText(state.getTickerSymbol());
        tickerSearchCompanyName.setText(state.getCompanyName());
        tickerSearchCountry.setText(state.getCountry());
        tickerSearchPreviousClose.setText(state.getPreviousClose().toPlainString());
        tickerSearchIndustry.setText(state.getIndustry());
    }

    public String getViewName() {
        return SEARCH_VIEW_NAME;
    }

}