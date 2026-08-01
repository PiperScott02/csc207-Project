package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.add_watchlist.AddWatchlistController;
import interface_adapter.add_watchlist.AddWatchlistState;
import interface_adapter.add_watchlist.AddWatchlistViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Add Watchlist Item Use Case.
 */
public class AddWatchlistView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "add watchlist";

    private final AddWatchlistViewModel addWatchlistViewModel;
    private final AddWatchlistController addWatchlistController;
    private final ViewManagerModel viewManagerModel; // Added ViewManagerModel

    private final JTextField tickerInputField = new JTextField(15);
    private final JButton saveButton;
    private final JButton cancelButton;

    public AddWatchlistView(AddWatchlistViewModel addWatchlistViewModel,
                            AddWatchlistController addWatchlistController,
                            ViewManagerModel viewManagerModel) { // Accept ViewManagerModel in constructor
        this.addWatchlistViewModel = addWatchlistViewModel;
        this.addWatchlistController = addWatchlistController;
        this.viewManagerModel = viewManagerModel;

        addWatchlistViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(AddWatchlistViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        LabelTextPanel tickerInfo = new LabelTextPanel(
                new JLabel(AddWatchlistViewModel.TICKER_LABEL), tickerInputField);

        JPanel buttons = new JPanel();
        saveButton = new JButton(AddWatchlistViewModel.SAVE_BUTTON_LABEL);
        buttons.add(saveButton);
        cancelButton = new JButton(AddWatchlistViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(cancelButton);

        saveButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(saveButton)) {
                            AddWatchlistState currentState = addWatchlistViewModel.getState();
                            addWatchlistController.execute(
                                    tickerInputField.getText()
                            );
                        }
                    }
                }
        );

        // Make the cancel button switch back to the main dashboard
        cancelButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Clear input field or error state if desired
                        AddWatchlistState currentState = addWatchlistViewModel.getState();
                        currentState.setAddWatchlistError(null);
                        addWatchlistViewModel.setState(currentState);

                        // Switch back to the main logged-in view
                        viewManagerModel.setState("logged in");
                        viewManagerModel.firePropertyChanged();
                    }
                }
        );

        tickerInputField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateState() {
                AddWatchlistState currentState = addWatchlistViewModel.getState();
                currentState.setTicker(tickerInputField.getText());
                addWatchlistViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateState();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateState();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateState();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(tickerInfo);
        this.add(buttons);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Unused if action listeners are defined inline, but required by interface
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        AddWatchlistState state = (AddWatchlistState) evt.getNewValue();
        if (state.getAddWatchlistError() != null) {
            JOptionPane.showMessageDialog(this, state.getAddWatchlistError());
        }
    }
}