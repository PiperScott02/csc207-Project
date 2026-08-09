package view;

import interface_adapter.add_holding.AddHoldingController;
import interface_adapter.add_holding.AddHoldingState;
import interface_adapter.add_holding.AddHoldingViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.black_litterman.BlackLittermanController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddHoldingView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);

    private final String viewName = "add holding";
    private final AddHoldingViewModel addHoldingViewModel;
    private final AddHoldingController addHoldingController;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final PortfolioHealthController portfolioHealthController;
    private final BlackLittermanController blackLittermanController;

    private final JTextField tickerInputField = new JTextField();
    private final JTextField sharesInputField = new JTextField();
    private final JTextField dateInputField = new JTextField();

    private JButton addHoldingButton;
    private JButton backButton;
    private JButton clearButton;

    public AddHoldingView(AddHoldingViewModel addHoldingViewModel,
                          AddHoldingController addHoldingController,
                          ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          PortfolioHealthController portfolioHealthController,
                          BlackLittermanController blackLittermanController) {
        this.addHoldingViewModel = addHoldingViewModel;
        this.addHoldingController = addHoldingController;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.portfolioHealthController = portfolioHealthController;
        this.blackLittermanController = blackLittermanController;
        this.addHoldingViewModel.addPropertyChangeListener(this);

        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // Reusing SidebarHelper for the navigation pane
        add(SidebarHelper.createSidebar("Holdings",
                this,
                viewManagerModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController), BorderLayout.WEST);

        add(createMainContentPanel(), BorderLayout.CENTER);

        // Action Listeners
        addHoldingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource().equals(addHoldingButton)) {
                    String tickerText = tickerInputField.getText().trim();
                    if (tickerText.isEmpty() || !tickerText.matches("^[a-zA-Z]+$")) {
                        JOptionPane.showMessageDialog(AddHoldingView.this, "Please enter a valid ticker symbol.");
                        return;
                    }

                    AddHoldingState currentState = addHoldingViewModel.getState();
                    currentState.setTicker(tickerInputField.getText());
                    currentState.setShares(sharesInputField.getText());
                    currentState.setPurchaseDate(dateInputField.getText());

                    try {
                        double shares = Double.parseDouble(sharesInputField.getText());

                        if (shares <= 0) {
                            JOptionPane.showMessageDialog(AddHoldingView.this, "Please enter a positive number for shares.");
                            return;
                        }

                        LocalDate purchaseDate = LocalDate.parse(dateInputField.getText());

                        addHoldingController.execute(
                                tickerInputField.getText(),
                                shares,
                                purchaseDate
                        );
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(AddHoldingView.this, "Please enter valid numerical values for shares.");
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(AddHoldingView.this, "Please enter a valid date in YYYY-MM-DD format.");
                    }
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddHoldingState currentState = addHoldingViewModel.getState();
                currentState.setTicker("");
                currentState.setShares("");
                currentState.setPurchaseDate("");

                tickerInputField.setText("");
                sharesInputField.setText("");
                dateInputField.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewManagerModel.setState("logged in");
                viewManagerModel.firePropertyChanged();
            }
        });
    }

    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(null);
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 30, 30, 40));

        // Title matching HoldingsView style and Didot font
        final JLabel titleLabel = new JLabel("Add New Holding");
        titleLabel.setFont(new Font("Didot", Font.BOLD, 30));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBounds(30, 85, 300, 35);

        // Form Card Container
        final JPanel card = new JPanel(null);
        card.setBackground(CARD_BG);
        card.setBounds(30, 140, 545, 430);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        // Ticker input field elements
        final JLabel tickerLbl = new JLabel("TICKER SYMBOL");
        tickerLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        tickerLbl.setForeground(TEXT_MUTED);
        tickerLbl.setBounds(25, 20, 200, 15);

        styleTextField(tickerInputField, "e.g. AAPL, MSFT, TSLA");
        tickerInputField.setBounds(25, 40, 495, 36);

        // Shares input field elements
        final JLabel sharesLbl = new JLabel("NUMBER OF SHARES");
        sharesLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        sharesLbl.setForeground(TEXT_MUTED);
        sharesLbl.setBounds(25, 90, 200, 15);

        styleTextField(sharesInputField, "Enter a positive number");
        sharesInputField.setBounds(25, 110, 495, 36);

        // Date input field elements
        final JLabel dateLbl = new JLabel("PURCHASE DATE (YYYY-MM-DD)");
        dateLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        dateLbl.setForeground(TEXT_MUTED);
        dateLbl.setBounds(25, 160, 200, 15);

        styleTextField(dateInputField, "YYYY-MM-DD");
        dateInputField.setBounds(25, 180, 495, 36);

        // Note section
        final JLabel noteLbl = new JLabel("NOTE");
        noteLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        noteLbl.setForeground(TEXT_MUTED);
        noteLbl.setBounds(25, 230, 200, 15);

        final JTextArea noteArea = new JTextArea("Enter the details of the stock you own. Historical closing price on the specified date will be retrieved automatically.");
        noteArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        noteArea.setForeground(TEXT_MUTED);
        noteArea.setBackground(CARD_BG);
        noteArea.setEditable(false);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        noteArea.setBounds(25, 250, 495, 30);

        // Styled buttons
        backButton = new JButton("Back to Dashboard");
        styleSecondaryButton(backButton);
        backButton.setBounds(25, 355, 155, 38);

        addHoldingButton = new JButton("+ Add Holding");
        stylePrimaryButton(addHoldingButton); // Using ACCENT_GREEN primary style
        addHoldingButton.setBounds(190, 355, 140, 38);

        clearButton = new JButton("Clear");
        styleSecondaryButton(clearButton);
        clearButton.setBounds(340, 355, 90, 38);

        card.add(tickerLbl);
        card.add(tickerInputField);
        card.add(sharesLbl);
        card.add(sharesInputField);
        card.add(dateLbl);
        card.add(dateInputField);
        card.add(noteLbl);
        card.add(noteArea);
        card.add(backButton);
        card.add(addHoldingButton);
        card.add(clearButton);

        panel.add(titleLabel);
        panel.add(card);

        return panel;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setBackground(BG_DARK);
        field.setForeground(TEXT_MAIN);
        field.setCaretColor(TEXT_MAIN);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(CARD_BG);
        button.setForeground(TEXT_MAIN);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_GREEN);
        button.setForeground(BG_DARK);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof AddHoldingState) {
            AddHoldingState state = (AddHoldingState) evt.getNewValue();
            if (state != null) {
                if (state.getAddHoldingError() != null) {
                    JOptionPane.showMessageDialog(this, state.getAddHoldingError());
                } else {
                    JOptionPane.showMessageDialog(this, "Holding successfully added!");
                    viewManagerModel.setState("holdings");
                    viewManagerModel.firePropertyChanged();
                }
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}