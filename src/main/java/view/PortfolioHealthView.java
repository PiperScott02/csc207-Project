package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthState;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PortfolioHealthView extends JPanel implements PropertyChangeListener {
    public final String viewName = "portfolioHealth view";

    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color ACCENT_RED = new Color(239, 68, 68);

    private final PortfolioHealthViewModel portfolioHealthViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final PortfolioHealthController portfolioHealthController;

    // Metric Labels
    private final JLabel scoreValueLabel = new JLabel("0.00 /100");
    private final JLabel riskPrefValueLabel = new JLabel("—");
    private final JLabel betaValueLabel = new JLabel("—");
    private final JLabel alphaValueLabel = new JLabel("—");
    private final JLabel sharpeValueLabel = new JLabel("—");

    // Advice Labels
    private final JLabel sharpeAdviceContentLabel = new JLabel("—");
    private final JLabel riskAlignmentAdviceContentLabel = new JLabel("—");
    private final JLabel diversificationAdviceContentLabel = new JLabel("—");
    private final JLabel newsAdviceContentLabel = new JLabel("—");

    public PortfolioHealthView(PortfolioHealthViewModel portfolioHealthViewModel,
                               ViewManagerModel viewManagerModel,
                               LoggedInViewModel loggedInViewModel,
                               PortfolioHealthController portfolioHealthController) {
        this.portfolioHealthViewModel = portfolioHealthViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.portfolioHealthController = portfolioHealthController;
        this.portfolioHealthViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // Add Sidebar on WEST using SidebarHelper
        add(SidebarHelper.createSidebar("Portfolio Health", this, viewManagerModel, loggedInViewModel, null, portfolioHealthController), BorderLayout.WEST);

        // Add Main Content on CENTER
        add(createMainContentPanel(), BorderLayout.CENTER);

        // Initialize with current state if available
        if (portfolioHealthViewModel.getState() != null) {
            updateView(portfolioHealthViewModel.getState());
        }
    }

    private JPanel createMainContentPanel() {
        final JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        final JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBackground(BG_DARK);

        // 1. Health Score Card
        centerContainer.add(createScoreCard());
        centerContainer.add(Box.createVerticalStrut(20));

        // 2. Metrics 4-Column Card Grid
        centerContainer.add(createMetricsGridCard());
        centerContainer.add(Box.createVerticalStrut(20));

        // 3. Advice Cards
        centerContainer.add(createAdviceCard("SHARPE ADVICE", sharpeAdviceContentLabel, ACCENT_RED));
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(createAdviceCard("RISK ALIGNMENT", riskAlignmentAdviceContentLabel, ACCENT_GREEN));
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(createAdviceCard("DIVERSIFICATION", diversificationAdviceContentLabel, ACCENT_RED));
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(createAdviceCard("NEWS ADVICE", newsAdviceContentLabel, ACCENT_GREEN));
        centerContainer.add(Box.createVerticalStrut(25));

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
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel backLink = new JLabel("← Back to Dashboard");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backLink.setForeground(TEXT_MUTED);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);

        backLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewManagerModel.setState("logged in");
                viewManagerModel.firePropertyChanged();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backLink.setForeground(TEXT_MAIN);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backLink.setForeground(TEXT_MUTED);
            }
        });

        final JLabel title = new JLabel("Portfolio Health Analytics");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(backLink);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(10));

        return headerPanel;
    }

    private JPanel createScoreCard() {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        final JLabel titleLabel = new JLabel("PORTFOLIO HEALTH SCORE");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        scoreValueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        scoreValueLabel.setForeground(TEXT_MAIN);
        scoreValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(scoreValueLabel);

        return card;
    }

    private JPanel createMetricsGridCard() {
        final JPanel card = new JPanel(new GridLayout(1, 4, 15, 0));
        card.setBackground(CARD_BG);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        card.add(createMetricColumn("RISK PREFERENCE", riskPrefValueLabel, null));
        card.add(createMetricColumn("BETA", betaValueLabel, "market sensitivity"));
        card.add(createMetricColumn("ALPHA", alphaValueLabel, "excess return"));
        card.add(createMetricColumn("SHARPE RATIO", sharpeValueLabel, "risk-adjusted return"));

        return card;
    }

    private JPanel createMetricColumn(String title, JLabel valueLabel, String subtitle) {
        final JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(CARD_BG);

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLbl.setForeground(TEXT_MUTED);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        valueLabel.setForeground(TEXT_MAIN);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        col.add(titleLbl);
        col.add(Box.createVerticalStrut(6));
        col.add(valueLabel);

        if (subtitle != null) {
            col.add(Box.createVerticalStrut(4));
            final JLabel subLbl = new JLabel(subtitle);
            subLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            subLbl.setForeground(TEXT_MUTED);
            subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            col.add(subLbl);
        }

        return col;
    }

    private JPanel createAdviceCard(String title, JLabel contentLabel, Color indicatorColor) {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        final JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setBackground(CARD_BG);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JPanel indicator = new JPanel();
        indicator.setBackground(indicatorColor);
        indicator.setPreferredSize(new Dimension(3, 12));

        final JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLbl.setForeground(TEXT_MUTED);

        titleRow.add(indicator);
        titleRow.add(titleLbl);

        contentLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        contentLabel.setForeground(TEXT_MAIN);
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleRow);
        card.add(Box.createVerticalStrut(10));
        card.add(contentLabel);

        return card;
    }

    private void updateView(PortfolioHealthState state) {
        scoreValueLabel.setText(safeFormatScore(state.getPortfolioHealthScore()));
        riskPrefValueLabel.setText(state.getRiskPreference() != null ? state.getRiskPreference() : "—");
        betaValueLabel.setText(safeFormatNumber(state.getBeta()));
        alphaValueLabel.setText(safeFormatAlpha(state.getAlpha()));
        sharpeValueLabel.setText(safeFormatNumber(state.getSharpeRatio()));

        sharpeAdviceContentLabel.setText(state.getSharpeAdvice() != null ? state.getSharpeAdvice() : "—");
        riskAlignmentAdviceContentLabel.setText(state.getRiskAlignmentAdvice() != null ? state.getRiskAlignmentAdvice() : "—");
        diversificationAdviceContentLabel.setText(state.getDiversificationAdvice() != null ? state.getDiversificationAdvice() : "—");
        newsAdviceContentLabel.setText(state.getNewsAdvice() != null ? state.getNewsAdvice() : "—");
    }

    private String safeFormatScore(Object val) {
        if (val == null) return "0.00 /100";
        if (val instanceof Number) {
            return String.format(java.util.Locale.US, "%.2f /100", ((Number) val).doubleValue());
        }
        try {
            double d = Double.parseDouble(val.toString());
            return String.format(java.util.Locale.US, "%.2f /100", d);
        } catch (NumberFormatException e) {
            return val.toString() + " /100";
        }
    }

    private String safeFormatNumber(Object val) {
        if (val == null) return "—";
        if (val instanceof Number) {
            return String.format(java.util.Locale.US, "%.2f", ((Number) val).doubleValue());
        }
        String s = val.toString();
        try {
            double d = Double.parseDouble(s);
            return String.format(java.util.Locale.US, "%.2f", d);
        } catch (NumberFormatException e) {
            return s; // Handles non-numeric strings like "NaN"
        }
    }

    private String safeFormatAlpha(Object val) {
        if (val == null) return "—";
        if (val instanceof Number) {
            return String.format(java.util.Locale.US, "%.2f%%", ((Number) val).doubleValue());
        }
        String s = val.toString();
        try {
            double d = Double.parseDouble(s);
            return String.format(java.util.Locale.US, "%.2f%%", d);
        } catch (NumberFormatException e) {
            return s; // Handles non-numeric strings like "NaN"
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof PortfolioHealthState) {
            PortfolioHealthState state = (PortfolioHealthState) evt.getNewValue();
            updateView(state);
        }
    }
}