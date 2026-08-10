package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.stock.StockState;
import interface_adapter.stock.StockViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockView extends JPanel implements PropertyChangeListener {

    // === DARK MODE UI PALETTE ===
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color SUB_CARD_BG = new Color(22, 30, 46);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color BADGE_BG_GREEN = new Color(6, 78, 59);
    private static final Color NEG_RED = new Color(239, 68, 68);
    private static final Color BADGE_BG_RED = new Color(127, 29, 29);
    private static final Color BETA_ORANGE = new Color(245, 158, 11);

    private final StockViewModel stockViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    // Dynamic UI Components
    private final JLabel tickerLabel = new JLabel("—");
    private final JLabel changeBadgeLabel = new JLabel("0.0000");
    private final JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));

    private final JLabel detailsLabel = new JLabel("— • USA • ANALYTICS");

    private final JLabel closePriceValueLabel = new JLabel("$0.0000");
    private final JLabel dailyChangeValueLabel = new JLabel("0.0000");

    private final JLabel betaValueLabel = new JLabel("0.0000");
    private final JLabel betaSubLabel = new JLabel("—");

    private final JLabel alphaValueLabel = new JLabel("0.0000");
    private final JLabel alphaSubLabel = new JLabel("—");

    private final JLabel sharpeRatioValueLabel = new JLabel("0.0000");
    private final JLabel sharpeSubLabel = new JLabel("—");

    public StockView(StockViewModel stockViewModel,
                     ViewManagerModel viewManagerModel,
                     LoggedInViewModel loggedInViewModel) {
        this.stockViewModel = stockViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;

        this.stockViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createMainContentPanel());
    }

    private JPanel createMainContentPanel() {
        final JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));
        cardPanel.setPreferredSize(new Dimension(500, 600));
        cardPanel.setMaximumSize(new Dimension(500, 620));

        // 1. Top Header Row (Ticker Symbol + Gain/Loss Badge in-line, Close Button on right)
        final JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(CARD_BG);
        topHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        topHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        topHeader.setBorder(BorderFactory.createEmptyBorder(0, -10, 0, 0));

        final JPanel leftHeaderGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftHeaderGroup.setBackground(CARD_BG);

        tickerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        tickerLabel.setForeground(TEXT_MAIN);

        badgePanel.setBackground(BADGE_BG_GREEN);
        badgePanel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        changeBadgeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        changeBadgeLabel.setForeground(ACCENT_GREEN);
        badgePanel.add(changeBadgeLabel);

        leftHeaderGroup.add(tickerLabel);
        leftHeaderGroup.add(badgePanel);

        final JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        closeButton.setForeground(TEXT_MUTED);
        closeButton.setBackground(CARD_BG);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> {
            this.viewManagerModel.setState("search");
            this.viewManagerModel.firePropertyChanged();
        });

        topHeader.add(leftHeaderGroup, BorderLayout.WEST);
        topHeader.add(closeButton, BorderLayout.EAST);

        // 2. Company Details Row
        final JPanel detailsHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        detailsHeader.setBackground(CARD_BG);
        detailsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        detailsLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        detailsLabel.setForeground(TEXT_MUTED);
        detailsHeader.add(detailsLabel);

        // Divider 1
        final JSeparator sep1 = createSeparator();

        // 3. Stock Analytics Header
        final JLabel analyticsTitle = new JLabel("STOCK ANALYTICS");
        analyticsTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        analyticsTitle.setForeground(TEXT_MUTED);
        analyticsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 4. Primary Price Metrics Panel
        final JPanel priceRowPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        priceRowPanel.setBackground(CARD_BG);
        priceRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        priceRowPanel.add(createMetricColumn("CLOSE PRICE", closePriceValueLabel, TEXT_MAIN));
        priceRowPanel.add(createMetricColumn("DAILY CHANGE", dailyChangeValueLabel, ACCENT_GREEN));

        // 5. 3-Column Sub-Grid Panel (Beta, Alpha, Sharpe)
        final JPanel metricsGrid = new JPanel(new GridLayout(1, 3, 10, 0));
        metricsGrid.setBackground(CARD_BG);
        metricsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        metricsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        metricsGrid.add(createSubMetricCard("BETA", betaValueLabel, BETA_ORANGE, betaSubLabel));
        metricsGrid.add(createSubMetricCard("ALPHA", alphaValueLabel, ACCENT_GREEN, alphaSubLabel));
        metricsGrid.add(createSubMetricCard("SHARPE", sharpeRatioValueLabel, ACCENT_GREEN, sharpeSubLabel));

        // Divider 2
        final JSeparator sep2 = createSeparator();

        // 6. Footer Explanations Panel
        final JPanel expPanel = createExplanationSection();

        // Assemble Main Card
        cardPanel.add(topHeader);
        cardPanel.add(Box.createVerticalStrut(6));
        cardPanel.add(detailsHeader);
        cardPanel.add(Box.createVerticalStrut(14));
        cardPanel.add(sep1);
        cardPanel.add(Box.createVerticalStrut(14));
        cardPanel.add(analyticsTitle);
        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(priceRowPanel);
        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(metricsGrid);
        cardPanel.add(Box.createVerticalStrut(16));
        cardPanel.add(sep2);
        cardPanel.add(Box.createVerticalStrut(14));
        cardPanel.add(expPanel);

        return cardPanel;
    }

    private JSeparator createSeparator() {
        final JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setBackground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    private JPanel createMetricColumn(String title, JLabel valueLbl, Color valueColor) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        valueLbl.setForeground(valueColor);
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLbl);
        panel.add(Box.createVerticalStrut(4));
        panel.add(valueLbl);
        return panel;
    }

    private JPanel createSubMetricCard(String title, JLabel valueLbl, Color valueColor, JLabel subLbl) {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SUB_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 9));
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        valueLbl.setForeground(valueColor);
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 9));
        subLbl.setForeground(TEXT_MUTED);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(valueLbl);
        card.add(Box.createVerticalStrut(2));
        card.add(subLbl);
        return card;
    }

    private JPanel createExplanationSection() {
        final JPanel expPanel = new JPanel();
        expPanel.setLayout(new BoxLayout(expPanel, BoxLayout.Y_AXIS));
        expPanel.setBackground(CARD_BG);
        expPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        expPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        final JLabel title = new JLabel("WHAT THESE MEAN");
        title.setFont(new Font("SansSerif", Font.BOLD, 10));
        title.setForeground(TEXT_MUTED);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        expPanel.add(title);
        expPanel.add(Box.createVerticalStrut(8));
        expPanel.add(createExpRow("Beta", "Sensitivity to market moves. >1 is more volatile than S&P 500."));
        expPanel.add(Box.createVerticalStrut(4));
        expPanel.add(createExpRow("Alpha", "Excess return vs benchmark. Positive = outperformance."));
        expPanel.add(Box.createVerticalStrut(4));
        expPanel.add(createExpRow("Sharpe", "Risk-adjusted return. >1 is strong; <0.5 is weak."));

        return expPanel;
    }

    private JPanel createExpRow(String term, String definition) {
        final JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(CARD_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));

        final JLabel termLbl = new JLabel(term + "   ");
        termLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        termLbl.setForeground(ACCENT_GREEN);

        final JLabel defLbl = new JLabel(definition);
        defLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        defLbl.setForeground(TEXT_MUTED);

        row.add(termLbl);
        row.add(defLbl);
        return row;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof StockState state) {
            // Live Ticker Updates
            String ticker = state.getTicker() != null && !state.getTicker().isEmpty() ? state.getTicker() : "N/A";
            tickerLabel.setText(ticker);
            detailsLabel.setText(ticker + " • USA • ANALYTICS");

            // Live Close Price Updates
            double closePrice = parseSafely(state.getClose());
            closePriceValueLabel.setText(String.format("$%.4f", closePrice));

            // Live Daily Price Change & Badge Updates
            double dailyChange = parseSafely(state.getDailyPriceChange());
            String formattedChange = String.format("%+.4f", dailyChange);
            dailyChangeValueLabel.setText(formattedChange);
            changeBadgeLabel.setText(formattedChange);

            if (dailyChange < 0) {
                dailyChangeValueLabel.setForeground(NEG_RED);
                changeBadgeLabel.setForeground(NEG_RED);
                badgePanel.setBackground(BADGE_BG_RED);
            } else {
                dailyChangeValueLabel.setForeground(ACCENT_GREEN);
                changeBadgeLabel.setForeground(ACCENT_GREEN);
                badgePanel.setBackground(BADGE_BG_GREEN);
            }

            // Live Beta Value & Dynamic Sub-label Evaluation
            double beta = parseSafely(state.getBeta());
            betaValueLabel.setText(String.format("%.4f", beta));
            betaSubLabel.setText(beta > 1 ? ">1 = more volatile" : "<=1 = stable");

            // Live Alpha Value & Dynamic Sub-label Evaluation
            double alpha = parseSafely(state.getAlpha());
            alphaValueLabel.setText(String.format("%+.4f", alpha));
            alphaSubLabel.setText(alpha >= 0 ? "Outperforming" : "Underperforming");

            // Live Sharpe Ratio & Dynamic Sub-label Evaluation
            double sharpe = parseSafely(state.getSharpeRatio());
            sharpeRatioValueLabel.setText(String.format("%.4f", sharpe));
            sharpeSubLabel.setText(sharpe > 1 ? "Strong" : (sharpe >= 0.5 ? "Moderate" : "Weak"));
        }
    }

    private double parseSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getViewName() {
        return stockViewModel.getViewName();
    }
}