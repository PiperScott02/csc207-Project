package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
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

/**
 * The screen for searching stocks and viewing search/similar results with dark theme and sidebar.
 */
public class SearchView extends JPanel implements PropertyChangeListener {

    private static final String SEARCH_VIEW_NAME = "search";
    private static final String LOGGED_IN_VIEW_NAME = "logged in";

    // === DARK MODE UI PALETTE ===
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);

    private final JButton searchButton = new JButton("Search");
    private final JButton tickerSearchSymbol = new JButton("N/A");
    private final JLabel tickerSearchCompanyName = new JLabel("N/A");
    private final JLabel tickerSearchCountry = new JLabel("N/A");
    private final JLabel tickerSearchIndustry = new JLabel("N/A");
    private final JLabel tickerSearchPreviousClose = new JLabel("N/A");
    private final JTextField searchInputField = new JTextField(30);

    private final JPanel similarSearchResultsPanel = createSimilarSearchResultsPanel();

    private final SimilarSearchController similarSearchController;
    private final SimilarSearchViewModel similarSearchViewModel;
    private final TickerSearchController tickerSearchController;
    private final TickerSearchViewModel tickerSearchViewModel;
    private final StockController stockController;
    private final ViewManagerModel viewManagerModel;
    private final StockViewModel stockViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final BlackLittermanController blackLittermanController;
    private final PortfolioHealthController portfolioHealthController;

    public SearchView(SimilarSearchController similarSearchController,
                      SimilarSearchViewModel similarSearchViewModel,
                      TickerSearchController tickerSearchController,
                      TickerSearchViewModel tickerSearchViewModel,
                      StockController stockController,
                      ViewManagerModel viewManagerModel,
                      StockViewModel stockViewModel,
                      LoggedInViewModel loggedInViewModel,
                      BlackLittermanController blackLittermanController,
                      PortfolioHealthController portfolioHealthController) {
        this.similarSearchController = similarSearchController;
        this.similarSearchViewModel = similarSearchViewModel;
        this.tickerSearchController = tickerSearchController;
        this.tickerSearchViewModel = tickerSearchViewModel;
        this.stockController = stockController;
        this.viewManagerModel = viewManagerModel;
        this.stockViewModel = stockViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.blackLittermanController = blackLittermanController;
        this.portfolioHealthController = portfolioHealthController;

        tickerSearchViewModel.addPropertyChangeListener(this);
        similarSearchViewModel.addPropertyChangeListener(this);
        loggedInViewModel.addPropertyChangeListener(this);

        tickerSearchSymbol.addActionListener(e -> {
            String ticker = tickerSearchSymbol.getText();
            if (!ticker.equals("N/A") && !ticker.trim().isEmpty()) {
                this.stockController.execute(ticker);
                this.viewManagerModel.setState(this.stockViewModel.getViewName());
                this.viewManagerModel.firePropertyChanged();
            }
        });

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(SidebarHelper.createSidebar("Search Stocks",
                this,
                viewManagerModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainContentPanel() {
        final JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        final JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBackground(BG_DARK);

        centerContainer.add(createSearchBar());
        centerContainer.add(Box.createVerticalStrut(20));
        centerContainer.add(tickerSearchResult());
        centerContainer.add(Box.createVerticalStrut(20));
        centerContainer.add(createSimilarSearchPanel(similarSearchResultsPanel));

        final JScrollPane scrollPane = new JScrollPane(centerContainer);
        scrollPane.setBackground(BG_DARK);
        scrollPane.getViewport().setBackground(BG_DARK);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createHeader() {
        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BG_DARK);

        final JLabel backButton = new JLabel("← Return to Portfolio View");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setForeground(TEXT_MUTED);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
                viewManagerModel.firePropertyChanged();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setForeground(TEXT_MAIN);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setForeground(TEXT_MUTED);
            }
        });

        final JLabel title = new JLabel("Search Stocks");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(backButton);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(15));

        return headerPanel;
    }

    private JPanel createSearchBar() {
        final JPanel searchPanel = new JPanel(null);
        searchPanel.setBackground(CARD_BG);
        searchPanel.setPreferredSize(new Dimension(750, 95));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        final JLabel queryLabel = new JLabel("QUERY");
        queryLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        queryLabel.setForeground(TEXT_MUTED);
        queryLabel.setBounds(20, 12, 100, 15);

        searchInputField.setBackground(BG_DARK);
        searchInputField.setForeground(TEXT_MAIN);
        searchInputField.setCaretColor(TEXT_MAIN);
        searchInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchInputField.setBounds(20, 32, 590, 36);

        searchButton.setBackground(ACCENT_GREEN);
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setOpaque(true);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(625, 32, 100, 36);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tickerSearchController.execute(searchInputField.getText());
                    similarSearchController.execute(searchInputField.getText());
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException");
                } catch (IOException ex) {
                    System.out.println("IOException");
                }
            }
        });

        if (searchInputField.getActionListeners().length == 0) {
            searchInputField.addActionListener(searchButton.getActionListeners()[0]);
        }

        searchPanel.add(queryLabel);
        searchPanel.add(searchInputField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    public JPanel tickerSearchResult() {
        final JPanel tickerSearchResultPanel = new JPanel();
        tickerSearchResultPanel.setLayout(
                new BoxLayout(tickerSearchResultPanel, BoxLayout.Y_AXIS));
        tickerSearchResultPanel.setBackground(BG_DARK);
        tickerSearchResultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel tickerSearchLabel = new JLabel("TICKER SEARCH RESULTS");
        tickerSearchLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        tickerSearchLabel.setForeground(TEXT_MUTED);
        tickerSearchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JPanel tickerSearchValuesPanel = new JPanel(new GridLayout(2, 5, 10, 5));
        tickerSearchValuesPanel.setBackground(CARD_BG);
        tickerSearchValuesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        tickerSearchValuesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        tickerSearchValuesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        addHeaderLabel(tickerSearchValuesPanel, "TICKER SYMBOL");
        addHeaderLabel(tickerSearchValuesPanel, "COUNTRY");
        addHeaderLabel(tickerSearchValuesPanel, "COMPANY NAME");
        addHeaderLabel(tickerSearchValuesPanel, "INDUSTRY");
        addHeaderLabel(tickerSearchValuesPanel, "PREVIOUS CLOSE");

        tickerSearchSymbol.setBackground(CARD_BG);
        tickerSearchSymbol.setForeground(ACCENT_GREEN);
        tickerSearchSymbol.setFont(new Font("SansSerif", Font.BOLD, 13));
        tickerSearchSymbol.setBorderPainted(false);
        tickerSearchSymbol.setFocusPainted(false);
        tickerSearchSymbol.setHorizontalAlignment(SwingConstants.LEFT);
        tickerSearchSymbol.setCursor(new Cursor(Cursor.HAND_CURSOR));

        styleResultLabel(tickerSearchCountry);
        styleResultLabel(tickerSearchCompanyName);
        styleResultLabel(tickerSearchIndustry);
        styleResultLabel(tickerSearchPreviousClose);

        tickerSearchValuesPanel.add(tickerSearchSymbol);
        tickerSearchValuesPanel.add(tickerSearchCountry);
        tickerSearchValuesPanel.add(tickerSearchCompanyName);
        tickerSearchValuesPanel.add(tickerSearchIndustry);
        tickerSearchValuesPanel.add(tickerSearchPreviousClose);

        tickerSearchResultPanel.add(tickerSearchLabel);
        tickerSearchResultPanel.add(Box.createVerticalStrut(8));
        tickerSearchResultPanel.add(tickerSearchValuesPanel);

        return tickerSearchResultPanel;
    }

    private JPanel createSimilarSearchResultsPanel() {
        final JPanel similarSearchResultPanel = new JPanel();
        similarSearchResultPanel.setLayout(
                new BoxLayout(similarSearchResultPanel, BoxLayout.Y_AXIS));
        similarSearchResultPanel.setBackground(CARD_BG);
        similarSearchResultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return similarSearchResultPanel;
    }

    private JPanel createSimilarSearchPanel(JPanel similarSearchResultsPanel) {
        final JPanel similarSearchPanel = new JPanel();
        similarSearchPanel.setLayout(new BoxLayout(similarSearchPanel, BoxLayout.Y_AXIS));
        similarSearchPanel.setBackground(BG_DARK);
        similarSearchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel sectionLabel = new JLabel("SIMILAR SEARCH RESULTS");
        sectionLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        sectionLabel.setForeground(TEXT_MUTED);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JPanel tableCard = new JPanel();
        tableCard.setLayout(new BoxLayout(tableCard, BoxLayout.Y_AXIS));
        tableCard.setBackground(CARD_BG);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JPanel topOfSimilarSearchPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        topOfSimilarSearchPanel.setBackground(CARD_BG);

        topOfSimilarSearchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topOfSimilarSearchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        addHeaderLabel(topOfSimilarSearchPanel, "TICKER SYMBOL");
        addHeaderLabel(topOfSimilarSearchPanel, "COUNTRY");
        addHeaderLabel(topOfSimilarSearchPanel, "COMPANY NAME");
        addHeaderLabel(topOfSimilarSearchPanel, "INDUSTRY");
        addHeaderLabel(topOfSimilarSearchPanel, "PREVIOUS CLOSE");

        tableCard.add(topOfSimilarSearchPanel);
        tableCard.add(Box.createVerticalStrut(10));
        tableCard.add(similarSearchResultsPanel);

        similarSearchPanel.add(sectionLabel);
        similarSearchPanel.add(Box.createVerticalStrut(8));
        similarSearchPanel.add(tableCard);

        return similarSearchPanel;
    }

    private void removeSimilarSearchResults(JPanel similarSearchResultsPanel) {
        similarSearchResultsPanel.removeAll();
        similarSearchResultsPanel.revalidate();
        similarSearchResultsPanel.repaint();
    }

    private void addSimilarSearchResults(JPanel similarSearchResultsPanel,
                                         SimilarSearchOutputData[] similarSearchOutputData) {
        for (SimilarSearchOutputData outputData : similarSearchOutputData) {
            final JPanel outputDataPanel = new JPanel(new GridLayout(1, 5, 10, 5));
            outputDataPanel.setBackground(CARD_BG);
            outputDataPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
            outputDataPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            final JButton symbolButton = new JButton(outputData.getTickerSymbol());
            symbolButton.setBackground(CARD_BG);
            symbolButton.setForeground(ACCENT_GREEN);
            symbolButton.setFont(new Font("SansSerif", Font.BOLD, 13));
            symbolButton.setBorderPainted(false);
            symbolButton.setFocusPainted(false);
            symbolButton.setHorizontalAlignment(SwingConstants.LEFT);
            symbolButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            symbolButton.addActionListener(e -> {
                this.stockController.execute(outputData.getTickerSymbol());
                this.viewManagerModel.setState(this.stockViewModel.getViewName());
                this.viewManagerModel.firePropertyChanged();
            });

            final JLabel countryLabel = new JLabel(outputData.getCountry());
            final JLabel companyLabel = new JLabel(outputData.getCompanyName());
            final JLabel industryLabel = new JLabel(outputData.getIndustry());
            final JLabel priceLabel = new JLabel(outputData.getPreviousClose().toPlainString());

            styleResultLabel(countryLabel);
            styleResultLabel(companyLabel);
            styleResultLabel(industryLabel);
            styleResultLabel(priceLabel);

            outputDataPanel.add(symbolButton);
            outputDataPanel.add(countryLabel);
            outputDataPanel.add(companyLabel);
            outputDataPanel.add(industryLabel);
            outputDataPanel.add(priceLabel);

            similarSearchResultsPanel.add(outputDataPanel);
        }
        similarSearchResultsPanel.revalidate();
        similarSearchResultsPanel.repaint();
    }

    private void addHeaderLabel(JPanel panel, String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setForeground(TEXT_MUTED);
        panel.add(label);
    }

    private void styleResultLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(TEXT_MAIN);
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