package view;

import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import interface_adapter.portfolio_health.PortfolioHealthState;


import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PortfolioHealthView extends JLabel implements PropertyChangeListener {
    public final String viewName = "portfolioHealth view";

    private final PortfolioHealthViewModel portfolioHealthViewModel;

    private final JLabel portfolioHealthScoreLabel = new JLabel("Portfolio Health Score: ");
    private final JLabel riskPreferenceLabel = new JLabel("Risk Preference: ");
    private final JLabel betaLabel = new JLabel("Beta: ");
    private final JLabel alphaLabel = new JLabel("Alpha: ");
    private final JLabel sharpeRatioLabel = new JLabel("Sharpe Ratio: ");
;


    public PortfolioHealthView(PortfolioHealthViewModel portfolioHealthViewModel) {
        this.portfolioHealthViewModel = portfolioHealthViewModel;
        this.portfolioHealthViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(portfolioHealthScoreLabel);
        add(riskPreferenceLabel);
        add(betaLabel);
        add(alphaLabel);
        add(sharpeRatioLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PortfolioHealthState state = (PortfolioHealthState) evt.getNewValue();
        portfolioHealthScoreLabel.setText("Portfolio Health Score: " + state.getPortfolioHealthScore() + "/100");
        riskPreferenceLabel.setText("Risk Preference: " + state.getRiskPreference());
        betaLabel.setText("Beta: " + state.getBeta());
        alphaLabel.setText("Alpha: " + state.getAlpha());
        sharpeRatioLabel.setText("Sharpe Ratio: " + state.getSharpeRatio());
    }
}
