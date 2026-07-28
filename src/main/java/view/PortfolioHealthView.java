package view;

import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import interface_adapter.portfolio_health.PortfolioHealthState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PortfolioHealthView extends JPanel implements PropertyChangeListener {
    public final String viewName = "portfolioHealth view";

    private final PortfolioHealthViewModel portfolioHealthViewModel;

    private final JLabel portfolioHealthScoreLabel = new JLabel("Portfolio Health Score: ");
    private final JLabel riskPreferenceLabel = new JLabel("Risk Preference: ");
    private final JLabel betaLabel = new JLabel("Beta: ");
    private final JLabel alphaLabel = new JLabel("Alpha: ");
    private final JLabel sharpeRatioLabel = new JLabel("Sharpe Ratio: ");

    // Advice labels
    private final JLabel sharpeAdviceLabel = new JLabel("Sharpe Advice: ");
    private final JLabel riskAlignmentAdviceLabel = new JLabel("Risk Alignment Advice: ");
    private final JLabel diversificationAdviceLabel = new JLabel("Diversification Advice: ");
    private final JLabel newsAdviceLabel = new JLabel("News Advice: ");

    public PortfolioHealthView(PortfolioHealthViewModel portfolioHealthViewModel) {
        this.portfolioHealthViewModel = portfolioHealthViewModel;
        this.portfolioHealthViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Portfolio Health Analytics");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));

        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(portfolioHealthScoreLabel);
        add(Box.createVerticalStrut(8));
        add(riskPreferenceLabel);
        add(Box.createVerticalStrut(8));
        add(betaLabel);
        add(Box.createVerticalStrut(8));
        add(alphaLabel);
        add(Box.createVerticalStrut(8));
        add(sharpeRatioLabel);
        add(Box.createVerticalStrut(12));

        // Add advice components
        add(sharpeAdviceLabel);
        add(Box.createVerticalStrut(6));
        add(riskAlignmentAdviceLabel);
        add(Box.createVerticalStrut(6));
        add(diversificationAdviceLabel);
        add(Box.createVerticalStrut(6));
        add(newsAdviceLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PortfolioHealthState state = (PortfolioHealthState) evt.getNewValue();
        portfolioHealthScoreLabel.setText("Portfolio Health Score: " + state.getPortfolioHealthScore() + "/100");
        riskPreferenceLabel.setText("Risk Preference: " + state.getRiskPreference());
        betaLabel.setText("Beta: " + state.getBeta());
        alphaLabel.setText("Alpha: " + state.getAlpha());
        sharpeRatioLabel.setText("Sharpe Ratio: " + state.getSharpeRatio());

        sharpeAdviceLabel.setText("Sharpe Advice: " + state.getSharpeAdvice());
        riskAlignmentAdviceLabel.setText("Risk Alignment Advice: " + state.getRiskAlignmentAdvice());
        diversificationAdviceLabel.setText("Diversification Advice: " + state.getDiversificationAdvice());
        newsAdviceLabel.setText("News Advice: " + state.getNewsAdvice());
    }
}