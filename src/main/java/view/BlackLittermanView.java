package view;

import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.black_litterman.BlackLittermanState;
import interface_adapter.black_litterman.BlackLittermanViewModel;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BlackLittermanView extends JPanel implements PropertyChangeListener {
    public final String viewName = "Black-Litterman view";

    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39);

    private final BlackLittermanViewModel blackLittermanViewModel;
    private BlackLittermanController blackLittermanController;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    private final JLabel headerLabel = new JLabel("Your 5 most heavily weighted stocks:");

    // Stock 1 component
    private final JLabel stock1Label = new JLabel("1. [Stock Name] - Market estimated return: [X]% | Adjusted: [Y]%");
    private final JTextField stock1OpinionField = new JTextField(10);
    private final JComboBox<String> stock1ConfidenceBox = new JComboBox<>(new String[]{"None", "Low", "Medium", "High", "Very High"});

    // Stock 2 components
    private final JLabel stock2Label = new JLabel("2. [Stock Name] - Market estimated return: [X]% | Adjusted: [Y]%");
    private final JTextField stock2OpinionField = new JTextField(10);
    private final JComboBox<String> stock2ConfidenceBox = new JComboBox<>(new String[]{"None", "Low", "Medium", "High", "Very High"});

    // Stock 3 components
    private final JLabel stock3Label = new JLabel("3. [Stock Name] - Market estimated return: [X]% | Adjusted: [Y]%");
    private final JTextField stock3OpinionField = new JTextField(10);
    private final JComboBox<String> stock3ConfidenceBox = new JComboBox<>(new String[]{"None", "Low", "Medium", "High", "Very High"});

    // Stock 4 components
    private final JLabel stock4Label = new JLabel("4. [Stock Name] - Market estimated return: [X]% | Adjusted: [Y]%");
    private final JTextField stock4OpinionField = new JTextField(10);
    private final JComboBox<String> stock4ConfidenceBox = new JComboBox<>(new String[]{"None", "Low", "Medium", "High", "Very High"});

    // Stock 5 components
    private final JLabel stock5Label = new JLabel("5. [Stock Name] - Market estimated return: [X]% | Adjusted: [Y]%");
    private final JTextField stock5OpinionField = new JTextField(10);
    private final JComboBox<String> stock5ConfidenceBox = new JComboBox<>(new String[]{"None", "Low", "Medium", "High", "Very High"});

    private final JButton inputViews = new JButton("Input views");
    private final JButton backButton = new JButton("← Back to Profile");

    public BlackLittermanView(ViewManagerModel viewManagerModel,
                              BlackLittermanViewModel blackLittermanViewModel,
                              BlackLittermanController blackLittermanController,
                              LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.blackLittermanViewModel = blackLittermanViewModel;
        this.blackLittermanController = blackLittermanController;
        this.blackLittermanViewModel.addPropertyChangeListener(this);
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);

        if (blackLittermanViewModel.getState() != null) {
            updateStockRows(blackLittermanViewModel.getState());
        }

        if (blackLittermanViewModel.getState() != null && blackLittermanViewModel.getState().getUser() != null) {
            blackLittermanController.loadMarketData(blackLittermanViewModel.getState().getUser());
        }

        inputViews.addActionListener(e -> {
            BlackLittermanState currentState = blackLittermanViewModel.getState();
            Map<String, Double> userViews = new HashMap<>();
            Map<String, String> confidenceLevels = new HashMap<>();
            User user = currentState.getUser();

            extractViewIfValid(stock1Label.getText(), stock1OpinionField.getText(),
                    (String) stock1ConfidenceBox.getSelectedItem(), userViews, confidenceLevels);
            extractViewIfValid(stock2Label.getText(), stock2OpinionField.getText(),
                    (String) stock2ConfidenceBox.getSelectedItem(), userViews, confidenceLevels);
            extractViewIfValid(stock3Label.getText(), stock3OpinionField.getText(),
                    (String) stock3ConfidenceBox.getSelectedItem(), userViews, confidenceLevels);
            extractViewIfValid(stock4Label.getText(), stock4OpinionField.getText(),
                    (String) stock4ConfidenceBox.getSelectedItem(), userViews, confidenceLevels);
            extractViewIfValid(stock5Label.getText(), stock5OpinionField.getText(),
                    (String) stock5ConfidenceBox.getSelectedItem(), userViews, confidenceLevels);

            currentState.setUserViews(userViews);
            currentState.setConfidenceLevels(confidenceLevels);
            blackLittermanViewModel.setState(currentState);

            if (blackLittermanController != null) {
                blackLittermanController.execute(user, userViews, confidenceLevels);
            }
        });

        backButton.addActionListener(e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChanged();
        });
    }

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
        navLinksPanel.add(createSidebarNavLink("Portfolio Health", false, e -> {
            viewManagerModel.setState("portfolio health");
            viewManagerModel.firePropertyChanged();
        }));
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
        navLinksPanel.add(createSidebarNavLink("Black-Litterman", true, e -> {
        }));

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(SIDEBAR_BG);
        bottomPanel.setPreferredSize(new Dimension(240, 95));
        bottomPanel.setLayout(null);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        String username = loggedInViewModel.getState() != null && loggedInViewModel.getState().getUsername() != null
                ? loggedInViewModel.getState().getUsername().toUpperCase()
                : "USER";

        final JLabel welcomeLabel = new JLabel("WELCOME, " + username);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        welcomeLabel.setForeground(TEXT_MUTED);
        welcomeLabel.setBounds(20, 12, 180, 15);

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        final JLabel dateLabel = new JLabel(currentDate);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        dateLabel.setForeground(TEXT_MUTED);
        dateLabel.setBounds(20, 30, 180, 15);

        final JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 11));
        logoutButton.setForeground(TEXT_MAIN);
        logoutButton.setBackground(CARD_BG);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBounds(20, 52, 200, 30);
        logoutButton.addActionListener(e -> {
            viewManagerModel.setState("log in");
            viewManagerModel.firePropertyChanged();
        });

        bottomPanel.add(welcomeLabel);
        bottomPanel.add(dateLabel);
        bottomPanel.add(logoutButton);

        sidebarPanel.add(brandPanel, BorderLayout.NORTH);
        sidebarPanel.add(navLinksPanel, BorderLayout.CENTER);
        sidebarPanel.add(bottomPanel, BorderLayout.SOUTH);

        return sidebarPanel;
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
        final JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        final JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBackground(BG_DARK);

        centerContainer.add(createStockCard(stock1Label, stock1OpinionField, stock1ConfidenceBox));
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(createStockCard(stock2Label, stock2OpinionField, stock2ConfidenceBox));
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(createStockCard(stock3Label, stock3OpinionField, stock3ConfidenceBox));
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(createStockCard(stock4Label, stock4OpinionField, stock4ConfidenceBox));
        centerContainer.add(Box.createVerticalStrut(15));
        centerContainer.add(createStockCard(stock5Label, stock5OpinionField, stock5ConfidenceBox));
        centerContainer.add(Box.createVerticalStrut(25));

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setBackground(BG_DARK);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputViews.setBackground(ACCENT_GREEN);
        inputViews.setForeground(Color.BLACK);
        inputViews.setFont(new Font("SansSerif", Font.BOLD, 13));
        inputViews.setFocusPainted(false);
        inputViews.setBorderPainted(false);
        inputViews.setOpaque(true);
        inputViews.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inputViews.setPreferredSize(new Dimension(120, 36));

        backButton.setBackground(CARD_BG);
        backButton.setForeground(TEXT_MAIN);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(150, 36));

        buttonPanel.add(inputViews);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(backButton);

        centerContainer.add(buttonPanel);

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

        final JLabel title = new JLabel("Black-Litterman Expected Return Views");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        headerLabel.setForeground(TEXT_MUTED);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(backLink);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(headerLabel);
        headerPanel.add(Box.createVerticalStrut(20));

        return headerPanel;
    }

    private JPanel createStockCard(JLabel label, JTextField opinionField, JComboBox<String> confidenceBox) {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT_MAIN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JPanel inputSubRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        inputSubRow.setBackground(CARD_BG);
        inputSubRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel opinionTitle = new JLabel("OPINION (%)");
        opinionTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        opinionTitle.setForeground(TEXT_MUTED);

        opinionField.setBackground(BG_DARK);
        opinionField.setForeground(TEXT_MAIN);
        opinionField.setCaretColor(TEXT_MAIN);
        opinionField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        opinionField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        opinionField.setPreferredSize(new Dimension(180, 32));

        final JLabel confidenceTitle = new JLabel("CONFIDENCE");
        confidenceTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        confidenceTitle.setForeground(TEXT_MUTED);

        confidenceBox.setBackground(TEXT_MAIN);
        confidenceBox.setForeground(BG_DARK);
        confidenceBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        confidenceBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        confidenceBox.setPreferredSize(new Dimension(150, 32));

        final JPanel opPanel = new JPanel();
        opPanel.setLayout(new BoxLayout(opPanel, BoxLayout.Y_AXIS));
        opPanel.setBackground(CARD_BG);
        opPanel.add(opinionTitle);
        opPanel.add(Box.createVerticalStrut(5));
        opPanel.add(opinionField);

        final JPanel confPanel = new JPanel();
        confPanel.setLayout(new BoxLayout(confPanel, BoxLayout.Y_AXIS));
        confPanel.setBackground(CARD_BG);
        confPanel.add(confidenceTitle);
        confPanel.add(Box.createVerticalStrut(5));
        confPanel.add(confidenceBox);

        inputSubRow.add(opPanel);
        inputSubRow.add(Box.createHorizontalStrut(20));
        inputSubRow.add(confPanel);

        card.add(label);
        card.add(Box.createVerticalStrut(12));
        card.add(inputSubRow);

        return card;
    }

    private void extractViewIfValid(String rowLabelText, String opinionText, String confidence,
                                    Map<String, Double> views, Map<String, String> confidences) {
        if (opinionText != null && !opinionText.trim().isEmpty()) {
            String ticker = extractTickerFromText(rowLabelText);
            if (ticker != null && !ticker.contains("[")) {
                try {
                    double opinionVal = Double.parseDouble(opinionText.trim()) / 100.0;
                    views.put(ticker, opinionVal);

                    if (confidence == null || "None".equalsIgnoreCase(confidence)) {
                        confidences.put(ticker, "Medium");
                    } else {
                        confidences.put(ticker, confidence);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    private String extractTickerFromText(String labelText) {
        try {
            int dotIndex = labelText.indexOf('.');
            int dashIndex = labelText.indexOf('—');
            if (dashIndex == -1) {
                dashIndex = labelText.indexOf('-');
            }

            if (dotIndex != -1 && dashIndex != -1 && dashIndex > dotIndex) {
                return labelText.substring(dotIndex + 1, dashIndex).trim();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public void setBlackLittermanController(BlackLittermanController blackLittermanController) {
        this.blackLittermanController = blackLittermanController;

        BlackLittermanState currentState = blackLittermanViewModel.getState();
        if (currentState != null && currentState.getUser() != null && blackLittermanController != null) {
            blackLittermanController.execute(
                    currentState.getUser(),
                    currentState.getUserViews() != null ? currentState.getUserViews() : new HashMap<>(),
                    currentState.getConfidenceLevels() != null ? currentState.getConfidenceLevels() : new HashMap<>()
            );
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == blackLittermanViewModel) {
            BlackLittermanState state = (BlackLittermanState) evt.getNewValue();
            if (state != null) {
                if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                    JOptionPane.showMessageDialog(this, state.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updateStockRows(state);
            }
        } else if (evt.getPropertyName().equals("state") || evt.getPropertyName().equals("logged in")) {
            removeAll();
            add(createSidebarPanel(), BorderLayout.WEST);
            add(createMainContentPanel(), BorderLayout.CENTER);
            revalidate();
            repaint();

            if (blackLittermanController != null && loggedInViewModel.getState() != null
                    && loggedInViewModel.getState().getUser() != null) {
                User user = loggedInViewModel.getState().getUser();

                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        blackLittermanController.loadMarketData(user);
                        return null;
                    }
                }.execute();
            }
        }
    }

    private void updateStockRows(BlackLittermanState state) {
        List<String> topTickers = state.getTopTickers();
        Map<String, Double> marketReturns = state.getMarketReturns();
        Map<String, Double> adjustedReturns = state.getAdjustedReturns();

        JLabel[] labels = {stock1Label, stock2Label, stock3Label, stock4Label, stock5Label};

        for (int i = 0; i < labels.length; i++) {
            if (topTickers != null && i < topTickers.size()) {
                String ticker = topTickers.get(i);
                double mktRet = (marketReturns != null) ? marketReturns.getOrDefault(ticker,
                        0.0) * 100.0 : 0.0;
                double adjRet = (adjustedReturns != null) ? adjustedReturns.getOrDefault(ticker,
                        0.0) * 100.0 : 0.0;

                labels[i].setText(String.format(java.util.Locale.US,
                        "%d. %s — Market estimated return: %.2f%% | Adjusted: %.2f%%",
                        (i + 1), ticker, mktRet, adjRet));
            } else {
                labels[i].setText(String.format("%d. [Stock Name] — Market estimated return: [X]%% | Adjusted: [Y]%%", (i + 1)));
            }
        }
    }
}