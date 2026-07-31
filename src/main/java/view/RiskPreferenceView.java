package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
import interface_adapter.risk_preference.RiskPreferenceController;
import interface_adapter.risk_preference.RiskPreferenceViewModel;

import java.util.EnumSet;
import java.util.Set;

import entity.InvestmentGoal;
import entity.RiskLevel;
import entity.TimeHorizon;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import interface_adapter.risk_preference.RiskPreferenceState;



/**
 * The screen where a user selects their investment risk preferences.
 */
public class RiskPreferenceView extends JPanel
        implements PropertyChangeListener {

    private static final String LOGGED_IN_VIEW_NAME = "logged in";

    private final String viewName = "risk preference";
    private final ViewManagerModel viewManagerModel;
    private final RiskPreferenceController controller;
    private final RiskPreferenceViewModel viewModel;

    private final JRadioButton conservativeButton =
            new JRadioButton("Conservative (Low Risk)");

    private final JRadioButton moderateButton =
            new JRadioButton("Moderate (Medium Risk)");

    private final JRadioButton aggressiveButton =
            new JRadioButton("Aggressive (High Risk)");

    private final JCheckBox capitalPreservationBox =
            new JCheckBox("Capital Preservation");

    private final JCheckBox incomeGenerationBox =
            new JCheckBox("Income Generation");

    private final JCheckBox longTermGrowthBox =
            new JCheckBox("Long-Term Growth");

    private final JCheckBox shortTermGainsBox =
            new JCheckBox("Short-Term Gains");

    private final JCheckBox speculationBox =
            new JCheckBox("Speculation / High Growth");

    private final JComboBox<String> timeHorizonBox =
            new JComboBox<>(new String[] {
                    "Less than 1 year",
                    "1–3 years",
                    "3–5 years",
                    "5–10 years",
                    "More than 10 years"
            });

    private final JLabel currentProfileLabel =
            new JLabel("Current Risk Profile: Not Set");

    private final JLabel lastUpdatedLabel =
            new JLabel("Last Updated: --");

    private final JButton saveButton =
            new JButton("Save Preferences");

    private final JButton resetButton =
            new JButton("Reset to Default");

    /**
     * Creates the risk-preference screen.
     *
     * @param viewManagerModel controls which application screen is visible
     * @param controller handles risk-preference actions
     * @param viewModel stores the risk-preference screen state
     */
    public RiskPreferenceView(
            ViewManagerModel viewManagerModel,
            RiskPreferenceController controller,
            RiskPreferenceViewModel viewModel) {

        this.viewManagerModel = viewManagerModel;
        this.controller = controller;
        this.viewModel = viewModel;

        viewModel.addPropertyChangeListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                controller.load();
            }
        });

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
        final JPanel headerPanel = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );

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
        final JPanel formPanel = new JPanel(
                new GridLayout(3, 1, 0, 15)
        );

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
        final JPanel panel = new JPanel(
                new GridLayout(4, 1, 5, 5)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "1. Select Your Risk Tolerance"
                )
        );

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
        final JPanel panel = new JPanel(
                new GridLayout(3, 2, 5, 5)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "2. Select Your Investment Goals"
                )
        );

        panel.add(capitalPreservationBox);
        panel.add(incomeGenerationBox);
        panel.add(longTermGrowthBox);
        panel.add(shortTermGainsBox);
        panel.add(speculationBox);

        return panel;
    }

    /**
     * Creates the investment-time-horizon selection.
     *
     * @return the time-horizon panel
     */
    private JPanel createTimeHorizonPanel() {
        final JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "3. Select Your Investment Time Horizon"
                )
        );

        timeHorizonBox.setSelectedItem("5–10 years");

        panel.add(
                new JLabel("How long do you plan to invest?"),
                BorderLayout.WEST
        );

        panel.add(
                timeHorizonBox,
                BorderLayout.CENTER
        );

        return panel;
    }

    /**
     * Creates the status information and navigation buttons.
     *
     * @return the bottom panel
     */
    private JPanel createBottomPanel() {
        final JPanel bottomPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        final JPanel statusPanel = new JPanel(
                new GridLayout(2, 1)
        );

        statusPanel.add(currentProfileLabel);
        statusPanel.add(lastUpdatedLabel);

        final JPanel buttonPanel = new JPanel();

        final JButton backButton =
                new JButton("Back to Dashboard");

        backButton.addActionListener(event -> {
            viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        saveButton.addActionListener(event -> {
            final RiskLevel riskLevel = getSelectedRiskLevel();

            final Set<InvestmentGoal> investmentGoals =
                    getSelectedInvestmentGoals();

            final TimeHorizon timeHorizon =
                    getSelectedTimeHorizon();

            controller.execute(
                    riskLevel,
                    investmentGoals,
                    timeHorizon
            );
        });

        resetButton.addActionListener(event -> resetFormToDefault());

        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);

        bottomPanel.add(statusPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Returns the risk level selected by the user.
     *
     * @return the selected risk level, or null if none is selected
     */
    private RiskLevel getSelectedRiskLevel() {
        RiskLevel result = null;

        if (conservativeButton.isSelected()) {
            result = RiskLevel.CONSERVATIVE;
        }
        else if (moderateButton.isSelected()) {
            result = RiskLevel.MODERATE;
        }
        else if (aggressiveButton.isSelected()) {
            result = RiskLevel.AGGRESSIVE;
        }

        return result;
    }

    /**
     * Returns all investment goals selected by the user.
     *
     * @return the selected investment goals
     */
    private Set<InvestmentGoal> getSelectedInvestmentGoals() {
        final Set<InvestmentGoal> goals =
                EnumSet.noneOf(InvestmentGoal.class);

        if (capitalPreservationBox.isSelected()) {
            goals.add(InvestmentGoal.CAPITAL_PRESERVATION);
        }

        if (incomeGenerationBox.isSelected()) {
            goals.add(InvestmentGoal.INCOME_GENERATION);
        }

        if (longTermGrowthBox.isSelected()) {
            goals.add(InvestmentGoal.LONG_TERM_GROWTH);
        }

        if (shortTermGainsBox.isSelected()) {
            goals.add(InvestmentGoal.SHORT_TERM_GAINS);
        }

        if (speculationBox.isSelected()) {
            goals.add(InvestmentGoal.SPECULATION_HIGH_GROWTH);
        }

        return goals;
    }

    /**
     * Converts the selected dropdown option into a TimeHorizon value.
     *
     * @return the selected time horizon
     */
    private TimeHorizon getSelectedTimeHorizon() {
        final String selected =
                (String) timeHorizonBox.getSelectedItem();

        TimeHorizon result = null;

        if ("Less than 1 year".equals(selected)) {
            result = TimeHorizon.LESS_THAN_ONE_YEAR;
        }
        else if ("1–3 years".equals(selected)) {
            result = TimeHorizon.ONE_TO_THREE_YEARS;
        }
        else if ("3–5 years".equals(selected)) {
            result = TimeHorizon.THREE_TO_FIVE_YEARS;
        }
        else if ("5–10 years".equals(selected)) {
            result = TimeHorizon.FIVE_TO_TEN_YEARS;
        }
        else if ("More than 10 years".equals(selected)) {
            result = TimeHorizon.MORE_THAN_TEN_YEARS;
        }

        return result;
    }

    /**
     * Resets the form controls to their default values.
     */
    private void resetFormToDefault() {
        moderateButton.setSelected(true);

        capitalPreservationBox.setSelected(false);
        incomeGenerationBox.setSelected(false);
        longTermGrowthBox.setSelected(false);
        shortTermGainsBox.setSelected(false);
        speculationBox.setSelected(false);

        timeHorizonBox.setSelectedItem("5–10 years");
    }

    /**
     * Updates the screen when the risk-preference state changes.
     *
     * @param event property-change event from the ViewModel
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        final RiskPreferenceState state = viewModel.getState();

        updateFormFromState(state);

        if (state.getError() != null && !state.getError().isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Unable to Save Preferences",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (state.getRiskLevel() != null) {
            currentProfileLabel.setText(
                    "Current Risk Profile: "
                            + formatRiskLevel(state.getRiskLevel())
            );
        }

        if (state.getLastUpdated() != null) {
            final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "MMM d, yyyy h:mm a"
                    );

            lastUpdatedLabel.setText(
                    "Last Updated: "
                            + state.getLastUpdated().format(formatter)
            );
        }

        if (state.getMessage() != null
                && !state.getMessage().isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getMessage(),
                    "Risk Preference",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Converts a RiskLevel enum into user-friendly text.
     *
     * @param riskLevel the selected risk level
     * @return formatted display text
     */
    private String formatRiskLevel(RiskLevel riskLevel) {
        String result;

        switch (riskLevel) {
            case CONSERVATIVE:
                result = "Conservative (Low Risk)";
                break;
            case MODERATE:
                result = "Moderate (Medium Risk)";
                break;
            case AGGRESSIVE:
                result = "Aggressive (High Risk)";
                break;
            default:
                result = riskLevel.toString();
                break;
        }

        return result;
    }

    /**
     * Returns the name used by CardLayout.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Updates the form controls using the saved ViewModel state.
     *
     * @param state the current risk-preference state
     */
    private void updateFormFromState(RiskPreferenceState state) {
        if (state.getRiskLevel() == RiskLevel.CONSERVATIVE) {
            conservativeButton.setSelected(true);
        }
        else if (state.getRiskLevel() == RiskLevel.MODERATE) {
            moderateButton.setSelected(true);
        }
        else if (state.getRiskLevel() == RiskLevel.AGGRESSIVE) {
            aggressiveButton.setSelected(true);
        }

        final Set<InvestmentGoal> goals =
                state.getInvestmentGoals();

        capitalPreservationBox.setSelected(
                goals.contains(InvestmentGoal.CAPITAL_PRESERVATION)
        );

        incomeGenerationBox.setSelected(
                goals.contains(InvestmentGoal.INCOME_GENERATION)
        );

        longTermGrowthBox.setSelected(
                goals.contains(InvestmentGoal.LONG_TERM_GROWTH)
        );

        shortTermGainsBox.setSelected(
                goals.contains(InvestmentGoal.SHORT_TERM_GAINS)
        );

        speculationBox.setSelected(
                goals.contains(InvestmentGoal.SPECULATION_HIGH_GROWTH)
        );

        if (state.getTimeHorizon() != null) {
            switch (state.getTimeHorizon()) {
                case LESS_THAN_ONE_YEAR:
                    timeHorizonBox.setSelectedItem("Less than 1 year");
                    break;
                case ONE_TO_THREE_YEARS:
                    timeHorizonBox.setSelectedItem("1–3 years");
                    break;
                case THREE_TO_FIVE_YEARS:
                    timeHorizonBox.setSelectedItem("3–5 years");
                    break;
                case FIVE_TO_TEN_YEARS:
                    timeHorizonBox.setSelectedItem("5–10 years");
                    break;
                case MORE_THAN_TEN_YEARS:
                    timeHorizonBox.setSelectedItem("More than 10 years");
                    break;
                default:
                    break;
            }
        }
    }
}