package interface_adapter.stock;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockView extends JPanel implements PropertyChangeListener {
    public final String viewName = "stock view";

    private final StockViewModel stockViewModel;

    private final JLabel tickerLabel = new JLabel("Ticker: ");
    private final JLabel companyNameLabel = new JLabel("Company: ");
    private final JLabel closePriceLabel = new JLabel("Close Price: ");
    private final JLabel betaLabel = new JLabel("Beta: ");
    private final JLabel alphaLabel = new JLabel("Alpha: ");
    private final JLabel sharpeRatioLabel = new JLabel("Sharpe Ratio: ");


    public StockView(StockViewModel stockViewModel) {
        this.stockViewModel = stockViewModel;
        this.stockViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(tickerLabel);
        add(companyNameLabel);
        add(closePriceLabel);
        add(betaLabel);
        add(alphaLabel);
        add(sharpeRatioLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        StockState state = (StockState) evt.getNewValue();
        tickerLabel.setText("Ticker: " + state.getTicker());
        companyNameLabel.setText("Company: " + state.getCompanyName());
        closePriceLabel.setText("Close Price: " + state.getClose());
        betaLabel.setText("Beta: " + state.getBeta());
        alphaLabel.setText("Alpha: " + state.getAlpha());
        sharpeRatioLabel.setText("Sharpe Ratio: " + state.getSharpeRatio());
    }
}