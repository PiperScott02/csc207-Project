package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
 * Standalone page for converting the total portfolio value.
 */
public class CurrencyConversionView extends JPanel
        implements PropertyChangeListener {

    private static final String LOGGED_IN_VIEW_NAME = "logged in";

    private final String viewName = "currency conversion";

    private final ViewManagerModel viewManagerModel;
    private final CurrencyConversionController controller;
    private final CurrencyConversionViewModel viewModel;

    /*
     * Provides access to the currently logged-in user's portfolio.
     */
    private final LoggedInViewModel loggedInViewModel;

    private final JComboBox<String> currencyBox =
            new JComboBox<>(new String[] {"CAD", "USD"});

    private final JLabel originalValueLabel =
            new JLabel("CAD 0.00", SwingConstants.CENTER);

    private final JLabel convertedValueLabel =
            new JLabel("CAD 0.00", SwingConstants.CENTER);

    private final JLabel exchangeRateLabel =
            new JLabel("1 CAD = 1 CAD", SwingConstants.CENTER);

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

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createConversionPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        final JPanel panel = new JPanel(new GridLayout(2, 1));

        final JLabel title =
                new JLabel("PortfolioPilot", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        final JLabel subtitle =
                new JLabel(
                        "Portfolio Currency Conversion",
                        SwingConstants.CENTER
                );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 17));

        panel.add(title);
        panel.add(subtitle);

        return panel;
    }

    private JPanel createConversionPanel() {
        final JPanel panel = new JPanel(
                new GridLayout(6, 1, 0, 15)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Convert Total Portfolio Value"
                )
        );

        final JLabel originalHeading =
                new JLabel(
                        "Original Portfolio Value",
                        SwingConstants.CENTER
                );

        final JPanel currencyPanel = new JPanel();
        currencyPanel.add(new JLabel("Display Currency:"));
        currencyPanel.add(currencyBox);

        final JButton convertButton =
                new JButton("Convert Portfolio Value");

        convertButton.addActionListener(event -> {
            final LoggedInState loggedInState =
                    loggedInViewModel.getState();

            if (loggedInState == null
                    || loggedInState.getUser() == null
                    || loggedInState.getUser().getPortfolio() == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No active portfolio found."
                );
                return;
            }

            final BigDecimal portfolioValueCad =
                    loggedInState
                            .getUser()
                            .getPortfolio()
                            .calculateTotalPortfolioValue();

            final String targetCurrency =
                    (String) currencyBox.getSelectedItem();

            originalValueLabel.setText(
                    "CAD "
                            + String.format(
                            "%,.2f",
                            portfolioValueCad
                    )
            );

            controller.execute(
                    portfolioValueCad,
                    "CAD",
                    targetCurrency
            );
        });

        panel.add(originalHeading);
        panel.add(originalValueLabel);
        panel.add(currencyPanel);
        panel.add(convertedValueLabel);
        panel.add(exchangeRateLabel);
        panel.add(convertButton);

        return panel;
    }

    private JPanel createButtonPanel() {
        final JPanel panel = new JPanel();

        final JButton backButton =
                new JButton("Back to Dashboard");

        backButton.addActionListener(event -> {
            viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        panel.add(backButton);

        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        final CurrencyConversionState state =
                viewModel.getState();

        if (state.getError() != null
                && !state.getError().isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Currency Conversion Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        convertedValueLabel.setText(
                state.getToCurrency()
                        + " "
                        + String.format(
                        "%,.2f",
                        state.getConvertedValue()
                )
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

    public String getViewName() {
        return viewName;
    }
}