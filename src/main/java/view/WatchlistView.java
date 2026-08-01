package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistState;
import interface_adapter.watchlist.WatchlistViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WatchlistView extends JPanel implements PropertyChangeListener {

    private final WatchlistViewModel watchlistViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    private final JLabel titleLabel = new JLabel("Your Watchlist");
    private final JTextArea watchlistArea = new JTextArea(10, 30);

    public WatchlistView(WatchlistViewModel watchlistViewModel,
                         ViewManagerModel viewManagerModel,
                         LoggedInViewModel loggedInViewModel) {
        this.watchlistViewModel = watchlistViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;

        this.watchlistViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Navigation / Action Panel
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Back Button setup
        JButton backButton = new JButton("← Back to Profile");
        backButton.addActionListener(e -> {
            this.viewManagerModel.setState(this.loggedInViewModel.getViewName());
            this.viewManagerModel.firePropertyChanged();
        });

        // Add Watchlist Item Button setup
        JButton addWatchlistButton = new JButton("+ Add Watchlist Item");
        addWatchlistButton.addActionListener(e -> {
            this.viewManagerModel.setState("add watchlist");
            this.viewManagerModel.firePropertyChanged();
        });

        topButtonPanel.add(backButton);
        topButtonPanel.add(Box.createHorizontalStrut(10));
        topButtonPanel.add(addWatchlistButton);

        // Title setup
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Watchlist display area setup
        watchlistArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(watchlistArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Build the panel
        add(topButtonPanel);
        add(Box.createVerticalStrut(15));
        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(scrollPane);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof WatchlistState state) {
            if (state.getItems() != null && !state.getItems().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (WatchlistState.WatchlistStockItem item : state.getItems()) {
                    sb.append(item.getTicker()).append(" - ")
                            .append(item.getCompanyName())
                            .append(" | Close: ").append(item.getClose())
                            .append(" | Change: ").append(item.getDailyPriceChange())
                            .append("\n");
                }
                watchlistArea.setText(sb.toString());
            } else {
                watchlistArea.setText("Your watchlist is currently empty.");
            }
        }
    }

    public String getViewName() {
        return watchlistViewModel.getViewName();
    }
}