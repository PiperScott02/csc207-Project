package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import interface_adapter.ViewManagerModel;

/**
 * The screen where a user selects their investment risk preferences.
 */
public class RiskPreferenceView extends JPanel {

    private static final String LOGGED_IN_VIEW_NAME = "logged in";

    private final String viewName = "risk preference";
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates the risk-preference screen.
     *
     * @param viewManagerModel controls which application screen is visible
     */
    public RiskPreferenceView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the title section.
     *
     * @return the header panel
     */
    private JPanel createHeaderPanel() {
        final JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        final JLabel title = new JLabel(
                "PortfolioPilot",
                SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        final JLabel subtitle = new JLabel(
                "Risk Preference Settings",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 17));

        headerPanel.add(title);
        headerPanel.add(subtitle);

        return headerPanel;
    }

    /**
     * Creates the main preference form.
     *
     * @return the form panel
     */
    private JPanel createFormPanel() {
        final JPanel formPanel = new JPanel(new GridLayout(3, 1, 0, 15));

        formPanel.add(createRiskLevelPanel());
        formPanel.add(createGoalsPanel());
        formPanel.add(createTimeHorizonPanel());

        return formPanel;
    }

    /**
     * Creates the risk-level radio buttons.
     *
     * @return the risk-level panel
     */
    private JPanel createRiskLevelPanel() {
        final JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "1. Select Your Risk Tolerance"
                )
        );

        final JRadioButton conservativeButton =
                new JRadioButton("Conservative (Low Risk)");

        final JRadioButton moderateButton =
                new JRadioButton("Moderate (Medium Risk)");

        final JRadioButton aggressiveButton =
                new JRadioButton("Aggressive (High Risk)");

        final ButtonGroup riskGroup = new ButtonGroup();
        riskGroup.add(conservativeButton);
        riskGroup.add(moderateButton);
        riskGroup.add(aggressiveButton);

        moderateButton.setSelected(true);

        panel.add(new JLabel("Choose one option:"));
        panel.add(conservativeButton);
        panel.add(moderateButton);
        panel.add(aggressiveButton);

        return panel;
    }

    /**
     * Creates the investment-goals checkboxes.
     *
     * @return the goals panel
     */
    private JPanel createGoalsPanel() {
        final JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "2. Select Your Investment Goals"
                )
        );

        panel.add(new JCheckBox("Capital Preservation"));
        panel.add(new JCheckBox("Income Generation"));
        panel.add(new JCheckBox("Long-Term Growth"));
        panel.add(new JCheckBox("Short-Term Gains"));
        panel.add(new JCheckBox("Speculation / High Growth"));

        return panel;
    }

    /**
     * Creates the investment-time-horizon selection.
     *
     * @return the time-horizon panel
     */
    private JPanel createTimeHorizonPanel() {
        final JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "3. Select Your Investment Time Horizon"
                )
        );

        final String[] timeHorizons = {
                "Less than 1 year",
                "1–3 years",
                "3–5 years",
                "5–10 years",
                "More than 10 years"
        };

        final JComboBox<String> timeHorizonBox =
                new JComboBox<>(timeHorizons);

        timeHorizonBox.setSelectedItem("5–10 years");

        panel.add(
                new JLabel("How long do you plan to invest?"),
                BorderLayout.WEST
        );
        panel.add(timeHorizonBox, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the status information and navigation buttons.
     *
     * @return the bottom panel
     */
    private JPanel createBottomPanel() {
        final JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        final JPanel statusPanel = new JPanel(new GridLayout(2, 1));

        statusPanel.add(
                new JLabel("Current Risk Profile: Not Set")
        );
        statusPanel.add(
                new JLabel("Last Updated: --")
        );

        final JPanel buttonPanel = new JPanel();

        final JButton backButton =
                new JButton("Back to Dashboard");

        final JButton saveButton =
                new JButton("Save Preferences");

        final JButton resetButton =
                new JButton("Reset to Default");

        backButton.addActionListener(event -> {
            viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);

        bottomPanel.add(statusPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Returns the name used by CardLayout.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }
}