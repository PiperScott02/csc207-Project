package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import interface_adapter.ViewManagerModel;
import interface_adapter.currency_conversion.CurrencyConversionController;
import interface_adapter.currency_conversion.CurrencyConversionState;
import interface_adapter.currency_conversion.CurrencyConversionViewModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;

/**
 * Standalone page for converting the total portfolio value from USD to a selected display currency.
 */
public class CurrencyConversionView extends JPanel
        implements PropertyChangeListener {

    private static final String LOGGED_IN_VIEW_NAME = "logged in";
    private final String viewName = "currency conversion";

    private final ViewManagerModel viewManagerModel;
    private final CurrencyConversionController controller;
    private final CurrencyConversionViewModel viewModel;
    private final LoggedInViewModel loggedInViewModel;

    // === DARK MODE UI PALETTE ===
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39);

    private final JComboBox<String> currencyBox =
            new JComboBox<>(new String[] {"CAD", "USD", "EUR", "GBP"});

    private final JLabel originalValueLabel =
            new JLabel("$0.00", SwingConstants.LEFT);

    private final JLabel convertedValueLabel =
            new JLabel("", SwingConstants.LEFT);

    private final JLabel exchangeRateLabel =
            new JLabel("", SwingConstants.LEFT);

    public CurrencyConversionView(
            ViewManagerModel viewManagerModel,
            CurrencyConversionController controller,
            CurrencyConversionViewModel viewModel,
            LoggedInViewModel loggedInViewModel) {

        this.viewManagerModel = viewManagerModel;
        this.controller = controller;
        this.viewModel = viewModel;
        this.loggedInViewModel = loggedInViewModel;

        viewModel.addPropertyChangeListener(this);
        this.loggedInViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // Add the sidebar on the left and main container on the center
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
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
        navLinksPanel.add(createSidebarNavLink("Portfolio Health", false, e -> {}));
        navLinksPanel.add(createSidebarNavLink("Risk Preference", false, e -> {
            viewManagerModel.setState("risk preference");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Currency", true, e -> {}));
        navLinksPanel.add(createSidebarNavLink("Search Stocks", false, e -> {
            viewManagerModel.setState("search");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Black-Litterman", false, e -> {}));

        // Bottom welcome panel matching screenshot
        final JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(SIDEBAR_BG);
        bottomPanel.setPreferredSize(new Dimension(240, 60));
        bottomPanel.setLayout(null);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        final JLabel welcomeLabel = new JLabel("WELCOME, HANA");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        welcomeLabel.setForeground(TEXT_MUTED);
        welcomeLabel.setBounds(20, 12, 180, 15);

        final JLabel dateLabel = new JLabel("Aug 7, 2026");
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
        final JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title Header matching screenshot "Currency"
        final JLabel titleLabel = new JLabel("Currency");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBounds(0, 10, 200, 40);

        // Back button
        final JButton backButton = new JButton("← Back to Dashboard");
        backButton.setForeground(TEXT_MUTED);
        backButton.setBackground(BG_DARK);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setHorizontalAlignment(SwingConstants.LEFT);
        backButton.setBounds(-5, 65, 160, 25);
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        backButton.addActionListener(event -> {
            viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        // Section Heading matching "Portfolio Currency Conversion"
        final JLabel sectionHeading = new JLabel("Portfolio Currency Conversion");
        sectionHeading.setFont(new Font("Serif", Font.BOLD, 22));
        sectionHeading.setForeground(TEXT_MAIN);
        sectionHeading.setBounds(0, 110, 450, 30);

        // Card Container matching the dark card in the screenshot
        final JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBounds(0, 155, 750, 360);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(4, 0, 4, 0);

        // Card Section Title
        final JLabel cardTitle = new JLabel("CONVERT TOTAL PORTFOLIO VALUE");
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        cardTitle.setForeground(TEXT_MUTED);
        cardPanel.add(cardTitle, gbc);

        // Original Portfolio Value Header
        final JLabel originalHeading = new JLabel("ORIGINAL PORTFOLIO VALUE (USD)");
        originalHeading.setFont(new Font("SansSerif", Font.BOLD, 10));
        originalHeading.setForeground(TEXT_MUTED);
        gbc.insets = new Insets(18, 0, 2, 0);
        cardPanel.add(originalHeading, gbc);

        // Original Value Large Display
        originalValueLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        originalValueLabel.setForeground(TEXT_MAIN);
        gbc.insets = new Insets(0, 0, 20, 0);
        cardPanel.add(originalValueLabel, gbc);

        // Display Currency Label & Box
        final JLabel currencyLabel = new JLabel("DISPLAY CURRENCY");
        currencyLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        currencyLabel.setForeground(TEXT_MUTED);
        gbc.insets = new Insets(10, 0, 2, 0);
        cardPanel.add(currencyLabel, gbc);

        currencyBox.setBackground(TEXT_MAIN);
        currencyBox.setForeground(BG_DARK);
        currencyBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        currencyBox.setPreferredSize(new Dimension(160, 36));
        gbc.insets = new Insets(0, 0, 20, 0);
        cardPanel.add(currencyBox, gbc);

        // Converted Value & Exchange Rate result labels
        convertedValueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        convertedValueLabel.setForeground(ACCENT_GREEN);
        gbc.insets = new Insets(5, 0, 2, 0);
        cardPanel.add(convertedValueLabel, gbc);

        exchangeRateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        exchangeRateLabel.setForeground(TEXT_MUTED);
        cardPanel.add(exchangeRateLabel, gbc);

        // Neon Green Convert Button
        final JButton convertButton = new JButton("Convert Portfolio Value");
        convertButton.setBackground(ACCENT_GREEN);
        convertButton.setForeground(Color.BLACK);
        convertButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        convertButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        convertButton.setPreferredSize(new Dimension(210, 40));
        convertButton.setFocusPainted(false);
        convertButton.setBorderPainted(false);
        convertButton.setOpaque(true);

        convertButton.addActionListener(event -> {
            final LoggedInState loggedInState = loggedInViewModel.getState();

            if (loggedInState == null
                    || loggedInState.getUser() == null
                    || loggedInState.getUser().getPortfolio() == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No active portfolio found."
                );
                return;
            }

            final BigDecimal portfolioValue =
                    loggedInState
                            .getUser()
                            .getPortfolio()
                            .calculateTotalPortfolioValue();

            final String targetCurrency =
                    (String) currencyBox.getSelectedItem();

            originalValueLabel.setText(
                    "$" + String.format("%,.2f", portfolioValue)
            );

            // Converts portfolio value from USD to the selected display currency
            controller.execute(
                    portfolioValue,
                    "USD",
                    targetCurrency
            );
        });

        gbc.insets = new Insets(15, 0, 5, 0);
        cardPanel.add(convertButton, gbc);

        mainPanel.add(titleLabel);
        mainPanel.add(backButton);
        mainPanel.add(sectionHeading);
        mainPanel.add(cardPanel);

        return mainPanel;
    }

    private void updatePortfolioDisplay() {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        if (loggedInState != null
                && loggedInState.getUser() != null
                && loggedInState.getUser().getPortfolio() != null) {
            BigDecimal portfolioValue = loggedInState.getUser().getPortfolio().calculateTotalPortfolioValue();
            originalValueLabel.setText("$" + String.format("%,.2f", portfolioValue));
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName()) || event.getNewValue() instanceof LoggedInState) {
            updatePortfolioDisplay();
        }

        final CurrencyConversionState state = viewModel.getState();
        if (state != null && state.getError() != null && !state.getError().isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Currency Conversion Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (state != null && state.getToCurrency() != null) {
            convertedValueLabel.setText(
                    state.getToCurrency()
                            + " "
                            + String.format("%,.2f", state.getConvertedValue())
            );

            exchangeRateLabel.setText(
                    "1 "
                            + state.getFromCurrency()
                            + " = "
                            + state.getExchangeRate()
                            + " "
                            + state.getToCurrency()
            );
        }
    }

    public String getViewName() {
        return viewName;
    }
}