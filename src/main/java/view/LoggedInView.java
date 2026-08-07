package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import entity.Stock;
import entity.StockHolding;

import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;

/**
 * The home screen displayed after a user successfully logs in.
 */
public class    LoggedInView extends JPanel implements PropertyChangeListener {

    private static final String LOGIN_VIEW_NAME = "log in";
    private static final String SEARCH_VIEW_NAME = "search";
    private static final String RISK_PREFERENCE_VIEW_NAME = "risk preference";
    private static final String CURRENCY_CONVERSION_VIEW_NAME = "currency conversion";
    private static final String NEWS_VIEW_NAME = "news";
    private static final String WATCHLIST_VIEW_NAME = "watchlist";
    private static final String ADD_HOLDING_VIEW_NAME = "add holding";

    // === DARK MODE UI CHANGE ===: Figma color palette variables
    private static final Color BG_DARK = new Color(11, 15, 25);       // #0B0F19 Main window background
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);     // Darker shade for persistent sidebar
    private static final Color CARD_BG = new Color(17, 24, 39);       // #111827 Cards & Table background
    private static final Color BORDER_COLOR = new Color(31, 41, 55);  // #1F2937 Borders
    private static final Color TEXT_MAIN = new Color(243, 244, 246);  // #F3F4F6 Primary white text
    private static final Color TEXT_MUTED = new Color(156, 163, 175); // #9CA3AF Muted labels
    private static final Color ACCENT_GREEN = new Color(16, 185, 129); // #10B981 Gain/Success color
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39); // Active selection highlight for sidebar

    private final String viewName = "logged in";
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final PortfolioHealthController portfolioHealthController;
    private final BlackLittermanController blackLittermanController;

    private final JLabel welcomeLabel = new JLabel("Welcome");
    private DefaultTableModel tableModel;
    private JLabel lastUpdatedLabel;
    private JLabel totalHoldingsLabel;
    private JLabel totalPortfolioValueValLabel;
    private JLabel totalGainLossValLabel;
    private JLabel dailyChangeValLabel;
    private boolean hasAddedHolding = false;

    /**
     * Creates the home screen.
     *
     * @param loggedInViewModel contains information about the logged-in user
     * @param viewManagerModel controls which application screen is visible
     * @param portfolioHealthController triggers calculation of portfolio health
     * @param blackLittermanController triggers Black-Litterman workflow
     */
    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        ViewManagerModel viewManagerModel,
                        PortfolioHealthController portfolioHealthController,
                        BlackLittermanController blackLittermanController) {

        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.portfolioHealthController = portfolioHealthController;
        this.blackLittermanController = blackLittermanController;

        loggedInViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createHoldingsPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        final JPanel topPanel = new JPanel(new BorderLayout(0, 15));

        topPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        topPanel.add(createSummaryPanel(), BorderLayout.CENTER);
        topPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel createHeaderPanel() {
        final JPanel headerPanel = new JPanel(new BorderLayout());
        final JPanel titlePanel = new JPanel(new GridLayout(2, 1));

        final JLabel title = new JLabel("PortfolioPilot", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        final JLabel subtitle = new JLabel("Personal Investment Portfolio Tracker", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSummaryPanel() {
        final JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Portfolio Summary"));

        totalPortfolioValueValLabel = new JLabel("$0.00", SwingConstants.CENTER);
        totalPortfolioValueValLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        totalGainLossValLabel = new JLabel("$0.00", SwingConstants.CENTER);
        totalGainLossValLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        dailyChangeValLabel = new JLabel("$0.00 (0.00%)", SwingConstants.CENTER);
        dailyChangeValLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        summaryPanel.add(createSummaryBox("Total Portfolio Value", totalPortfolioValueValLabel));
        summaryPanel.add(createSummaryBox("Total Gain / Loss", totalGainLossValLabel));
        summaryPanel.add(createSummaryBox("Daily Change", dailyChangeValLabel));

        return summaryPanel;
    }

    private JPanel createSummaryBox(String heading, JLabel valueLabel) {
        final JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(15, 10, 15, 10)
                )
        );

        final JLabel headingLabel = new JLabel(heading, SwingConstants.CENTER);
        headingLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        panel.add(headingLabel);
        panel.add(valueLabel);

        return panel;
    }

    private JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 9, 8, 0));

        final JButton watchlistButton = new JButton("Watchlist");
        watchlistButton.addActionListener(event -> {
            viewManagerModel.setState(WATCHLIST_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });
        buttonPanel.add(watchlistButton);

        final JButton addHoldingButton = new JButton("Add Holding");
        addHoldingButton.addActionListener(event -> {
            viewManagerModel.setState(ADD_HOLDING_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });
        buttonPanel.add(addHoldingButton);

        // News Button
        final JButton newsButton = new JButton("News");
        newsButton.addActionListener(event -> {
            viewManagerModel.setState(NEWS_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });
        buttonPanel.add(newsButton);

        // Portfolio Health Button
        final JButton portfolioHealthButton = new JButton("Portfolio Health");
        portfolioHealthButton.addActionListener(event -> {
            LoggedInState state = loggedInViewModel.getState();
            if (state != null && state.getUser() != null) {
                portfolioHealthController.execute(state.getUser());
            } else {
                JOptionPane.showMessageDialog(this, "No active user session found.");
            }
        });
        buttonPanel.add(portfolioHealthButton);

        // Risk Preference Button
        final JButton riskPreferenceButton = new JButton("Risk Preference");
        riskPreferenceButton.addActionListener(event -> {
            viewManagerModel.setState(RISK_PREFERENCE_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });
        buttonPanel.add(riskPreferenceButton);

        // Currency Conversion Button
        final JButton currencyConversionButton = new JButton("Currency Conversion");
        currencyConversionButton.addActionListener(event -> {
            viewManagerModel.setState(CURRENCY_CONVERSION_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });
        buttonPanel.add(currencyConversionButton);

        // Log Out Button
        final JButton logOutButton = new JButton("Log Out");
        logOutButton.addActionListener(event -> {
            viewManagerModel.setState(LOGIN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });
        buttonPanel.add(logOutButton);

        // Search Button
        final JButton searchButton = new JButton("Search Stocks");
        searchButton.addActionListener(event -> {
            viewManagerModel.setState(SEARCH_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });
        buttonPanel.add(searchButton);

        // Input Stock Views Button (Black-Litterman)
        final JButton stockViewsButton = new JButton("Input Stock Views");
        stockViewsButton.addActionListener(event -> {
            LoggedInState state = loggedInViewModel.getState();
            if (state != null && state.getUser() != null) {
                blackLittermanController.loadMarketData(state.getUser());
            } else {
                JOptionPane.showMessageDialog(this, "No active user session found.");
            }
        });
        buttonPanel.add(stockViewsButton);

        return buttonPanel;
    }

    private JPanel createHoldingsPanel() {
        final JPanel holdingsPanel = new JPanel(new BorderLayout(0, 10));
        holdingsPanel.setBorder(BorderFactory.createTitledBorder("Your Holdings"));

        final String[] columnNames = {
                "Ticker", "Company", "Shares", "Avg Price", "Current Price", "Gain / Loss", "Gain %"
        };

        // Assign to the class-level tableModel instance variable
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        final JTable holdingsTable = new JTable(tableModel);
        holdingsTable.setRowHeight(28);
        holdingsTable.setFillsViewportHeight(true);

        final JScrollPane scrollPane = new JScrollPane(holdingsTable);
        final JPanel statusPanel = new JPanel(new BorderLayout());

        totalHoldingsLabel = new JLabel("Total Holdings: 0");
        lastUpdatedLabel = new JLabel("Last updated: --");

        statusPanel.add(totalHoldingsLabel, BorderLayout.WEST);
        statusPanel.add(lastUpdatedLabel, BorderLayout.EAST);

        holdingsPanel.add(scrollPane, BorderLayout.CENTER);
        holdingsPanel.add(statusPanel, BorderLayout.SOUTH);

        return holdingsPanel;
    }

    /**
     * Updates the username AND holdings table displayed on the home screen.
     *
     * @param event property-change event from the view model
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName())) {
            final LoggedInState state = (LoggedInState) event.getNewValue();
            final String username = state.getUsername();

            if (username == null || username.isBlank()) {
                welcomeLabel.setText("Welcome");
            } else {
                welcomeLabel.setText("Welcome, " + username);
            }

            // Repopulate the holdings table and calculate totals whenever the state changes
            if (tableModel != null && state.getHoldings() != null) {
                tableModel.setRowCount(0); // Clear current rows

                java.math.BigDecimal totalPortfolioValue = java.math.BigDecimal.ZERO;
                java.math.BigDecimal totalGainLoss = java.math.BigDecimal.ZERO;
                java.math.BigDecimal totalDailyChange = java.math.BigDecimal.ZERO;

                for (StockHolding holding : state.getHoldings()) {
                    Stock stock = holding.getStock();

                    // Accumulate portfolio-wide totals
                    totalPortfolioValue = totalPortfolioValue.add(holding.calculateTotalValue());
                    totalGainLoss = totalGainLoss.add(holding.calculateGainLoss());

                    // Accumulate daily change if stock and price change data exist
                    if (stock != null && stock.getDailyPriceChange() != null) {
                        java.math.BigDecimal holdingDailyChange = stock.getDailyPriceChange()
                                .multiply(java.math.BigDecimal.valueOf(holding.getNumberOfShares()));
                        totalDailyChange = totalDailyChange.add(holdingDailyChange);
                    }

                    // Populate table rows
                    tableModel.addRow(new Object[]{
                            stock != null ? stock.getTickerSymbol() : "",
                            stock != null ? stock.getCompanyName() : "",
                            holding.getNumberOfShares(),
                            String.format("$%.2f", holding.getAveragePrice()),
                            stock != null ? String.format("$%.2f", stock.getClose()) : "",
                            String.format("$%.2f", holding.calculateGainLoss()),
                            String.format("%.2f%%", holding.calculateGainLossPercentage())
                    });
                }

                // Update the top summary boxes
                if (totalPortfolioValueValLabel != null) {
                    totalPortfolioValueValLabel.setText(String.format("$%.2f", totalPortfolioValue));
                }
                if (totalGainLossValLabel != null) {
                    totalGainLossValLabel.setText(String.format("$%.2f", totalGainLoss));
                }
                if (dailyChangeValLabel != null) {
                    dailyChangeValLabel.setText(String.format("$%.2f", totalDailyChange));
                }

                // Only update "Total Holdings:" count when a Holding is added
                if (totalHoldingsLabel != null) {
                    int uniqueHoldingCount = state.getHoldings().size();
                    totalHoldingsLabel.setText("Total Holdings: " + uniqueHoldingCount);
                }

                // Check if user has any Holdings
                if (!state.getHoldings().isEmpty()) {
                    hasAddedHolding = true;
                }
            }
            // Only update "Last updated:" timestamp when a Holding is added
            if (lastUpdatedLabel != null) {
                if (hasAddedHolding) {
                    String currentTime = java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    lastUpdatedLabel.setText("Last updated: " + currentTime);
                } else {
                    lastUpdatedLabel.setText("Last updated: --");
                }
            }
        }
    }
    public String getViewName() {
        return viewName;
    }
}