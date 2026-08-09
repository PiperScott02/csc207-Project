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
// === ADDED: Imports for dynamic date formatting ===
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The screen for searching stocks and viewing search/similar results with dark theme and sidebar.
 */
public class SearchView extends JPanel implements PropertyChangeListener {

    private static final String SEARCH_VIEW_NAME = "search";
    private static final String LOGGED_IN_VIEW_NAME = "logged in";

    // === DARK MODE UI PALETTE ===
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39);

    // The Display Text Fields for the Ticker Search Result
    private final JButton searchButton = new JButton("Search");
    private final JButton tickerSearchSymbol = new JButton("N/A");
    private final JLabel tickerSearchCompanyName = new JLabel("N/A");
    private final JLabel tickerSearchCountry = new JLabel("N/A");
    private final JLabel tickerSearchIndustry = new JLabel("N/A");
    private final JLabel tickerSearchPreviousClose = new JLabel("N/A");
    private final JTextField searchInputField = new JTextField(30);

    // Similar Search Panel
    private final JPanel similarSearchResultsPanel = createSimilarSearchResultsPanel();

    private final SimilarSearchController similarSearchController;
    private final SimilarSearchViewModel similarSearchViewModel;
    private final TickerSearchController tickerSearchController;
    private final TickerSearchViewModel tickerSearchViewModel;

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

        tickerSearchViewModel.addPropertyChangeListener(this);
        similarSearchViewModel.addPropertyChangeListener(this);
        // === ADDED: Listen to loggedInViewModel changes for dynamic session updates ===
        loggedInViewModel.addPropertyChangeListener(this);

        // Click main ticker button -> Execute StockUseCase & Switch to Stock View
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

        // Add sidebar on the left and main content on the center
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    /**
     * Creates the sidebar navigation panel matching other redesigned views. ===
     */
    private JPanel createSidebarPanel() {
        final JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(240, 0));
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        final JPanel brandPanel = new JPanel();
        brandPanel.setBackground(SIDEBAR_BG);
        brandPanel.setPreferredSize(new Dimension(240, 70));
        brandPanel.setLayout(null);

        final JLabel logoBadge = new JLabel("P", SwingConstants.CENTER);
        logoBadge.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoBadge.setForeground(TEXT_MAIN);
        logoBadge.setBackground(ACCENT_GREEN);
        logoBadge.setOpaque(true);
        logoBadge.setBounds(20, 20, 28, 28);

        final JLabel brandLabel = new JLabel("PortfolioPilot");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        brandLabel.setForeground(TEXT_MAIN);
        brandLabel.setBounds(58, 20, 150, 28);

        brandPanel.add(logoBadge);
        brandPanel.add(brandLabel);

        final JPanel navLinksPanel = new JPanel();
        navLinksPanel.setBackground(SIDEBAR_BG);
        navLinksPanel.setLayout(new GridLayout(10, 1, 0, 2));
        navLinksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        navLinksPanel.add(createSidebarNavLink("Overview", false, e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Holdings", false, e -> {
            viewManagerModel.setState("holdings");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Watchlist", false, e -> {
            viewManagerModel.setState("watchlist");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("News & Sentiment", false, e -> {
            viewManagerModel.setState("news");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Portfolio Health", false, e -> {}));
        navLinksPanel.add(createSidebarNavLink("Risk Preference", false, e -> {
            viewManagerModel.setState("risk preference");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Currency", false, e -> {
            viewManagerModel.setState("currency conversion");
            viewManagerModel.firePropertyChanged();
        }));
        // Search Stocks is active here
        navLinksPanel.add(createSidebarNavLink("Search Stocks", true, e -> {}));
        navLinksPanel.add(createSidebarNavLink("Black-Litterman", false, e -> {}));

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(SIDEBAR_BG);
        bottomPanel.setPreferredSize(new Dimension(240, 60));
        bottomPanel.setLayout(null);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        // === MODIFIED: Dynamically retrieve logged-in username instead of hardcoding "HANA" ===
        String username = loggedInViewModel.getState() != null && loggedInViewModel.getState().getUsername() != null
                ? loggedInViewModel.getState().getUsername().toUpperCase()
                : "USER";

        final JLabel welcomeLabel = new JLabel("WELCOME, " + username);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        welcomeLabel.setForeground(TEXT_MUTED);
        welcomeLabel.setBounds(20, 12, 180, 15);

        // === MODIFIED: Dynamically retrieve current live system date instead of hardcoding "Aug 7, 2026" ===
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        final JLabel dateLabel = new JLabel(currentDate);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        dateLabel.setForeground(TEXT_MUTED);
        dateLabel.setBounds(20, 30, 180, 15);

        bottomPanel.add(welcomeLabel);
        bottomPanel.add(dateLabel);

        sidebarPanel.add(brandPanel, BorderLayout.NORTH);
        sidebarPanel.add(navLinksPanel, BorderLayout.CENTER);
        sidebarPanel.add(bottomPanel, BorderLayout.SOUTH);

        return sidebarPanel;
    }

    /**
     * Helper to style sidebar navigation buttons. ===
     */
    private JButton createSidebarNavLink(String text, boolean isActive, ActionListener action) {
        final JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 13));
        button.setForeground(isActive ? TEXT_MAIN : TEXT_MUTED);
        button.setBackground(isActive ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        button.addActionListener(action);
        return button;
    }

    /**
     * Wrapper panel to manage scrollable content area alongside the sidebar. ===
     */
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

        // === MODIFIED: Uses class-level searchButton instance variable instead of redeclaring final local ===
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

        // Header Labels
        addHeaderLabel(tickerSearchValuesPanel, "TICKER SYMBOL");
        addHeaderLabel(tickerSearchValuesPanel, "COUNTRY");
        addHeaderLabel(tickerSearchValuesPanel, "COMPANY NAME");
        addHeaderLabel(tickerSearchValuesPanel, "INDUSTRY");
        addHeaderLabel(tickerSearchValuesPanel, "PREVIOUS CLOSE");

        // Style Ticker Symbol Button
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

        final JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CARD_BG);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JPanel topOfSimilarSearchPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        topOfSimilarSearchPanel.setBackground(CARD_BG);
        addHeaderLabel(topOfSimilarSearchPanel, "TICKER SYMBOL");
        addHeaderLabel(topOfSimilarSearchPanel, "COUNTRY");
        addHeaderLabel(topOfSimilarSearchPanel, "COMPANY NAME");
        addHeaderLabel(topOfSimilarSearchPanel, "INDUSTRY");
        addHeaderLabel(topOfSimilarSearchPanel, "PREVIOUS CLOSE");

        tableCard.add(topOfSimilarSearchPanel, BorderLayout.NORTH);
        tableCard.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        tableCard.add(similarSearchResultsPanel, BorderLayout.SOUTH);

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

            // Click similar search symbol button -> Execute StockUseCase & Switch to Stock View
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

    /**
     * Helper method for consistent table column header text styling. ===
     */
    private void addHeaderLabel(JPanel panel, String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setForeground(TEXT_MUTED);
        panel.add(label);
    }

    /**
     * Helper method for consistent table data text styling. ===
     */
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
            // === ADDED: Re-renders UI dynamically when user state or login session updates ===
        } else if (evt.getPropertyName().equals("state") || evt.getPropertyName().equals("logged in")) {
            removeAll();
            add(createSidebarPanel(), BorderLayout.WEST);
            add(createMainContentPanel(), BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    public String getViewName() {
        return SEARCH_VIEW_NAME;
    }
}