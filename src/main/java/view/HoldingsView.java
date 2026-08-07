package view;

import entity.Stock;
import entity.StockHolding;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.delete_holding.DeleteHoldingController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HoldingsView extends JPanel implements ActionListener, PropertyChangeListener {

    // === DARK MODE UI PALETTE ===
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39);

    private final String viewName = "holdings";
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final DeleteHoldingController deleteHoldingController;

    // Dynamic Summary Metric Labels
    private final JLabel portfolioValLabel = new JLabel("$0.00");
    private final JLabel totalPnlValLabel = new JLabel("$0.00");
    private final JLabel todaysChangeValLabel = new JLabel("$0.00");
    private final JLabel costBasisValLabel = new JLabel("$0.00");
    private final JLabel holdingsCountLabel = new JLabel("0 positions");
    private final JLabel lastUpdatedLabel = new JLabel();

    private DefaultTableModel detailedHoldingsTableModel;

    public HoldingsView(ViewManagerModel viewManagerModel, LoggedInViewModel loggedInViewModel, DeleteHoldingController deleteHoldingController) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.deleteHoldingController = deleteHoldingController;
        this.loggedInViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);

        if (loggedInViewModel.getState() != null) {
            updateViewFromState(loggedInViewModel.getState());
        }
    }

    public String getViewName() {
        return viewName;
    }

    private JPanel createSidebarPanel() {
        final JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

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
        navLinksPanel.setLayout(new GridLayout(9, 1, 0, 2));
        navLinksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        navLinksPanel.add(createSidebarNavLink("Overview", false, e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Holdings", false, e -> {}));
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
        navLinksPanel.add(createSidebarNavLink("Search Stocks", false, e -> {
            viewManagerModel.setState("search");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Black-Litterman", false, e -> {}));

        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(navLinksPanel, BorderLayout.CENTER);

        return sidebar;
    }

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

    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(null);
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        final JLabel titleLabel = new JLabel("Holdings");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBounds(0, 10, 200, 35);

        // 4-Column Dynamic Metrics Row
        final JPanel metricsRow = new JPanel(new GridLayout(1, 4, 15, 0));
        metricsRow.setBackground(BG_DARK);
        metricsRow.setBounds(0, 60, 940, 95);

        metricsRow.add(createMetricCard("PORTFOLIO VALUE", portfolioValLabel, holdingsCountLabel, TEXT_MAIN));
        metricsRow.add(createMetricCard("TOTAL P&L", totalPnlValLabel, "+12.19%", ACCENT_GREEN));
        metricsRow.add(createMetricCard("TODAY'S CHANGE", todaysChangeValLabel, "+0.42% vs prev. close", ACCENT_GREEN));
        metricsRow.add(createMetricCard("COST BASIS", costBasisValLabel, "Total invested", TEXT_MAIN));

        // Section Title & Add Holding Button
        final JLabel sectionTitle = new JLabel("Your Holdings");
        sectionTitle.setFont(new Font("Serif", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_MAIN);
        sectionTitle.setBounds(0, 175, 200, 25);

        final JButton addHoldingBtn = new JButton("+ Add Holding");
        addHoldingBtn.setBackground(ACCENT_GREEN);
        addHoldingBtn.setForeground(Color.BLACK);
        addHoldingBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        addHoldingBtn.setFocusPainted(false);
        addHoldingBtn.setBounds(810, 170, 130, 35);
        addHoldingBtn.addActionListener(e -> {
            viewManagerModel.setState("add holding");
            viewManagerModel.firePropertyChanged();
        });

        // Detailed Table Panel matching screenshot columns
        final JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBounds(0, 220, 940, 300);
        tablePanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        final String[] columnNames = {
                "TICKER / NAME", "SHARES", "AVG COST", "CURR. PRICE", "GAIN / LOSS", "GAIN %", ""
        };
        detailedHoldingsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        final JTable holdingsTable = new JTable(detailedHoldingsTableModel);
        holdingsTable.setBackground(CARD_BG);
        holdingsTable.setForeground(TEXT_MAIN);
        holdingsTable.setGridColor(BORDER_COLOR);
        holdingsTable.setRowHeight(32);
        holdingsTable.getTableHeader().setBackground(CARD_BG);
        holdingsTable.getTableHeader().setForeground(TEXT_MUTED);
        holdingsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));

        // Set width for the delete column
        holdingsTable.getColumnModel().getColumn(6).setPreferredWidth(40);
        holdingsTable.getColumnModel().getColumn(6).setMaxWidth(50);

        // === SINGLE-CLICK MOUSE LISTENER FOR THE "×" COLUMN ===
        holdingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = holdingsTable.rowAtPoint(e.getPoint());
                int col = holdingsTable.columnAtPoint(e.getPoint());

                // If column 6 ("×") is clicked
                if (col == 6 && row >= 0) {
                    String fullTickerString = (String) holdingsTable.getValueAt(row, 0);
                    if (fullTickerString != null && !fullTickerString.isEmpty()) {
                        String tickerToDelete;
                        if (fullTickerString.contains(" - ")) {
                            tickerToDelete = fullTickerString.split(" - ")[0].trim();
                        } else {
                            tickerToDelete = fullTickerString.trim();
                        }

                        if (deleteHoldingController != null) {
                            deleteHoldingController.execute(tickerToDelete);
                        }
                    }
                }
            }
        });

        final JScrollPane scrollPane = new JScrollPane(holdingsTable);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(null);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Footer status info
        lastUpdatedLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lastUpdatedLabel.setForeground(TEXT_MUTED);
        lastUpdatedLabel.setBounds(0, 530, 400, 20);
        updateLastUpdatedTime();

        panel.add(titleLabel);
        panel.add(metricsRow);
        panel.add(sectionTitle);
        panel.add(addHoldingBtn);
        panel.add(tablePanel);
        panel.add(lastUpdatedLabel);

        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valLbl, JLabel subLbl, Color valColor) {
        final JPanel card = new JPanel(null);
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setBounds(20, 15, 200, 15);

        valLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        valLbl.setForeground(valColor);
        valLbl.setBounds(18, 35, 200, 30);

        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(TEXT_MUTED);
        subLbl.setBounds(20, 65, 200, 15);

        card.add(titleLbl);
        card.add(valLbl);
        card.add(subLbl);
        return card;
    }

    private JPanel createMetricCard(String title, JLabel valLbl, String subText, Color valColor) {
        final JLabel subLbl = new JLabel(subText);
        return createMetricCard(title, valLbl, subLbl, valColor);
    }

    private void updateLastUpdatedTime() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
        lastUpdatedLabel.setText("Last updated: " + now.format(formatter));
    }

    private void updateViewFromState(LoggedInState state) {
        if (state.getHoldings() != null) {
            BigDecimal totalPortfolioValue = BigDecimal.ZERO;
            BigDecimal totalCostBasis = BigDecimal.ZERO;
            BigDecimal totalGainLoss = BigDecimal.ZERO;
            BigDecimal totalDailyChange = BigDecimal.ZERO;

            if (detailedHoldingsTableModel != null) {
                detailedHoldingsTableModel.setRowCount(0);
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

                if (detailedHoldingsTableModel != null) {
                    detailedHoldingsTableModel.addRow(new Object[]{
                            stock != null ? stock.getTickerSymbol() + " - " + stock.getCompanyName() : "",
                            holding.getNumberOfShares(),
                            String.format("$%.2f", holding.getAveragePrice()),
                            String.format("$%.2f", holding.calculateTotalValue().doubleValue() / holding.getNumberOfShares()), // or use average price if current price isn't a getter
                            String.format("$%.2f", holdingGain.doubleValue()),
                            String.format("%+.2f%%", holding.calculateGainLossPercentage().doubleValue()),
                            "x" // The delete button symbol
                    });
                }
            }

            BigDecimal allTimePercentage = BigDecimal.ZERO;
            if (totalCostBasis.compareTo(BigDecimal.ZERO) > 0) {
                allTimePercentage = totalGainLoss
                        .divide(totalCostBasis, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            // Update Summary Labels Dynamically
            portfolioValLabel.setText(String.format("$%.2f", totalPortfolioValue.doubleValue()));
            totalPnlValLabel.setText(String.format("$%.2f", totalGainLoss.doubleValue()));
            if (totalGainLoss.compareTo(BigDecimal.ZERO) < 0) {
                totalPnlValLabel.setForeground(new Color(239, 68, 68));
            } else {
                totalPnlValLabel.setForeground(ACCENT_GREEN);
            }

            todaysChangeValLabel.setText(String.format("$%.2f", totalDailyChange.doubleValue()));
            if (totalDailyChange.compareTo(BigDecimal.ZERO) < 0) {
                todaysChangeValLabel.setForeground(new Color(239, 68, 68));
            } else {
                todaysChangeValLabel.setForeground(ACCENT_GREEN);
            }

            costBasisValLabel.setText(String.format("$%.2f", totalCostBasis.doubleValue()));
            holdingsCountLabel.setText(state.getHoldings().size() + " positions");
        }
        updateLastUpdatedTime();
    }

    // Renders the "×" text styled like a clean button
    private static class DeleteButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public DeleteButtonRenderer() {
            setOpaque(true);
            setForeground(new Color(156, 163, 175)); // TEXT_MUTED
            setBackground(new Color(17, 24, 39));   // CARD_BG
            setBorder(null);
            setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Handles clicks on the "×" button to trigger deletion
    private static class DeleteButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String clickedTicker;
        private JTable table;
        private DeleteHoldingController controller;

        public DeleteButtonEditor(JCheckBox checkBox, JTable table, DeleteHoldingController controller) {
            super(checkBox);
            this.table = table;
            this.controller = controller;
            button = new JButton("×");
            button.setOpaque(true);
            button.setForeground(new Color(239, 68, 68)); // Red color for delete
            button.setBackground(new Color(17, 24, 39));
            button.setBorder(null);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));

            button.addActionListener(e -> {
                fireEditingStopped();
                if (controller != null && clickedTicker != null) {
                    controller.execute(clickedTicker);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Extract just the ticker symbol from column 0 (e.g., "AAPL - Apple Inc." -> "AAPL")
            String fullTickerString = (String) table.getValueAt(row, 0);
            if (fullTickerString != null && fullTickerString.contains(" - ")) {
                clickedTicker = fullTickerString.split(" - ")[0].trim();
            } else {
                clickedTicker = fullTickerString;
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "×";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            if (state != null) {
                updateViewFromState(state);
            }
        }
    }
}