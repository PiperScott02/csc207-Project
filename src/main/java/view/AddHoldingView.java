package view;

import interface_adapter.add_holding.AddHoldingController;
import interface_adapter.add_holding.AddHoldingState;
import interface_adapter.add_holding.AddHoldingViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AddHoldingView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "add holding";
    private final AddHoldingViewModel addHoldingViewModel;
    private final AddHoldingController addHoldingController;
    private final ViewManagerModel viewManagerModel;

    private final JTextField tickerInputField = new JTextField(15);
    private final JTextField sharesInputField = new JTextField(15);
    private final JTextField priceInputField = new JTextField(15);

    private final JButton addHoldingButton;
    private final JButton backButton;
    private final JButton clearButton;

    public AddHoldingView(AddHoldingViewModel addHoldingViewModel, AddHoldingController addHoldingController,
                          ViewManagerModel viewManagerModel) {
        this.addHoldingViewModel = addHoldingViewModel;
        this.addHoldingController = addHoldingController;
        this.viewManagerModel = viewManagerModel;
        this.addHoldingViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Header
        JLabel title = new JLabel(AddHoldingViewModel.TITLE_LABEL, JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Center Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder(""));

        // Helper text intro
        JLabel introLabel = new JLabel("Enter the details of the stock you own.");
        introLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(introLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Ticker Row
        formPanel.add(createFieldPanel(AddHoldingViewModel.TICKER_LABEL, tickerInputField, "Example: AAPL, MSFT, TSLA"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Shares Row
        formPanel.add(createFieldPanel(AddHoldingViewModel.SHARES_LABEL, sharesInputField, "Enter a positive number."));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Price Row
        formPanel.add(createFieldPanel(AddHoldingViewModel.PRICE_LABEL, priceInputField, "Enter the average price you paid per share."));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Note Box
        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.setBorder(BorderFactory.createTitledBorder("Note"));
        JTextArea noteText = new JTextArea("Current price will be retrieved automatically using the stock API when you add the holding.");
        noteText.setEditable(false);
        noteText.setOpaque(false);
        noteText.setLineWrap(true);
        noteText.setWrapStyleWord(true);
        notePanel.add(noteText, BorderLayout.CENTER);
        formPanel.add(notePanel);

        add(formPanel, BorderLayout.CENTER);

        // Bottom Button Panel
        JPanel buttonPanel = new JPanel();
        backButton = new JButton(AddHoldingViewModel.BACK_BUTTON_LABEL);
        addHoldingButton = new JButton(AddHoldingViewModel.ADD_BUTTON_LABEL);
        clearButton = new JButton(AddHoldingViewModel.CLEAR_BUTTON_LABEL);

        buttonPanel.add(backButton);
        buttonPanel.add(addHoldingButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addHoldingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (evt.getSource().equals(addHoldingButton)) {
                    AddHoldingState currentState = addHoldingViewModel.getState();
                    try {
                        double shares = Double.parseDouble(sharesInputField.getText());
                        double price = Double.parseDouble(priceInputField.getText());

                        addHoldingController.execute(
                                tickerInputField.getText(),
                                shares,
                                price
                        );
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(AddHoldingView.this, "Please enter valid numerical values for shares and price.");
                    }
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tickerInputField.setText("");
                sharesInputField.setText("");
                priceInputField.setText("");
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

    private JPanel createFieldPanel(String labelText, JTextField textField, String subText) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));

        JPanel inputSubPanel = new JPanel(new BorderLayout());
        inputSubPanel.add(textField, BorderLayout.NORTH);

        JLabel subLabel = new JLabel(subText);
        subLabel.setForeground(Color.BLUE);
        subLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        inputSubPanel.add(subLabel, BorderLayout.SOUTH);

        panel.add(label, BorderLayout.WEST);
        panel.add(inputSubPanel, BorderLayout.CENTER);
        return panel;
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
                }
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}