package interface_adapter.stock;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockView extends JPanel implements PropertyChangeListener {
    public final String viewName = "stock view";

    private final StockViewModel stockViewModel;

    private final JLabel tickerLabel = new JLabel("Ticker: ");
    private final JLabel companyNameLabel = new JLabel("Company: ");
    private final JLabel closePriceLabel = new JLabel("Close Price: ");
    private final JLabel dailyPriceChangeLabel = new JLabel("Daily Price Change: ");
    private final JLabel betaLabel = new JLabel("Beta: ");
    private final JLabel alphaLabel = new JLabel("Alpha: ");
    private final JLabel sharpeRatioLabel = new JLabel("Sharpe Ratio: ");

    public StockView(StockViewModel stockViewModel) {
        this.stockViewModel = stockViewModel;
        this.stockViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Stock Analytics");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));

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
        StockState state = (StockState) evt.getNewValue();
        tickerLabel.setText("Ticker: " + state.getTicker());
        companyNameLabel.setText("Company: " + state.getCompanyName());
        closePriceLabel.setText("Close Price: " + state.getClose());
        dailyPriceChangeLabel.setText("Daily Price Change: " + state.getDailyPriceChange());
        betaLabel.setText("Beta: " + state.getBeta());
        alphaLabel.setText("Alpha: " + state.getAlpha());
        sharpeRatioLabel.setText("Sharpe Ratio: " + state.getSharpeRatio());
    }
}