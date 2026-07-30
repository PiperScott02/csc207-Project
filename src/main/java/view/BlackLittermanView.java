package view;

import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.black_litterman.BlackLittermanState;
import interface_adapter.black_litterman.BlackLittermanViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackLittermanView extends JPanel implements PropertyChangeListener {
    public final String viewName = "Black-Litterman view";

    private final BlackLittermanViewModel blackLittermanViewModel;
    private BlackLittermanController blackLittermanController;
    private final ViewManagerModel viewManagerModel;

    private final JLabel headerLabel = new JLabel("Your 5 most heavily weighted stocks:");

    // Stock 1 components
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
    private final JButton backButton = new JButton("← Back to Profile"); // <-- Added Back Button

    public BlackLittermanView(ViewManagerModel viewManagerModel,
                              BlackLittermanViewModel blackLittermanViewModel,
                              BlackLittermanController blackLittermanController) {
        this.viewManagerModel = viewManagerModel;
        this.blackLittermanViewModel = blackLittermanViewModel;
        this.blackLittermanController = blackLittermanController;
        this.blackLittermanViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Black-Litterman Expected Return Views");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));

        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(headerLabel);
        add(Box.createVerticalStrut(12));

        add(createStockRow(stock1Label, stock1OpinionField, stock1ConfidenceBox));
        add(Box.createVerticalStrut(10));
        add(createStockRow(stock2Label, stock2OpinionField, stock2ConfidenceBox));
        add(Box.createVerticalStrut(10));
        add(createStockRow(stock3Label, stock3OpinionField, stock3ConfidenceBox));
        add(Box.createVerticalStrut(10));
        add(createStockRow(stock4Label, stock4OpinionField, stock4ConfidenceBox));
        add(Box.createVerticalStrut(10));
        add(createStockRow(stock5Label, stock5OpinionField, stock5ConfidenceBox));
        add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.add(inputViews);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Spacing between buttons
        buttonPanel.add(backButton); // <-- Added to button panel
        add(buttonPanel);

        // Update display if state already contains populated data upon view creation
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
                blackLittermanController.execute(user,
                        blackLittermanViewModel.getState().getUserViews(),
                        blackLittermanViewModel.getState().getConfidenceLevels()
                );
            }
        });

        // Back button behavior utilizing ViewManagerModel cleanly
        backButton.addActionListener(e -> {
            viewManagerModel.setState("logged in"); // Matches LoggedInView's view name
            viewManagerModel.firePropertyChanged();
        });
    }

    private JPanel createStockRow(JLabel label, JTextField opinionField, JComboBox<String> confidenceBox) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel inputSubRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputSubRow.add(new JLabel("Opinion (%):"));
        inputSubRow.add(opinionField);
        inputSubRow.add(Box.createHorizontalStrut(10));
        inputSubRow.add(new JLabel("Confidence:"));
        inputSubRow.add(confidenceBox);

        row.add(label);
        row.add(Box.createVerticalStrut(4));
        row.add(inputSubRow);
        return row;
    }

    private void extractViewIfValid(String rowLabelText, String opinionText, String confidence,
                                    Map<String, Double> views, Map<String, String> confidences) {
        if (opinionText != null && !opinionText.trim().isEmpty()
                && confidence != null && !"None".equalsIgnoreCase(confidence)) {

            String ticker = extractTickerFromText(rowLabelText);
            if (ticker != null && !ticker.contains("[")) {
                try {
                    double opinionVal = Double.parseDouble(opinionText.trim());
                    views.put(ticker, opinionVal);
                    confidences.put(ticker, confidence);
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    private String extractTickerFromText(String labelText) {
        try {
            int dotIndex = labelText.indexOf('.');
            int dashIndex = labelText.indexOf('-');
            if (dotIndex != -1 && dashIndex != -1 && dashIndex > dotIndex) {
                return labelText.substring(dotIndex + 1, dashIndex).trim();
            }
        } catch (Exception ignored) {}
        return null;
    }

    public void setBlackLittermanController(BlackLittermanController blackLittermanController) {
        this.blackLittermanController = blackLittermanController;

        // Safely invoke initial state execution once controller is bound
        BlackLittermanState currentState = blackLittermanViewModel.getState();
        if (currentState != null && currentState.getUser() != null && blackLittermanController != null) {
            // Ensure user has holdings/views initialized before calculating matrix
            blackLittermanController.execute(
                    currentState.getUser(),
                    currentState.getUserViews() != null ? currentState.getUserViews() : new HashMap<>(),
                    currentState.getConfidenceLevels() != null ? currentState.getConfidenceLevels() : new HashMap<>()
            );
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        BlackLittermanState state = (BlackLittermanState) evt.getNewValue();
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        updateStockRows(state);
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
                        "%d. %s - Market estimated return: %.2f%% | Adjusted: %.2f%%",
                        (i + 1), ticker, mktRet, adjRet));
            } else {
                labels[i].setText(String.format("%d. [Stock Name] - Market estimated return: [X]%% | Adjusted: [Y]%%", (i + 1)));
            }
        }
    }
}