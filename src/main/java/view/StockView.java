package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.stock.StockState;
import interface_adapter.stock.StockViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockView extends JPanel implements PropertyChangeListener {

    private final StockViewModel stockViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    private final JLabel tickerLabel = new JLabel("Ticker: ");
    private final JLabel companyNameLabel = new JLabel("Company: ");
    private final JLabel closePriceLabel = new JLabel("Close Price: ");
    private final JLabel dailyPriceChangeLabel = new JLabel("Daily Price Change: ");
    private final JLabel betaLabel = new JLabel("Beta: ");
    private final JLabel alphaLabel = new JLabel("Alpha: ");
    private final JLabel sharpeRatioLabel = new JLabel("Sharpe Ratio: ");

    public StockView(StockViewModel stockViewModel,
                     ViewManagerModel viewManagerModel,
                     LoggedInViewModel loggedInViewModel) {
        this.stockViewModel = stockViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;

        this.stockViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Back Button setup
        JButton backButton = new JButton("← Back to Profile");
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addActionListener(e -> {
            this.viewManagerModel.setState(this.loggedInViewModel.getViewName());
            this.viewManagerModel.firePropertyChanged();
        });

        // Title setup
        JLabel titleLabel = new JLabel("Stock Analytics");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Align components to the left side
        tickerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        companyNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        closePriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dailyPriceChangeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        betaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        alphaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sharpeRatioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Build the panel
        add(backButton);
        add(Box.createVerticalStrut(15));
        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(tickerLabel);
        add(Box.createVerticalStrut(8));
        add(companyNameLabel);
        add(Box.createVerticalStrut(8));
        add(closePriceLabel);
        add(Box.createVerticalStrut(8));
        add(dailyPriceChangeLabel);
        add(Box.createVerticalStrut(8));
        add(betaLabel);
        add(Box.createVerticalStrut(8));
        add(alphaLabel);
        add(Box.createVerticalStrut(8));
        add(sharpeRatioLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof StockState state) {
            tickerLabel.setText("Ticker: " + state.getTicker());
            companyNameLabel.setText("Company: " + state.getCompanyName());
            closePriceLabel.setText("Close Price: " + state.getClose());
            dailyPriceChangeLabel.setText("Daily Price Change: " + state.getDailyPriceChange());
            betaLabel.setText("Beta: " + state.getBeta());
            alphaLabel.setText("Alpha: " + state.getAlpha());
            sharpeRatioLabel.setText("Sharpe Ratio: " + state.getSharpeRatio());
        }
    }

    public String getViewName() {
        return stockViewModel.getViewName();
    }
}