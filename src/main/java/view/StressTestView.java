package view;

import entity.Stock;
import entity.StockHolding;
import entity.StressScenario;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.stress_test.StressTestController;
import interface_adapter.stress_test.StressTestState;
import interface_adapter.stress_test.StressTestViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class StressTestView extends JPanel implements ActionListener, PropertyChangeListener {

    /**
     * Dark UI color palette matching HoldingsView.
     */
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color NEG_RED = new Color(239, 68, 68);
    private static final Color ACCENT_GOLD = new Color(212, 175, 55);
    private static final Color BADGE_BG = new Color(30, 41, 59);

    private final String viewName = "stress test";
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final StressTestViewModel stressTestViewModel;
    private final StressTestController stressTestController;
    private final BlackLittermanController blackLittermanController;
    private final PortfolioHealthController portfolioHealthController;

    private final JLabel currentValLabel = new JLabel("$0.00");
    private final JLabel stressedValLabel = new JLabel("$0.00");
    private final JLabel lossLabel = new JLabel("$0.00");
    private final JLabel impactLabel = new JLabel("0.00%");
    private final JLabel activeScenarioTitle = new JLabel("SELECT A HISTORICAL MARKET EVENT");

    private final JLabel drawdownLabel = new JLabel("Market drawdown: --");
    private final JLabel recoveryLabel = new JLabel("Historical recovery: --");
    private final JLabel durationLabel = new JLabel("Duration: --");

    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel progressStressedLabel = new JLabel("$0.00");
    private final JLabel progressTotalLabel = new JLabel("of $0.00");

    private DefaultTableModel holdingImpactTableModel;
    private StressScenario currentScenario = null;

    public StressTestView(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          StressTestViewModel stressTestViewModel,
                          StressTestController stressTestController,
                          BlackLittermanController blackLittermanController,
                          PortfolioHealthController portfolioHealthController) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.stressTestViewModel = stressTestViewModel;
        this.stressTestController = stressTestController;
        this.blackLittermanController = blackLittermanController;
        this.portfolioHealthController = portfolioHealthController;

        this.loggedInViewModel.addPropertyChangeListener(this);
        this.stressTestViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(SidebarHelper.createSidebar("Stress Test",
                this,
                viewManagerModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController), BorderLayout.WEST);

        add(createScrollableContent(), BorderLayout.CENTER);
    }

    public String getViewName() {
        return viewName;
    }

    private JScrollPane createScrollableContent() {
        JPanel mainContent = createMainContentPanel();
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.getViewport().setBackground(BG_DARK);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(null);
        panel.setBackground(BG_DARK);
        panel.setPreferredSize(new Dimension(980, 880));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 40, 40));

        final JLabel titleLabel = new JLabel("Stress Test");
        titleLabel.setFont(new Font("Didot", Font.BOLD, 30));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBounds(30, 15, 200, 35);

        final JLabel sectionHeader = new JLabel("SELECT A HISTORICAL MARKET EVENT");
        sectionHeader.setFont(new Font("SansSerif", Font.BOLD, 10));
        sectionHeader.setForeground(TEXT_MUTED);
        sectionHeader.setBounds(30, 65, 300, 15);
        panel.add(sectionHeader);

        // --- ROW 1 CARDS ---
        panel.add(createScenarioCard("COVID-19 Crash", "Feb - Mar 2020 • 33 days", "Fastest bear market in history. Global lockdowns triggered a 34% S&P 500 collapse in 33 days as economies froze overnight.", "-34%", 30, 85));
        panel.add(createScenarioCard("2008 Financial Crisis", "Oct 2007 - Mar 2009 • 17 months", "The subprime mortgage collapse cascaded into a global credit freeze. The S&P 500 lost 57% peak-to-trough — the worst drawdown since the Great Depression.", "-57%", 340, 85));
        panel.add(createScenarioCard("Dot-com Bubble", "Mar 2000 - Oct 2002 • 31 months", "Speculative excess in internet stocks collapsed. Nasdaq fell 78%. Technology holdings were decimated while defensive sectors held relatively firm.", "-49%", 650, 85));

        // --- ROW 2 CARDS ---
        panel.add(createScenarioCard("2022 Rate Hike Bear Market", "Jan - Oct 2022 • 9 months", "The Fed's fastest tightening cycle in 40 years crushed growth stocks and bonds simultaneously. Nasdaq fell 33%, S&P 500 dropped 25%.", "-25%", 30, 205));
        panel.add(createScenarioCard("Black Monday", "Oct 19, 1987 • 1 day", "The largest single-day percentage crash in market history. The Dow fell 22.6% in a single session, driven by program trading and panic.", "-22%", 340, 205));
        panel.add(createScenarioCard("Russia-Ukraine Invasion", "Feb - Mar 2022 • 3 weeks", "Russia's invasion of Ukraine sent energy soaring and risk assets plunging. European markets hit hardest; energy sector surged.", "-12%", 650, 205));

        // --- RESULTS PANEL ---
        final JPanel resultPanel = new JPanel(null);
        resultPanel.setBackground(CARD_BG);
        resultPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        resultPanel.setBounds(30, 335, 920, 170);

        activeScenarioTitle.setBounds(25, 18, 400, 20);
        activeScenarioTitle.setForeground(ACCENT_GOLD);
        activeScenarioTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        resultPanel.add(activeScenarioTitle);

        resultPanel.add(createMetricCard("CURRENT VALUE", currentValLabel, TEXT_MAIN, 25, 50));
        resultPanel.add(createMetricCard("STRESSED VALUE", stressedValLabel, TEXT_MAIN, 250, 50));
        resultPanel.add(createMetricCard("ESTIMATED LOSS", lossLabel, NEG_RED, 480, 50));
        resultPanel.add(createMetricCard("PORTFOLIO IMPACT", impactLabel, NEG_RED, 710, 50));

        // Divider line inside result panel
        JSeparator sep = new JSeparator();
        sep.setBounds(25, 120, 870, 2);
        sep.setForeground(BORDER_COLOR);
        resultPanel.add(sep);

        drawdownLabel.setBounds(25, 132, 220, 20);
        drawdownLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        drawdownLabel.setForeground(TEXT_MUTED);
        resultPanel.add(drawdownLabel);

        recoveryLabel.setBounds(260, 132, 250, 20);
        recoveryLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        recoveryLabel.setForeground(TEXT_MUTED);
        resultPanel.add(recoveryLabel);

        durationLabel.setBounds(520, 132, 200, 20);
        durationLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        durationLabel.setForeground(TEXT_MUTED);
        resultPanel.add(durationLabel);

        panel.add(resultPanel);

        // --- PROJECTED PORTFOLIO VALUE SECTION ---
        final JLabel projTitle = new JLabel("PROJECTED PORTFOLIO VALUE");
        projTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        projTitle.setForeground(TEXT_MUTED);
        projTitle.setBounds(30, 520, 300, 15);
        panel.add(projTitle);

        final JPanel progressCard = new JPanel(null);
        progressCard.setBackground(CARD_BG);
        progressCard.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        progressCard.setBounds(30, 540, 920, 60);

        progressStressedLabel.setBounds(20, 18, 100, 25);
        progressStressedLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        progressStressedLabel.setForeground(TEXT_MAIN);
        progressCard.add(progressStressedLabel);

        progressBar.setBounds(130, 22, 690, 16);
        progressBar.setBackground(BORDER_COLOR);
        progressBar.setForeground(NEG_RED);
        progressBar.setOpaque(true);
        progressCard.add(progressBar);

        progressTotalLabel.setBounds(835, 18, 80, 25);
        progressTotalLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        progressTotalLabel.setForeground(TEXT_MUTED);
        progressCard.add(progressTotalLabel);

        panel.add(progressCard);

        // --- PER-HOLDING IMPACT TABLE ---
        final JLabel tableTitle = new JLabel("PER-HOLDING IMPACT");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        tableTitle.setForeground(TEXT_MUTED);
        tableTitle.setBounds(30, 620, 300, 15);
        panel.add(tableTitle);

        final String[] columnNames = {
                "TICKER", "SECTOR", "CURRENT PRICE", "STRESSED PRICE", "CURRENT VALUE", "ESTIMATED LOSS"
        };
        holdingImpactTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        final JTable impactTable = new JTable(holdingImpactTableModel);
        impactTable.setBackground(CARD_BG);
        impactTable.setForeground(TEXT_MAIN);
        impactTable.setGridColor(BORDER_COLOR);
        impactTable.setRowHeight(36);
        impactTable.getTableHeader().setBackground(CARD_BG);
        impactTable.getTableHeader().setForeground(TEXT_MUTED);
        impactTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));
        impactTable.setRowSelectionAllowed(false);
        impactTable.setFocusable(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(column == 0 ? JLabel.LEFT : JLabel.CENTER);
                setBackground(CARD_BG);
                setForeground(column == 3 || column == 5 ? NEG_RED : TEXT_MAIN);
                return c;
            }
        };
        for (int i = 0; i < impactTable.getColumnCount(); i++) {
            impactTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        final JScrollPane tableScroll = new JScrollPane(impactTable);
        tableScroll.getViewport().setBackground(CARD_BG);
        tableScroll.setBounds(30, 645, 920, 180);
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        panel.add(tableScroll);

        panel.add(titleLabel);
        return panel;
    }

    private JPanel createScenarioCard(String title, String timeline, String desc, String shock, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 290, 110);
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setBounds(15, 12, 180, 20);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLbl.setForeground(TEXT_MAIN);
        card.add(titleLbl);

        JLabel badge = new JLabel(shock, SwingConstants.CENTER);
        badge.setBounds(225, 12, 50, 20);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(NEG_RED);
        badge.setBackground(BADGE_BG);
        badge.setOpaque(true);
        card.add(badge);

        JLabel timeLbl = new JLabel(timeline);
        timeLbl.setBounds(15, 34, 260, 15);
        timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        timeLbl.setForeground(TEXT_MUTED);
        card.add(timeLbl);

        JTextArea descArea = new JTextArea(desc);
        descArea.setBounds(15, 54, 260, 45);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 10));
        descArea.setForeground(TEXT_MUTED);
        descArea.setBackground(CARD_BG);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setFocusable(false);
        card.add(descArea);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                BigDecimal shockDecimal = parseShockToDecimal(shock);
                currentScenario = new StressScenario(title, timeline.split("•")[0].trim(), desc, shockDecimal);
                stressTestController.execute(currentScenario);
            }
        });

        return card;
    }

    private BigDecimal parseShockToDecimal(String shockStr) {
        try {
            String clean = shockStr.replace("%", "").trim();
            return new BigDecimal(clean).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return new BigDecimal("-0.30");
        }
    }

    private JPanel createMetricCard(String title, JLabel valLbl, Color valColor, int x, int y) {
        final JPanel card = new JPanel(null);
        card.setBounds(x, y, 200, 60);
        card.setBackground(CARD_BG);

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setBounds(0, 0, 200, 15);

        valLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        valLbl.setForeground(valColor);
        valLbl.setBounds(0, 20, 200, 30);

        card.add(titleLbl);
        card.add(valLbl);
        return card;
    }

    private void updateViewFromStressState(StressTestState state) {
        if (state != null && holdingImpactTableModel != null) {
            holdingImpactTableModel.setRowCount(0);
            java.util.List<String> tickers = state.getTickers();
            java.util.List<String> sectors = state.getSectors();
            java.util.List<BigDecimal> currPrices = state.getCurrentPrices();
            java.util.List<BigDecimal> stressedPrices = state.getStressedPrices();
            java.util.List<BigDecimal> currValues = state.getCurrentValues();
            java.util.List<BigDecimal> estLosses = state.getEstimatedLosses();

            for (int i = 0; i < tickers.size(); i++) {
                holdingImpactTableModel.addRow(new Object[]{
                        tickers.get(i),
                        sectors.get(i),
                        String.format("$%.2f", currPrices.get(i)),
                        String.format("$%.2f", stressedPrices.get(i)),
                        String.format("$%.2f", currValues.get(i)),
                        String.format("$%.2f", estLosses.get(i))
                });
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            if (evt.getNewValue() instanceof StressTestState) {
                final StressTestState state = (StressTestState) evt.getNewValue();
                activeScenarioTitle.setText(state.getScenarioName().toUpperCase());
                currentValLabel.setText(String.format("$%.2f", state.getCurrentValue().doubleValue()));
                stressedValLabel.setText(String.format("$%.2f", state.getStressedValue().doubleValue()));
                lossLabel.setText(String.format("$%.2f", state.getEstimatedLoss().doubleValue()));
                impactLabel.setText(String.format("%+.2f%%", state.getImpactPercentage().doubleValue()));

                drawdownLabel.setText("Market drawdown: " + state.getImpactPercentage() + "%");
                recoveryLabel.setText("Historical recovery: 5 months");
                durationLabel.setText("Duration: 33 days");

                progressStressedLabel.setText(String.format("$%.2f", state.getStressedValue().doubleValue()));
                progressTotalLabel.setText("of $" + state.getCurrentValue());

                double cur = state.getCurrentValue().doubleValue();
                double str = state.getStressedValue().doubleValue();
                if (cur > 0) {
                    int pct = (int) ((str / cur) * 100);
                    progressBar.setValue(Math.max(0, Math.min(100, pct)));
                }

                updateViewFromStressState(state);

                if (state.getEstimatedLoss().compareTo(BigDecimal.ZERO) < 0) {
                    lossLabel.setForeground(NEG_RED);
                    impactLabel.setForeground(NEG_RED);
                } else {
                    lossLabel.setForeground(ACCENT_GREEN);
                    impactLabel.setForeground(ACCENT_GREEN);
                }
            } else if (evt.getNewValue() instanceof LoggedInState) {
                if (currentScenario == null) {
                    currentScenario = new StressScenario(
                            "COVID-19 Crash",
                            "Feb - Mar 2020",
                            "Fastest bear market in history.",
                            new BigDecimal("-0.34")
                    );
                }
                stressTestController.execute(currentScenario);
            }
        }
    }
}