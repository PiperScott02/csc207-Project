package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_watchlist.AddWatchlistController;
import interface_adapter.add_watchlist.AddWatchlistState;
import interface_adapter.add_watchlist.AddWatchlistViewModel;
import interface_adapter.delete_watchlist.DeleteWatchlistController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistController;
import interface_adapter.watchlist.WatchlistState;
import interface_adapter.watchlist.WatchlistViewModel;


/**
 * The View component for the WatchlistView screen.
 * Displays the user's saved stock watchlist, handles user input for adding stocks,
 * and responds to state updates via the Observer pattern.
 */
public class WatchlistView extends JPanel implements PropertyChangeListener {

    // Dark UI Color Palette
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39);

    private final String viewName = "watchlist";

    // View Models and Controllers
    private final WatchlistViewModel watchlistViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final AddWatchlistViewModel addWatchlistViewModel;
    private final AddWatchlistController addWatchlistController;
    private final DeleteWatchlistController deleteWatchlistController;
    private final WatchlistController watchlistController;

    // UI Components and State References
    private DefaultTableModel watchlistTableModel;
    private JPanel addFormCard;
    private JTextField tickerInputField;
    private JPanel tablePanel;

    /**
     * Constructs a new WatchlistView with the necessary view models and controllers.
     *
     * @param watchlistViewModel     the view model managing watchlist state data
     * @param viewManagerModel       the model responsible for switching views
     * @param loggedInViewModel      the view model for logged-in user details
     * @param addWatchlistViewModel  the view model managing add-watchlist status/errors
     * @param addWatchlistController the controller used to execute add-watchlist requests
     */
    public WatchlistView(WatchlistViewModel watchlistViewModel,
                         ViewManagerModel viewManagerModel,
                         LoggedInViewModel loggedInViewModel,
                         AddWatchlistViewModel addWatchlistViewModel,
                         AddWatchlistController addWatchlistController,
                         DeleteWatchlistController deleteWatchlistController,
                         WatchlistController watchlistController) {
        this.watchlistViewModel = watchlistViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.addWatchlistViewModel = addWatchlistViewModel;
        this.addWatchlistController = addWatchlistController;
        this.deleteWatchlistController = deleteWatchlistController;
        this.watchlistController = watchlistController;

        // 1. Register listener for view model updates
        this.watchlistViewModel.addPropertyChangeListener(this);
        this.addWatchlistViewModel.addPropertyChangeListener(this);
        this.viewManagerModel.addPropertyChangeListener(this);

        // 2. Configure primary panel components and style
        setBackground(BG_DARK);
        setLayout(new BorderLayout());
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);

        // 3. Sync the initial UI components with state data if present
        if (watchlistViewModel.getState() != null) {
            updateViewFromState(watchlistViewModel.getState());
        }
    }

    /**
     * Returns the name of this view for navigation and screen switching
     *
     * @return the view name string
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Creates and returns the navigation sidebar panel
     *
     * @return the configured sidebar JPanel
     */
    private JPanel createSidebarPanel() {
        // Initialize and style the main sidebar pane
        final JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        final JPanel brandPanel = new JPanel();
        brandPanel.setBackground(SIDEBAR_BG);
        brandPanel.setPreferredSize(new Dimension(240, 70));
        brandPanel.setLayout(null);

        final JLabel logoBadge = new JLabel("P", SwingConstants.CENTER);
        logoBadge.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoBadge.setForeground(TEXT_MAIN);
        logoBadge.setBackground(ACCENT_GREEN);
        logoBadge.setOpaque(true);
        logoBadge.setBounds(20, 20, 28, 28);

        final JLabel brandLabel = new JLabel("PortfolioPilot");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        brandLabel.setForeground(TEXT_MAIN);
        brandLabel.setBounds(58, 20, 150, 28);

        brandPanel.add(logoBadge);
        brandPanel.add(brandLabel);

        // Build the navigation links panel and wire view-switching actions
        final JPanel navLinksPanel = new JPanel();
        navLinksPanel.setBackground(SIDEBAR_BG);
        navLinksPanel.setLayout(new GridLayout(9, 1, 0, 2));
        navLinksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        navLinksPanel.add(createSidebarNavLink("Overview", false, e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Holdings", false, e -> {
            viewManagerModel.setState("holdings");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Watchlist", true, e -> {
        }));
        navLinksPanel.add(createSidebarNavLink("News & Sentiment", false, e -> {
            viewManagerModel.setState("news");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Portfolio Health", false, e -> {
        }));
        navLinksPanel.add(createSidebarNavLink("Risk Preference", false, e -> {
            viewManagerModel.setState("risk preference");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Currency", false, e -> {
            viewManagerModel.setState("currency conversion");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Search Stocks", false, e -> {
            viewManagerModel.setState("search");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Black-Litterman", false, e -> {
        }));

        // Assemble and return the final sidebar layout
        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(navLinksPanel, BorderLayout.CENTER);

        return sidebar;
    }

    /**
     * Helper method to create the styled navigation button for the sidebar
     *
     * @param text     the button display text
     * @param isActive whether this navigation link corresponds to the current active view
     * @param action   the action listener triggered when clicked
     * @return the configured navigation JButton
     */
    private JButton createSidebarNavLink(String text, boolean isActive, ActionListener action) {
        final JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 13));
        button.setForeground(isActive ? TEXT_MAIN : TEXT_MUTED);
        button.setBackground(isActive ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        button.addActionListener(action);
        return button;
    }

    /**
     * Creates and returns the main content panel containing the title, add button, form card, and table.
     *
     * @return the configured main content JPanel
     */
    private JPanel createMainContentPanel() {
        final JPanel panel = new JPanel(null);
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        final JLabel titleLabel = createTitleLabel();
        final JButton addWatchlistBtn = createAddWatchlistButton(panel);

        panel.add(titleLabel);
        panel.add(addWatchlistBtn);
        panel.add(addFormCard);
        panel.add(tablePanel);

        return panel;
    }

    /**
     * Creates the main screen title label.
     *
     * @return the title JLabel
     */
    private JLabel createTitleLabel() {
        final JLabel titleLabel = new JLabel("Watchlist");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBounds(20, 10, 200, 80);
        return titleLabel;
    }

    /**
     * Creates the "+ Add Company" action button and initializes dependent panels.
     *
     * @param parentPanel the parent container panel for revalidation layout updates
     * @return the configured add watchlist JButton
     */
    private JButton createAddWatchlistButton(JPanel parentPanel) {
        final JButton addWatchlistBtn = new JButton("+ Add Company");
        addWatchlistBtn.setBackground(ACCENT_GREEN);
        addWatchlistBtn.setForeground(Color.BLACK);
        addWatchlistBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        addWatchlistBtn.setBounds(800, 60, 160, 35);
        addWatchlistBtn.setFocusPainted(false);
        addWatchlistBtn.setBorderPainted(false);
        addWatchlistBtn.setOpaque(true);
        addWatchlistBtn.setContentAreaFilled(true);

        // Initialize subordinate UI sections
        addFormCard = createAddFormCard(parentPanel, addWatchlistBtn);
        tablePanel = createTablePanel();

        // Toggle button listener to show/hide the inline add form card
        addWatchlistBtn.addActionListener(e -> {
            final boolean isVisible = addFormCard.isVisible();
            addFormCard.setVisible(!isVisible);
            if (!isVisible) {
                tablePanel.setBounds(0, 285, 940, 260);
            } else {
                tablePanel.setBounds(0, 110, 940, 420);
            }
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        return addWatchlistBtn;
    }

    /**
     * Creates the inline form card used for inputting new ticker symbols.
     *
     * @param parentPanel     the parent container panel
     * @param addWatchlistBtn the trigger button reference
     * @return the configured add form JPanel
     */
    private JPanel createAddFormCard(JPanel parentPanel, JButton addWatchlistBtn) {
        final JPanel card = new JPanel(null);
        card.setBackground(CARD_BG);
        card.setBounds(20, 80, 550, 160);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        card.setVisible(false);

        final JLabel formHeader = new JLabel("ADD NEW WATCHLIST ITEM");
        formHeader.setFont(new Font("SansSerif", Font.BOLD, 10));
        formHeader.setForeground(TEXT_MUTED);
        formHeader.setBounds(25, 15, 250, 15);

        final JLabel tickerLabel = new JLabel("TICKER SYMBOL");
        tickerLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        tickerLabel.setForeground(TEXT_MUTED);
        tickerLabel.setBounds(25, 42, 200, 15);

        tickerInputField = new JTextField();
        tickerInputField.setBackground(BG_DARK);
        tickerInputField.setForeground(TEXT_MAIN);
        tickerInputField.setCaretColor(TEXT_MAIN);
        tickerInputField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tickerInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 60, 85)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        tickerInputField.setBounds(20, 62, 510, 35);

        final JButton submitBtn = new JButton("+ Add");
        submitBtn.setBackground(ACCENT_GREEN);
        submitBtn.setForeground(TEXT_MAIN);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        submitBtn.setBounds(0, 110, 120, 32);

        final JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(28, 38, 58));
        cancelBtn.setForeground(TEXT_MAIN);
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setBounds(80, 110, 120, 32);

        // Cancel Button Action
        cancelBtn.addActionListener(e -> {
            tickerInputField.setText("");
            card.setVisible(false);
            tablePanel.setBounds(0, 110, 940, 420);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        // Submit Button Action
        submitBtn.addActionListener(e -> {
            final String ticker = tickerInputField.getText().trim().toUpperCase();
            if (!ticker.isEmpty() && addWatchlistController != null) {
                addWatchlistController.execute(ticker);
            }
            tickerInputField.setText("");
            card.setVisible(false);
            tablePanel.setBounds(0, 110, 940, 420);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        card.add(formHeader);
        card.add(tickerLabel);
        card.add(tickerInputField);
        card.add(submitBtn);
        card.add(cancelBtn);

        return card;
    }

    /**
     * Creates and returns the table panel containing the watchlist items table.
     *
     * @return the configured table JPanel
     */
    private JPanel createTablePanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setBounds(20, 110, 940, 420);
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        final String[] columnNames = {"TICKER", "COMPANY", "CLOSE", "CHANGE", ""};
        watchlistTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        final JTable watchlistTable = new JTable(watchlistTableModel);
        watchlistTable.setBackground(CARD_BG);
        watchlistTable.setForeground(TEXT_MAIN);
        watchlistTable.setGridColor(BORDER_COLOR);
        watchlistTable.setRowHeight(38);
        watchlistTable.getTableHeader().setBackground(CARD_BG);
        watchlistTable.getTableHeader().setForeground(TEXT_MUTED);
        watchlistTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));

        // Configure custom cell rendering, alignment, and padding
        final DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1) {
                    setHorizontalAlignment(JLabel.LEFT);
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                }
                setBackground(isSelected ? new Color(28, 38, 58) : CARD_BG);
                setForeground(TEXT_MAIN);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        for (int i = 0; i < watchlistTable.getColumnCount(); i++) {
            watchlistTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        watchlistTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        watchlistTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        watchlistTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        watchlistTable.getColumnModel().getColumn(3).setPreferredWidth(140);
        watchlistTable.getColumnModel().getColumn(4).setPreferredWidth(45);
        watchlistTable.getColumnModel().getColumn(4).setMaxWidth(50);

        // Handle delete button row actions
        watchlistTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                final int row = watchlistTable.rowAtPoint(e.getPoint());
                final int col = watchlistTable.columnAtPoint(e.getPoint());
                if (col == 4 && row >= 0) {
                    final String ticker = (String) watchlistTable.getValueAt(row, 0);
                    if (ticker != null && !ticker.isEmpty() && deleteWatchlistController != null) {
                        deleteWatchlistController.execute(ticker);
                    }
                }
            }
        });

        final JScrollPane scrollPane = new JScrollPane(watchlistTable);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Refreshes the watchlist table with formatted stock data from the provided state.
     *
     * @param state the current watchlist state containing stock items and prices
     */
    private void updateViewFromState(WatchlistState state) {
        if (watchlistTableModel != null && state.getItems() != null) {
            watchlistTableModel.setRowCount(0);
            for (WatchlistState.WatchlistStockItem item : state.getItems()) {

                // Parse close price safely
                Object closeVal = "—";
                if (item.getClose() != null && !item.getClose().isEmpty()) {
                    try {
                        closeVal = String.format("$%.2f", Double.parseDouble(item.getClose()));
                    } catch (NumberFormatException e) {
                        closeVal = item.getClose();
                    }
                }

                // Parse daily price change safely
                Object changeVal = "—";
                if (item.getDailyPriceChange() != null && !item.getDailyPriceChange().isEmpty()) {
                    try {
                        changeVal = String.format("%+.4f", Double.parseDouble(item.getDailyPriceChange()));
                    } catch (NumberFormatException e) {
                        changeVal = item.getDailyPriceChange();
                    }
                }

                watchlistTableModel.addRow(new Object[]{
                        item.getTicker(),
                        item.getCompanyName(),
                        closeVal,
                        changeVal,
                        "×"
                });
            }
        }
    }

    /**
     * Responds to property change events by updating the view state or displaying error dialogs.
     *
     * @param evt the property change event containing the new state
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Only trigger a fetch when switching views to "watchlist"
        if ("view".equals(evt.getPropertyName()) && "watchlist".equals(evt.getNewValue())) {
            if (this.watchlistController != null && loggedInViewModel.getState() != null) {
                User currentUser = loggedInViewModel.getState().getUser();
                if (currentUser != null) {
                    this.watchlistController.execute(currentUser);
                }
            }
        }
        // Handle standard model updates from WatchlistViewModel
        else if (evt.getNewValue() instanceof WatchlistState state) {
            updateViewFromState(state);
        }
        // Handle error states from AddWatchlistViewModel
        else if (evt.getNewValue() instanceof AddWatchlistState state) {
            if (state.getAddWatchlistError() != null) {
                JOptionPane.showMessageDialog(this, state.getAddWatchlistError());
            }
        }
    }
}