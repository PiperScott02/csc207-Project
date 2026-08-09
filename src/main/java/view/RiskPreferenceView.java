package view;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;

import interface_adapter.ViewManagerModel;
import interface_adapter.risk_preference.RiskPreferenceController;
import interface_adapter.risk_preference.RiskPreferenceViewModel;
import interface_adapter.risk_preference.RiskPreferenceState;
import entity.RiskLevel;

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

    // Dark UI Color Palette
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39);

    // Layout Dimension Constants
    private static final int PANEL_PREFERRED_WIDTH = 750;
    private static final int PANEL_PREFERRED_HEIGHT = 260;

    // Risk tolerance selection buttons
    private final JRadioButton conservativeButton =
            new JRadioButton("Conservative (Low Risk)");

    private final JRadioButton moderateButton =
            new JRadioButton("Moderate (Medium Risk)");

    private final JRadioButton aggressiveButton =
            new JRadioButton("Aggressive (High Risk)");

    // Status and profile information display labels
    private final JLabel currentProfileLabel =
            new JLabel("Current Risk Profile: Not Set");

    private final JLabel lastUpdatedLabel =
            new JLabel("Last Updated: --");

    // Form interaction action buttons
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

        // 1. Register listener for view model updates
        viewModel.addPropertyChangeListener(this);

        // 2. Load initial data when the view becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                controller.load();
            }
        });

        // 3. Configure main panel layout and background
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setBackground(BG_DARK);

        // 4. Assemble UI sections
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
        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BG_DARK);

        final JLabel backLink = new JLabel("← Back to Dashboard");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backLink.setForeground(TEXT_MUTED);
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);

        backLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
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

        final JLabel title = new JLabel("Risk Preference Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(backLink);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(title);

        return headerPanel;
    }

    /**
     * Creates the main preference form.
     *
     * @return the form panel
     */
    private JPanel createFormPanel() {
        final JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(BG_DARK);
        formPanel.add(createRiskLevelPanel(), BorderLayout.CENTER);

        return formPanel;
    }

    /**
     * Creates the risk-level radio buttons.
     *
     * @return the risk-level panel
     */
    private JPanel createRiskLevelPanel() {
        final JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(BG_DARK);

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        BorderFactory.createEmptyBorder(25, 30, 25, 30)
                )
        );

        panel.setPreferredSize(new Dimension(PANEL_PREFERRED_WIDTH, PANEL_PREFERRED_HEIGHT));

        final JLabel sectionHeader = new JLabel("SELECT YOUR RISK TOLERANCE");
        sectionHeader.setFont(new Font("SansSerif", Font.BOLD, 12));
        sectionHeader.setForeground(TEXT_MUTED);
        sectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel instruction = new JLabel("Choose one option:");
        instruction.setFont(new Font("SansSerif", Font.PLAIN, 16));
        instruction.setForeground(TEXT_MAIN);
        instruction.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Style Radio Buttons for Dark Theme
        styleRadioButton(conservativeButton);
        styleRadioButton(moderateButton);
        styleRadioButton(aggressiveButton);

        final ButtonGroup riskGroup = new ButtonGroup();
        riskGroup.add(conservativeButton);
        riskGroup.add(moderateButton);
        riskGroup.add(aggressiveButton);

        moderateButton.setSelected(true);

        panel.add(sectionHeader);
        panel.add(Box.createVerticalStrut(15));
        panel.add(instruction);
        panel.add(Box.createVerticalStrut(20));

        panel.add(conservativeButton);
        panel.add(Box.createVerticalStrut(12));

        panel.add(moderateButton);
        panel.add(Box.createVerticalStrut(12));

        panel.add(aggressiveButton);

        outerPanel.add(panel);
        return outerPanel;
    }

    /**
     * Helper to style radio buttons for dark mode compatibility.
     *
     * @param button the radio button to style
     */
    private void styleRadioButton(JRadioButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setForeground(TEXT_MAIN);
        button.setBackground(CARD_BG);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Creates the status information and navigation buttons.
     *
     * @return the bottom panel
     */
    private JPanel createBottomPanel() {
        final JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(BG_DARK);

        final JPanel statusPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        statusPanel.setBackground(BG_DARK);

        currentProfileLabel.setForeground(TEXT_MAIN);
        currentProfileLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lastUpdatedLabel.setForeground(TEXT_MUTED);
        lastUpdatedLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        statusPanel.add(currentProfileLabel);
        statusPanel.add(lastUpdatedLabel);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_DARK);

        final JButton backButton = new JButton("Back to Dashboard");
        styleSecondaryButton(backButton);
        stylePrimaryButton(saveButton);
        styleSecondaryButton(resetButton);

        // Configure button action behaviors
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
     * Applies primary accent styling to the Save button (mint green).
     *
     * @param button the button to style
     */
    private void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_GREEN);
        button.setForeground(BG_DARK);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(9, 16, 9, 16)
        ));
    }

    /**
     * Applies dark styling to secondary buttons.
     *
     * @param button the button to style
     */
    private void styleSecondaryButton(JButton button) {
        button.setBackground(BG_DARK);
        button.setForeground(TEXT_MAIN);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(9, 16, 9, 16)
        ));
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

        // 1. Handle and display error dialog if present
        if (state.getError() != null && !state.getError().isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Unable to Save Preferences",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 2. Update current active risk profile display label
        if (state.getRiskLevel() != null) {
            currentProfileLabel.setText(
                    "Current Risk Profile: "
                            + formatRiskLevel(state.getRiskLevel())
            );
        }

        // 3. Format and update the last modification timestamp label
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

        // 4. Show confirmation success message dialog if present
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