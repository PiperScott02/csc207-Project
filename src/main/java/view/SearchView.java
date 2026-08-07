package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.similar_search.SimilarSearchController;
import interface_adapter.similar_search.SimilarSearchState;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockViewModel;
import interface_adapter.ticker_search.TickerSearchController;
import interface_adapter.ticker_search.TickerSearchState;
import interface_adapter.ticker_search.TickerSearchViewModel;
import use_case.similar_search.SimilarSearchOutputData;

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

    // Similar Search Panel
    private final JPanel similarSearchResultsPanel = createSimilarSearchResultsPanel();

    private final SimilarSearchController similarSearchController;
    private final SimilarSearchViewModel similarSearchViewModel;
    private final TickerSearchController tickerSearchController;
    private final TickerSearchViewModel tickerSearchViewModel;

    // Error Message Label
    private final JLabel searchBarErrorMessage = new JLabel("");

    // Stock Navigation Dependencies
    private final StockController stockController;
    private final ViewManagerModel viewManagerModel;
    private final StockViewModel stockViewModel;

    // Return to Portfolio Dependency
    private final LoggedInViewModel loggedInViewModel;

    public SearchView(SimilarSearchController similarSearchController,
                      SimilarSearchViewModel similarSearchViewModel,
                      TickerSearchController tickerSearchController,
                      TickerSearchViewModel tickerSearchViewModel,
                      StockController stockController,
                      ViewManagerModel viewManagerModel,
                      StockViewModel stockViewModel,
                      LoggedInViewModel loggedInViewModel) {
        this.similarSearchController = similarSearchController;
        this.similarSearchViewModel = similarSearchViewModel;
        this.tickerSearchController = tickerSearchController;
        this.tickerSearchViewModel = tickerSearchViewModel;
        this.stockController = stockController;
        this.viewManagerModel = viewManagerModel;
        this.stockViewModel = stockViewModel;
        this.loggedInViewModel = loggedInViewModel;

        searchBarErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        searchBarErrorMessage.setForeground(Color.RED);
        searchBarErrorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        tickerSearchViewModel.addPropertyChangeListener(this);
        similarSearchViewModel.addPropertyChangeListener(this);

        // Click main ticker button -> Execute StockUseCase & Switch to Stock View
        tickerSearchSymbol.addActionListener(e -> {
            String ticker = tickerSearchSymbol.getText();
            if (!ticker.equals("N/A") && !ticker.trim().isEmpty()) {
                this.stockController.execute(ticker);
                this.viewManagerModel.setState(this.stockViewModel.getViewName());
                this.viewManagerModel.firePropertyChanged();
            }
        });

        setLayout(new BorderLayout(15, 15));

        add(createHeader(), BorderLayout.NORTH);

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(createSearchBar());
        centerPanel.add(Box.createVerticalStrut(2));
        centerPanel.add(tickerSearchResult());
        centerPanel.add(Box.createVerticalStrut(2));
        centerPanel.add(createSimilarSearchPanel(similarSearchResultsPanel));

        add(centerPanel, BorderLayout.CENTER);

        // Adding Buffer around Left and Right of Search View
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);

        // Adding Button to return to Portfolio View (a.k.a Logged in View)
        final JPanel southPanel = new JPanel();
        final JButton backButton = new JButton("Return to Portfolio View");
        backButton.addActionListener(e -> {
            this.viewManagerModel.setState(this.loggedInViewModel.getViewName());
            this.viewManagerModel.firePropertyChanged();
        });
        southPanel.add(backButton);
        add(southPanel, BorderLayout.SOUTH);
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
                String searchText = searchInputField.getText();
                if (searchText.contains(" ") || searchText.isEmpty()) {
                    searchBarErrorMessage.setText("ERROR: search contains space or search is empty.");
                } else {
                    searchBarErrorMessage.setText("");
                    try {
                        tickerSearchController.execute(searchInputField.getText());
                        similarSearchController.execute(searchInputField.getText());
                    } catch (InterruptedException ex) {
                        System.out.println("InterruptedException");
                    } catch (IOException ex) {
                        System.out.println("IOException");
                    }
                }
            }
        });

        final JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        final JPanel searchBar = new JPanel();
        searchBar.add(searchButton);
        searchBar.add(searchInputField);
        searchBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchPanel.add(searchBar);
        searchPanel.add(searchBarErrorMessage);
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        return searchPanel;
    }

    private JPanel createSimilarSearchPanel(JPanel similarSearchResultsPanel) {
        final JPanel similarSearchPanel = new JPanel();
        similarSearchPanel.setLayout(new BoxLayout(similarSearchPanel, BoxLayout.Y_AXIS));
        similarSearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        similarSearchPanel.add(new JLabel("Similar Search Results"));

        final JPanel topOfSimilarSearchPanel = new JPanel();
        topOfSimilarSearchPanel.setLayout(new GridLayout(1, 5));
        topOfSimilarSearchPanel.add(new JLabel("Ticker Symbol"));
        topOfSimilarSearchPanel.add(new JLabel("Country"));
        topOfSimilarSearchPanel.add(new JLabel("Company Name"));
        topOfSimilarSearchPanel.add(new JLabel("Industry"));
        topOfSimilarSearchPanel.add(new JLabel("Previous Close"));

        similarSearchPanel.add(topOfSimilarSearchPanel);
        similarSearchPanel.add(similarSearchResultsPanel);

        return similarSearchPanel;
    }

    public JPanel tickerSearchResult() {
        final JPanel tickerSearchResultPanel = new JPanel();
        tickerSearchResultPanel.setLayout(
                new BoxLayout(tickerSearchResultPanel, BoxLayout.Y_AXIS));
        tickerSearchResultPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

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

    private JPanel createSimilarSearchResultsPanel() {
        final JPanel similarSearchResultPanel = new JPanel();
        similarSearchResultPanel.setLayout(
                new BoxLayout(similarSearchResultPanel, BoxLayout.Y_AXIS));

        return similarSearchResultPanel;
    }

    private void removeSimilarSearchResults(JPanel similarSearchResultsPanel) {
        similarSearchResultsPanel.removeAll();
        similarSearchResultsPanel.revalidate();
        similarSearchResultsPanel.repaint();
    }

    private void addSimilarSearchResults(JPanel similarSearchResultsPanel,
                                         SimilarSearchOutputData[] similarSearchOutputData) {
        for (SimilarSearchOutputData outputData : similarSearchOutputData) {
            JPanel outputDataPanel = new JPanel();
            outputDataPanel.setLayout(new GridLayout(1, 5));
            outputDataPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JButton symbolButton = new JButton(outputData.getTickerSymbol());
            // Click similar search symbol button -> Execute StockUseCase & Switch to Stock View
            symbolButton.addActionListener(e -> {
                this.stockController.execute(outputData.getTickerSymbol());
                this.viewManagerModel.setState(this.stockViewModel.getViewName());
                this.viewManagerModel.firePropertyChanged();
            });

            outputDataPanel.add(symbolButton);
            outputDataPanel.add(new JLabel(outputData.getCountry()));
            outputDataPanel.add(new JLabel(outputData.getCompanyName()));
            outputDataPanel.add(new JLabel(outputData.getIndustry()));
            outputDataPanel.add(new JLabel(outputData.getPreviousClose().toPlainString()));

            similarSearchResultsPanel.add(outputDataPanel);
            similarSearchResultsPanel.revalidate();
            similarSearchResultsPanel.repaint();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("ticker search")) {
            TickerSearchState tickerSearchState = (TickerSearchState) evt.getNewValue();

            tickerSearchSymbol.setText(tickerSearchState.getTickerSymbol());
            tickerSearchCompanyName.setText(tickerSearchState.getCompanyName());
            tickerSearchCountry.setText(tickerSearchState.getCountry());
            tickerSearchPreviousClose.setText(tickerSearchState.getPreviousClose().toPlainString());
            tickerSearchIndustry.setText(tickerSearchState.getIndustry());

        } else if (evt.getPropertyName().equals("similar search")) {
            SimilarSearchState similarSearchState = (SimilarSearchState) evt.getNewValue();
            removeSimilarSearchResults(similarSearchResultsPanel);
            addSimilarSearchResults(similarSearchResultsPanel,
                    similarSearchState.getSimilarSearchOutputData());
        }
    }

    public String getViewName() {
        return SEARCH_VIEW_NAME;
    }

}