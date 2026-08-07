package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;

import interface_adapter.ViewManagerModel;
import interface_adapter.risk_preference.RiskPreferenceController;
import interface_adapter.risk_preference.RiskPreferenceViewModel;

import entity.RiskLevel;

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
                new BorderLayout()
        );

        formPanel.add(
                createRiskLevelPanel(),
                BorderLayout.CENTER
        );

        return formPanel;
    }

    /**
     * Creates the risk-level radio buttons.
     *
     * @return the risk-level panel
     */
    private JPanel createRiskLevelPanel() {
        final JPanel outerPanel = new JPanel(new GridBagLayout());

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Select Your Risk Tolerance"
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 30, 20, 30
                        )
                )
        );

        panel.setPreferredSize(new Dimension(700, 280));

        final JLabel instruction =
                new JLabel("Choose one option:");

        instruction.setFont(
                new Font("SansSerif", Font.PLAIN, 18)
        );

        instruction.setAlignmentX(Component.LEFT_ALIGNMENT);

        conservativeButton.setFont(
                new Font("SansSerif", Font.PLAIN, 18)
        );

        moderateButton.setFont(
                new Font("SansSerif", Font.PLAIN, 18)
        );

        aggressiveButton.setFont(
                new Font("SansSerif", Font.PLAIN, 18)
        );

        final ButtonGroup riskGroup = new ButtonGroup();

        riskGroup.add(conservativeButton);
        riskGroup.add(moderateButton);
        riskGroup.add(aggressiveButton);

        moderateButton.setSelected(true);

        panel.add(instruction);
        panel.add(Box.createVerticalStrut(20));

        panel.add(conservativeButton);
        panel.add(Box.createVerticalStrut(20));

        panel.add(moderateButton);
        panel.add(Box.createVerticalStrut(20));

        panel.add(aggressiveButton);

        outerPanel.add(panel);

        return outerPanel;
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

            controller.execute(riskLevel);
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
     * Resets the form controls to their default values.
     */
    private void resetFormToDefault() {
        moderateButton.setSelected(true);
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
    }
}