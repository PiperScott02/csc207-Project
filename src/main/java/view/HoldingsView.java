package view;

import entity.Stock;
import entity.StockHolding;
import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.delete_holding.DeleteHoldingController;
import interface_adapter.portfolio_health.PortfolioHealthController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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

    /**
     * Dark UI color palette.
     */
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color NEG_RED = new Color(239, 68, 68);

    private final String viewName = "holdings";
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final DeleteHoldingController deleteHoldingController;
    private final PortfolioHealthController portfolioHealthController;
    private final BlackLittermanController blackLittermanController;

    private final JLabel portfolioValLabel = new JLabel("$0.00");
    private final JLabel totalPnlValLabel = new JLabel("$0.00");
    private final JLabel todaysChangeValLabel = new JLabel("$0.00");
    private final JLabel costBasisValLabel = new JLabel("$0.00");

    private final JLabel totalPnlSubLabel = new JLabel("+0.00%");
    private final JLabel todaysChangeSubLabel = new JLabel("vs prev. close");
    private final JLabel costBasisSubLabel = new JLabel("Total invested");

    private final JLabel holdingsCountLabel = new JLabel("0 positions");
    private final JLabel lastUpdatedLabel = new JLabel();

    private DefaultTableModel detailedHoldingsTableModel;

    public HoldingsView(ViewManagerModel viewManagerModel,
                        LoggedInViewModel loggedInViewModel,
                        DeleteHoldingController deleteHoldingController,
                        PortfolioHealthController portfolioHealthController,
                        BlackLittermanController blackLittermanController) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.deleteHoldingController = deleteHoldingController;
        this.portfolioHealthController = portfolioHealthController;
        this.blackLittermanController = blackLittermanController;

        this.loggedInViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(SidebarHelper.createSidebar("Holdings",
                this,
                viewManagerModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController), BorderLayout.WEST);

        add(createMainContentPanel(), BorderLayout.CENTER);

        if (loggedInViewModel.getState() != null) {
            updateViewFromState(loggedInViewModel.getState());
        }
    }

    public String getViewName() {
        return viewName;
    }

    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(null);
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 30, 30, 40));

        final JLabel titleLabel = new JLabel("Holdings");
        titleLabel.setFont(new Font("Didot", Font.BOLD, 30));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBounds(30, 25, 195, 35);

        final JPanel metricsRow = new JPanel(new GridLayout(1, 4, 15, 0));
        metricsRow.setBackground(BG_DARK);
        metricsRow.setBounds(30, 85, 940, 95);

        metricsRow.add(createMetricCard("PORTFOLIO VALUE", portfolioValLabel, holdingsCountLabel, TEXT_MAIN));
        metricsRow.add(createMetricCard("TOTAL P&L", totalPnlValLabel, totalPnlSubLabel, ACCENT_GREEN));
        metricsRow.add(createMetricCard("TODAY'S CHANGE", todaysChangeValLabel, todaysChangeSubLabel, ACCENT_GREEN));
        metricsRow.add(createMetricCard("COST BASIS", costBasisValLabel, costBasisSubLabel, TEXT_MAIN));

        final JButton addHoldingBtn = new JButton("+ Add Holding");
        addHoldingBtn.setBackground(ACCENT_GREEN);
        addHoldingBtn.setForeground(Color.WHITE);
        addHoldingBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        addHoldingBtn.setBounds(825, 520, 145, 35);
        addHoldingBtn.setFocusPainted(false);
        addHoldingBtn.setBorderPainted(false);
        addHoldingBtn.setOpaque(true);
        addHoldingBtn.setContentAreaFilled(true);
        addHoldingBtn.addActionListener(e -> {
            viewManagerModel.setState("add holding");
            viewManagerModel.firePropertyChanged();
        });

        final String[] columnNames = {
                "TICKER", "COMPANY", "SHARES", "AVG COST", "CURR. PRICE", "GAIN / LOSS", "GAIN %", ""
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

        holdingsTable.setRowSelectionAllowed(false);
        holdingsTable.setCellSelectionEnabled(false);
        holdingsTable.setFocusable(false);

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 1) {
                    setHorizontalAlignment(JLabel.LEFT);
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                }

                setBackground(CARD_BG);
                setForeground(TEXT_MAIN);
                return c;
            }
        };
        for (int i = 0; i < holdingsTable.getColumnCount(); i++) {
            holdingsTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        holdingsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        holdingsTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        holdingsTable.getColumnModel().getColumn(7).setPreferredWidth(40);
        holdingsTable.getColumnModel().getColumn(7).setMaxWidth(50);

        holdingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = holdingsTable.rowAtPoint(e.getPoint());
                int col = holdingsTable.columnAtPoint(e.getPoint());

                if (col == 7 && row >= 0) {
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
        scrollPane.setBounds(30, 200, 940, 300);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.setViewportBorder(null);

        lastUpdatedLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lastUpdatedLabel.setForeground(TEXT_MUTED);
        lastUpdatedLabel.setBounds(30, 520, 400, 20);
        updateLastUpdatedTime();

        panel.add(titleLabel);
        panel.add(metricsRow);
        panel.add(addHoldingBtn);
        panel.add(scrollPane);
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
                            stock != null ? stock.getTickerSymbol() : "",
                            stock != null ? stock.getCompanyName() : "",
                            holding.getNumberOfShares(),
                            String.format("$%.2f", holding.getAveragePrice()),
                            String.format("$%.2f", holding.calculateTotalValue().doubleValue() / holding.getNumberOfShares()),
                            String.format("$%.2f", holdingGain.doubleValue()),
                            String.format("%+.2f%%", holding.calculateGainLossPercentage().doubleValue()),
                            "×"
                    });
                }
            }

            BigDecimal allTimePercentage = BigDecimal.ZERO;
            if (totalCostBasis.compareTo(BigDecimal.ZERO) > 0) {
                allTimePercentage = totalGainLoss
                        .divide(totalCostBasis, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            BigDecimal prevCloseTotal = totalPortfolioValue.subtract(totalDailyChange);
            BigDecimal dailyPercentage = BigDecimal.ZERO;
            if (prevCloseTotal.compareTo(BigDecimal.ZERO) > 0) {
                dailyPercentage = totalDailyChange
                        .divide(prevCloseTotal, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            portfolioValLabel.setText(String.format("$%.2f", totalPortfolioValue.doubleValue()));
            holdingsCountLabel.setText(state.getHoldings().size() + " positions");

            totalPnlValLabel.setText(String.format("$%.2f", totalGainLoss.doubleValue()));
            totalPnlSubLabel.setText(String.format("%+.2f%%", allTimePercentage.doubleValue()));
            if (totalGainLoss.compareTo(BigDecimal.ZERO) < 0) {
                totalPnlValLabel.setForeground(NEG_RED);
                totalPnlSubLabel.setForeground(NEG_RED);
            } else {
                totalPnlValLabel.setForeground(ACCENT_GREEN);
                totalPnlSubLabel.setForeground(TEXT_MUTED);
            }

            todaysChangeValLabel.setText(String.format("$%.2f", totalDailyChange.doubleValue()));
            todaysChangeSubLabel.setText(String.format("%+.2f%% vs prev. close", dailyPercentage.doubleValue()));
            if (totalDailyChange.compareTo(BigDecimal.ZERO) < 0) {
                todaysChangeValLabel.setForeground(NEG_RED);
                todaysChangeSubLabel.setForeground(NEG_RED);
            } else {
                todaysChangeValLabel.setForeground(ACCENT_GREEN);
                todaysChangeSubLabel.setForeground(TEXT_MUTED);
            }

            costBasisValLabel.setText(String.format("$%.2f", totalCostBasis.doubleValue()));
        }
        updateLastUpdatedTime();
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