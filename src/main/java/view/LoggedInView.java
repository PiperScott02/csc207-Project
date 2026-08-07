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
    private final JLabel dateLabel = new JLabel();
    private DefaultTableModel holdingsTableModel;
    private DefaultTableModel watchlistTableModel;
    private JLabel lastUpdatedLabel;

    private JLabel totalPortfolioValueValLabel;
    private JLabel allTimePercentageBadge;
    private JLabel subTextLabel;
    private JLabel totalGainLossValLabel;
    private JLabel totalGainLossSubLabel;
    private JLabel dailyChangeValLabel;
    private JLabel dailyChangeSubLabel;
    private JLabel totalHoldingsValLabel;

    /**
     * Creates the home screen with sidebar layout.
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

        // Split main view into left sidebar and right main content area
        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);

        // Populate initial state if already present in the ViewModel
        if (loggedInViewModel.getState() != null) {
            updateViewFromState(loggedInViewModel.getState());
        }
    }

    // === DARK MODE UI CHANGE ===: Builds the persistent left navigation sidebar matching your screenshot
    private JPanel createSidebarPanel() {
        final JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        // Top Brand Header inside Sidebar
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

        // Center Navigation Links List
        final JPanel navLinksPanel = new JPanel();
        navLinksPanel.setBackground(SIDEBAR_BG);
        navLinksPanel.setLayout(new GridLayout(9, 1, 0, 2));
        navLinksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Highlight "Overview" as active since we are on the Overview screen
        navLinksPanel.add(createSidebarNavLink("Overview", false, e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Holdings", false, e -> {
            viewManagerModel.setState("holdings");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Watchlist", false, event -> {
            viewManagerModel.setState(WATCHLIST_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("News & Sentiment", false, event -> {
            viewManagerModel.setState(NEWS_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Portfolio Health", false, event -> {
            LoggedInState state = loggedInViewModel.getState();
            if (state != null && state.getUser() != null) {
                portfolioHealthController.execute(state.getUser());
            } else {
                JOptionPane.showMessageDialog(this, "No active user session found.");
            }
        }));
        navLinksPanel.add(createSidebarNavLink("Risk Preference", false, event -> {
            viewManagerModel.setState(RISK_PREFERENCE_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Currency", false, event -> {
            viewManagerModel.setState(CURRENCY_CONVERSION_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Search Stocks", false, event -> {
            viewManagerModel.setState(SEARCH_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Black-Litterman", false, event -> {
            LoggedInState state = loggedInViewModel.getState();
            if (state != null && state.getUser() != null) {
                blackLittermanController.loadMarketData(state.getUser());
            } else {
                JOptionPane.showMessageDialog(this, "No active user session found.");
            }
        }));

        // Bottom User Profile & Log Out Section
        final JPanel userFooterPanel = new JPanel();
        userFooterPanel.setBackground(SIDEBAR_BG);
        userFooterPanel.setPreferredSize(new Dimension(240, 80));
        userFooterPanel.setLayout(null);
        userFooterPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        welcomeLabel.setForeground(TEXT_MUTED);
        welcomeLabel.setBounds(20, 15, 200, 15);

        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dateLabel.setForeground(TEXT_MUTED);
        dateLabel.setBounds(20, 32, 200, 15);
        updateDateTimeDisplay();

        final JButton logOutLink = new JButton("↳ Log Out");
        logOutLink.setFont(new Font("SansSerif", Font.PLAIN, 12));
        logOutLink.setForeground(new Color(239, 68, 68));
        logOutLink.setContentAreaFilled(false);
        logOutLink.setBorderPainted(false);
        logOutLink.setFocusPainted(false);
        logOutLink.setHorizontalAlignment(SwingConstants.LEFT);
        logOutLink.setBounds(14, 50, 120, 20);
        logOutLink.addActionListener(event -> {
            viewManagerModel.setState(LOGIN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        userFooterPanel.add(welcomeLabel);
        userFooterPanel.add(dateLabel);
        userFooterPanel.add(logOutLink);

        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(navLinksPanel, BorderLayout.CENTER);
        sidebar.add(userFooterPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    // Helper for styled sidebar links
    private JButton createSidebarNavLink(String text, boolean isActive, java.awt.event.ActionListener action) {
        final JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 13));
        button.setForeground(isActive ? TEXT_MAIN : TEXT_MUTED);
        button.setBackground(isActive ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        button.addActionListener(action);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setForeground(TEXT_MAIN);
                    button.setBackground(CARD_BG);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setForeground(TEXT_MUTED);
                    button.setBackground(SIDEBAR_BG);
                }
            }
        });
        return button;
    }

    // Right Main Content Area containing Overview Header, Metrics, and Preview Tables
    private JPanel createMainContentPanel() {
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Top Title "Overview"
        final JLabel overviewTitle = new JLabel("Overview");
        overviewTitle.setFont(new Font("Serif", Font.BOLD, 26));
        overviewTitle.setForeground(TEXT_MAIN);
        overviewTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Scrollable content wrapper for dashboard widgets (without redundant button row)
        final JPanel contentBody = new JPanel();
        contentBody.setBackground(BG_DARK);
        contentBody.setLayout(new BoxLayout(contentBody, BoxLayout.Y_AXIS));

        contentBody.add(createTopPortfolioValueCard());
        contentBody.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));
        contentBody.add(createMiddleMetricsRow());
        contentBody.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));
        contentBody.add(createHoldingsAndWatchlistPreviewPanel());
        contentBody.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));
        contentBody.add(createFooterStatusPanel());

        final JScrollPane scrollPane = new JScrollPane(contentBody);
        scrollPane.setBackground(BG_DARK);
        scrollPane.getViewport().setBackground(BG_DARK);
        scrollPane.setBorder(null);

        mainPanel.add(overviewTitle, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    // Large top card for Total Portfolio Value
    private JPanel createTopPortfolioValueCard() {
        final JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setLayout(null);
        card.setPreferredSize(new Dimension(0, 130));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        final JLabel titleLbl = new JLabel("TOTAL PORTFOLIO VALUE");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setBounds(25, 20, 200, 15);

        totalPortfolioValueValLabel = new JLabel("$0.00");
        totalPortfolioValueValLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        totalPortfolioValueValLabel.setForeground(TEXT_MAIN);
        totalPortfolioValueValLabel.setBounds(23, 40, 250, 40);

        allTimePercentageBadge = new JLabel(" 0.00% ALL-TIME ", SwingConstants.CENTER);
        allTimePercentageBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
        allTimePercentageBadge.setForeground(ACCENT_GREEN);
        allTimePercentageBadge.setBackground(new Color(6, 78, 59));
        allTimePercentageBadge.setOpaque(true);
        allTimePercentageBadge.setBounds(235, 50, 140, 24);

        subTextLabel = new JLabel("Cost basis: $0.00");
        subTextLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTextLabel.setForeground(TEXT_MUTED);
        subTextLabel.setBounds(25, 90, 450, 20);

        card.add(titleLbl);
        card.add(totalPortfolioValueValLabel);
        card.add(allTimePercentageBadge);
        card.add(subTextLabel);

        return card;
    }

    // Middle row with 3 metrics cards (Gain/Loss, Daily Change, Total Holdings)
    private JPanel createMiddleMetricsRow() {
        final JPanel row = new JPanel(new GridLayout(1, 3, 15, 0));
        row.setBackground(BG_DARK);
        row.setPreferredSize(new Dimension(0, 95));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        // Card 1: Gain / Loss
        final JPanel c1 = new JPanel();
        c1.setBackground(CARD_BG);
        c1.setLayout(null);
        c1.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        final JLabel tLbl1 = new JLabel("TOTAL GAIN / LOSS");
        tLbl1.setFont(new Font("SansSerif", Font.BOLD, 10));
        tLbl1.setForeground(TEXT_MUTED);
        tLbl1.setBounds(20, 15, 200, 15);
        totalGainLossValLabel = new JLabel("$0.00");
        totalGainLossValLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalGainLossValLabel.setForeground(ACCENT_GREEN);
        totalGainLossValLabel.setBounds(18, 35, 200, 30);
        totalGainLossSubLabel = new JLabel("0.00%");
        totalGainLossSubLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        totalGainLossSubLabel.setForeground(TEXT_MUTED);
        totalGainLossSubLabel.setBounds(20, 65, 200, 15);
        c1.add(tLbl1);
        c1.add(totalGainLossValLabel);
        c1.add(totalGainLossSubLabel);

        // Card 2: Daily Change
        final JPanel c2 = new JPanel();
        c2.setBackground(CARD_BG);
        c2.setLayout(null);
        c2.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        final JLabel tLbl2 = new JLabel("DAILY CHANGE");
        tLbl2.setFont(new Font("SansSerif", Font.BOLD, 10));
        tLbl2.setForeground(TEXT_MUTED);
        tLbl2.setBounds(20, 15, 200, 15);
        dailyChangeValLabel = new JLabel("$0.00");
        dailyChangeValLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        dailyChangeValLabel.setForeground(ACCENT_GREEN);
        dailyChangeValLabel.setBounds(18, 35, 200, 30);
        dailyChangeSubLabel = new JLabel("vs prev. close");
        dailyChangeSubLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dailyChangeSubLabel.setForeground(TEXT_MUTED);
        dailyChangeSubLabel.setBounds(20, 65, 200, 15);
        c2.add(tLbl2);
        c2.add(dailyChangeValLabel);
        c2.add(dailyChangeSubLabel);

        // Card 3: Total Holdings
        final JPanel c3 = new JPanel();
        c3.setBackground(CARD_BG);
        c3.setLayout(null);
        c3.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        final JLabel tLbl3 = new JLabel("TOTAL HOLDINGS");
        tLbl3.setFont(new Font("SansSerif", Font.BOLD, 10));
        tLbl3.setForeground(TEXT_MUTED);
        tLbl3.setBounds(20, 15, 200, 15);
        totalHoldingsValLabel = new JLabel("0");
        totalHoldingsValLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalHoldingsValLabel.setForeground(TEXT_MAIN);
        totalHoldingsValLabel.setBounds(18, 35, 200, 30);
        final JLabel sLbl3 = new JLabel("active positions");
        sLbl3.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sLbl3.setForeground(TEXT_MUTED);
        sLbl3.setBounds(20, 65, 200, 15);
        c3.add(tLbl3);
        c3.add(totalHoldingsValLabel);
        c3.add(sLbl3);

        row.add(c1);
        row.add(c2);
        row.add(c3);

        return row;
    }


    // Holdings & Watchlist bottom cards preview populated dynamically from state
    private JPanel createHoldingsAndWatchlistPreviewPanel() {
        final JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(BG_DARK);
        panel.setPreferredSize(new Dimension(0, 180));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        final String[] cols = {"Ticker", "Company", "Price", "Change"};

        // Holdings Preview Card
        final JPanel holdingsCard = new JPanel(new BorderLayout());
        holdingsCard.setBackground(CARD_BG);
        holdingsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        final JLabel hTitle = new JLabel("YOUR HOLDINGS");
        hTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        hTitle.setForeground(TEXT_MUTED);

        holdingsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        final JTable holdingsTable = new JTable(holdingsTableModel);
        holdingsTable.setBackground(CARD_BG);
        holdingsTable.setForeground(TEXT_MAIN);
        holdingsTable.setGridColor(BORDER_COLOR);
        holdingsTable.setRowHeight(28);
        holdingsTable.getTableHeader().setVisible(false);

        final JScrollPane sp1 = new JScrollPane(holdingsTable);
        sp1.getViewport().setBackground(CARD_BG);
        sp1.setBorder(null);

        holdingsCard.add(hTitle, BorderLayout.NORTH);
        holdingsCard.add(sp1, BorderLayout.CENTER);

        // Watchlist Preview Card
        final JPanel watchlistCard = new JPanel(new BorderLayout());
        watchlistCard.setBackground(CARD_BG);
        watchlistCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        final JLabel wTitle = new JLabel("WATCHLIST");
        wTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        wTitle.setForeground(TEXT_MUTED);

        watchlistTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        final JTable watchTable = new JTable(watchlistTableModel);
        watchTable.setBackground(CARD_BG);
        watchTable.setForeground(TEXT_MAIN);
        watchTable.setGridColor(BORDER_COLOR);
        watchTable.setRowHeight(28);
        watchTable.getTableHeader().setVisible(false);

        final JScrollPane sp2 = new JScrollPane(watchTable);
        sp2.getViewport().setBackground(CARD_BG);
        sp2.setBorder(null);

        watchlistCard.add(wTitle, BorderLayout.NORTH);
        watchlistCard.add(sp2, BorderLayout.CENTER);

        panel.add(holdingsCard);
        panel.add(watchlistCard);

        return panel;
    }

    private JPanel createFooterStatusPanel() {
        final JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_DARK);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        lastUpdatedLabel = new JLabel();
        lastUpdatedLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lastUpdatedLabel.setForeground(TEXT_MUTED);
        updateLastUpdatedTime();

        footer.add(lastUpdatedLabel, BorderLayout.WEST);
        return footer;
    }

    private void updateDateTimeDisplay() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        dateLabel.setText(now.format(dateFormatter));
    }

    private void updateLastUpdatedTime() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
        lastUpdatedLabel.setText("Last updated: " + now.format(formatter));
    }

    private void updateViewFromState(LoggedInState state) {
        final String username = state.getUsername();
        if (username != null && !username.isBlank()) {
            welcomeLabel.setText("WELCOME, " + username.toUpperCase());
        } else {
            welcomeLabel.setText("WELCOME");
        }

        if (state.getHoldings() != null) {
            BigDecimal totalPortfolioValue = BigDecimal.ZERO;
            BigDecimal totalCostBasis = BigDecimal.ZERO;
            BigDecimal totalGainLoss = BigDecimal.ZERO;
            BigDecimal totalDailyChange = BigDecimal.ZERO;

            if (holdingsTableModel != null) {
                holdingsTableModel.setRowCount(0);
            }

            for (StockHolding holding : state.getHoldings()) {
                Stock stock = holding.getStock();
                BigDecimal holdingValue = holding.calculateTotalValue();
                BigDecimal holdingCost = holding.calculateTotalCost();
                BigDecimal holdingGain = holding.calculateGainLoss();
                BigDecimal holdingDailyChange = BigDecimal.ZERO;
                if (stock != null && stock.getDailyPriceChange() != null) {
                    holdingDailyChange = stock.getDailyPriceChange().multiply(BigDecimal.valueOf(holding.getNumberOfShares()));
                }

                totalPortfolioValue = totalPortfolioValue.add(holdingValue);
                totalCostBasis = totalCostBasis.add(holdingCost);
                totalGainLoss = totalGainLoss.add(holdingGain);
                totalDailyChange = totalDailyChange.add(holdingDailyChange);

                if (holdingsTableModel != null) {
                    holdingsTableModel.addRow(new Object[]{
                            stock != null ? stock.getTickerSymbol() : "",
                            stock != null ? stock.getCompanyName() : "",
                            String.format("$%.2f", holding.getAveragePrice()),
                            String.format("%+.2f%%", holding.calculateGainLossPercentage())
                    });
                }

                if (dailyChangeValLabel != null) {
                    dailyChangeValLabel.setText(String.format("$%.2f", totalDailyChange.doubleValue()));
                    if (totalDailyChange.compareTo(BigDecimal.ZERO) < 0) {
                        dailyChangeValLabel.setForeground(new Color(239, 68, 68)); // Red for negative
                    } else {
                        dailyChangeValLabel.setForeground(ACCENT_GREEN); // Green for positive/zero
                    }
                }
            }
            BigDecimal allTimePercentage = BigDecimal.ZERO;
            if (totalCostBasis.compareTo(BigDecimal.ZERO) > 0) {
                allTimePercentage = totalGainLoss
                        .divide(totalCostBasis, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            // Update Overview Metrics dynamically
            if (totalPortfolioValueValLabel != null) {
                totalPortfolioValueValLabel.setText(String.format("$%.2f", totalPortfolioValue.doubleValue()));
            }
            if (allTimePercentageBadge != null) {
                allTimePercentageBadge.setText(String.format(" %+.2f%% ALL-TIME ", allTimePercentage.doubleValue()));

                if (allTimePercentage.compareTo(BigDecimal.ZERO) < 0) {
                    // Negative: Soft red text with a dark red background container
                    allTimePercentageBadge.setForeground(new Color(239, 68, 68));
                    allTimePercentageBadge.setBackground(new Color(127, 29, 29));
                } else {
                    // Positive / Zero: Green text with a dark green background container
                    allTimePercentageBadge.setForeground(ACCENT_GREEN);
                    allTimePercentageBadge.setBackground(new Color(6, 78, 59));
                }
            }
            if (subTextLabel != null) {
                subTextLabel.setText(String.format("Cost basis: $%.2f", totalCostBasis.doubleValue()));
            }
            if (totalGainLossValLabel != null) {
                totalGainLossValLabel.setText(String.format("$%.2f", totalGainLoss.doubleValue()));
                if (totalGainLoss.compareTo(BigDecimal.ZERO) < 0) {
                    totalGainLossValLabel.setForeground(new Color(239, 68, 68)); // Red for negative
                } else {
                    totalGainLossValLabel.setForeground(ACCENT_GREEN); // Green for positive/zero
                }
            }
            if (totalGainLossSubLabel != null) {
                totalGainLossSubLabel.setText(String.format("%+.2f%%", allTimePercentage.doubleValue()));
                if (allTimePercentage.compareTo(BigDecimal.ZERO) < 0) {
                    totalGainLossSubLabel.setForeground(new Color(239, 68, 68)); // Red for negative
                } else {
                    totalGainLossSubLabel.setForeground(TEXT_MUTED); // Muted text for positive/zero
                }
            }
            if (totalHoldingsValLabel != null) {
                totalHoldingsValLabel.setText(String.valueOf(state.getHoldings().size()));
            }
        }
        updateLastUpdatedTime();
    }
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName())) {
            final LoggedInState state = (LoggedInState) event.getNewValue();
            if (state != null) {
                updateViewFromState(state);
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}
