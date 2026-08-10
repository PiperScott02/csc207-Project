package view;

import java.awt.*;
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
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.currency_conversion.CurrencyConversionController;
import interface_adapter.currency_conversion.CurrencyConversionState;
import interface_adapter.currency_conversion.CurrencyConversionViewModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;

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
    private final BlackLittermanController blackLittermanController;
    private final PortfolioHealthController portfolioHealthController;

    // === DARK MODE UI PALETTE ===
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);

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
            LoggedInViewModel loggedInViewModel,
            BlackLittermanController blackLittermanController,
            PortfolioHealthController portfolioHealthController) {

        this.viewManagerModel = viewManagerModel;
        this.controller = controller;
        this.viewModel = viewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.blackLittermanController = blackLittermanController;
        this.portfolioHealthController = portfolioHealthController;

        viewModel.addPropertyChangeListener(this);
        this.loggedInViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // Add the sidebar helper on the left and main container on the center
        add(SidebarHelper.createSidebar("Currency",
                this,
                viewManagerModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
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