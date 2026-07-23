package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;

/**
 * The home screen displayed after a user successfully logs in.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private static final String LOGIN_VIEW_NAME = "log in";

    private static final String RISK_PREFERENCE_VIEW_NAME =
            "risk preference";

    private final String viewName = "logged in";
    private final ViewManagerModel viewManagerModel;

    private final JLabel welcomeLabel = new JLabel("Welcome");

    /**
     * Creates the home screen.
     *
     * @param loggedInViewModel contains information about the logged-in user
     * @param viewManagerModel controls which application screen is visible
     */
    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        ViewManagerModel viewManagerModel) {

        this.viewManagerModel = viewManagerModel;

        loggedInViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createHoldingsPanel(), BorderLayout.CENTER);
    }

    /**
     * Creates the upper portion of the dashboard.
     *
     * @return the completed top panel
     */
    private JPanel createTopPanel() {
        final JPanel topPanel = new JPanel(new BorderLayout(0, 15));

        topPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        topPanel.add(createSummaryPanel(), BorderLayout.CENTER);
        topPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return topPanel;
    }

    /**
     * Creates the application title and welcome message.
     *
     * @return the header panel
     */
    private JPanel createHeaderPanel() {
        final JPanel headerPanel = new JPanel(new BorderLayout());

        final JPanel titlePanel = new JPanel(new GridLayout(2, 1));

        final JLabel title = new JLabel(
                "PortfolioPilot",
                SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        final JLabel subtitle = new JLabel(
                "Personal Investment Portfolio Tracker",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Creates the portfolio-summary section.
     *
     * @return the summary panel
     */
    private JPanel createSummaryPanel() {
        final JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));

        summaryPanel.setBorder(
                BorderFactory.createTitledBorder("Portfolio Summary")
        );

        summaryPanel.add(createSummaryBox(
                "Total Portfolio Value",
                "$0.00"
        ));

        summaryPanel.add(createSummaryBox(
                "Total Gain / Loss",
                "$0.00"
        ));

        summaryPanel.add(createSummaryBox(
                "Daily Change",
                "$0.00 (0.00%)"
        ));

        return summaryPanel;
    }

    /**
     * Creates one summary box.
     *
     * @param heading heading shown above the value
     * @param value value displayed in the box
     * @return the completed summary box
     */
    private JPanel createSummaryBox(String heading, String value) {
        final JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(15, 10, 15, 10)
                )
        );

        final JLabel headingLabel = new JLabel(
                heading,
                SwingConstants.CENTER
        );
        headingLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        final JLabel valueLabel = new JLabel(
                value,
                SwingConstants.CENTER
        );
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        panel.add(headingLabel);
        panel.add(valueLabel);

        return panel;
    }

    /**
     * Creates the navigation buttons.
     *
     * @return the button panel
     */
    private JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 7, 8, 0));

        buttonPanel.add(createFeatureButton("Add Holding"));
        buttonPanel.add(createFeatureButton("Search Stock"));
        buttonPanel.add(createFeatureButton("Watchlist"));

        // The requested News button.
        buttonPanel.add(createFeatureButton("News"));

        buttonPanel.add(createFeatureButton("Insights"));

        final JButton riskPreferenceButton =
                new JButton("Risk Preference");

        riskPreferenceButton.addActionListener(event -> {
            viewManagerModel.setState(RISK_PREFERENCE_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        buttonPanel.add(riskPreferenceButton);

        final JButton logOutButton = new JButton("Log Out");

        logOutButton.addActionListener(event -> {
            viewManagerModel.setState(LOGIN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        buttonPanel.add(logOutButton);

        return buttonPanel;
    }

    /**
     * Creates a feature button that can be connected to another view later.
     *
     * @param buttonText text displayed on the button
     * @return the completed button
     */
    private JButton createFeatureButton(String buttonText) {
        final JButton button = new JButton(buttonText);

        button.addActionListener(event ->
                JOptionPane.showMessageDialog(
                        this,
                        buttonText + " screen will be connected here."
                )
        );

        return button;
    }

    /**
     * Creates the holdings table.
     *
     * @return the holdings panel
     */
    private JPanel createHoldingsPanel() {
        final JPanel holdingsPanel = new JPanel(new BorderLayout(0, 10));

        holdingsPanel.setBorder(
                BorderFactory.createTitledBorder("Your Holdings")
        );

        final String[] columnNames = {
                "Ticker",
                "Company",
                "Shares",
                "Avg Price",
                "Current Price",
                "Gain / Loss",
                "Gain %"
        };

        final DefaultTableModel tableModel =
                new DefaultTableModel(columnNames, 0) {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        final JTable holdingsTable = new JTable(tableModel);
        holdingsTable.setRowHeight(28);
        holdingsTable.setFillsViewportHeight(true);

        final JScrollPane scrollPane =
                new JScrollPane(holdingsTable);

        final JPanel statusPanel = new JPanel(new BorderLayout());

        statusPanel.add(
                new JLabel("Total Holdings: 0"),
                BorderLayout.WEST
        );

        statusPanel.add(
                new JLabel("Last updated: --"),
                BorderLayout.EAST
        );

        holdingsPanel.add(scrollPane, BorderLayout.CENTER);
        holdingsPanel.add(statusPanel, BorderLayout.SOUTH);

        return holdingsPanel;
    }

    /**
     * Updates the username displayed on the home screen.
     *
     * @param event property-change event from the view model
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName())) {
            final LoggedInState state =
                    (LoggedInState) event.getNewValue();

            final String username = state.getUsername();

            if (username == null || username.isBlank()) {
                welcomeLabel.setText("Welcome");
            }
            else {
                welcomeLabel.setText("Welcome, " + username);
            }
        }
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