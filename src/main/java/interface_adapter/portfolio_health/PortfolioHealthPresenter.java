package interface_adapter.portfolio_health;

import javax.swing.JOptionPane;
import interface_adapter.ViewManagerModel;
import use_case.portfolio_health.PortfolioHealthOutputBoundary;
import use_case.portfolio_health.PortfolioHealthOutputData;

public class PortfolioHealthPresenter implements PortfolioHealthOutputBoundary {

    private final PortfolioHealthViewModel portfolioHealthViewModel;
    private final ViewManagerModel viewManagerModel;

    public PortfolioHealthPresenter(ViewManagerModel viewManagerModel,
                                    PortfolioHealthViewModel portfolioHealthViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.portfolioHealthViewModel = portfolioHealthViewModel;
    }

    @Override
    public void prepareSuccessView(PortfolioHealthOutputData outputData) {
        PortfolioHealthState currentState = portfolioHealthViewModel.getState();

        currentState.setRiskPreference(outputData.getRiskPreference());
        currentState.setPortfolioHealthScore(outputData.getPortfolioHealthScore());
        currentState.setBeta(outputData.getBeta());
        currentState.setAlpha(outputData.getAnnualizedAlpha());
        currentState.setSharpeRatio(outputData.getAnnualizedSharpeRatio());
        currentState.setSharpeAdvice(outputData.getSharpeAdvice());
        currentState.setRiskAlignmentAdvice(outputData.getRiskAlignmentAdvice());
        currentState.setDiversificationAdvice(outputData.getDiversificationAdvice());
        currentState.setNewsAdvice(outputData.getNewsAdvice());
        currentState.setErrorMessage(null);

        portfolioHealthViewModel.setState(currentState);
        portfolioHealthViewModel.firePropertyChanged();

        viewManagerModel.setState(portfolioHealthViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        PortfolioHealthState currentState = portfolioHealthViewModel.getState();
        currentState.setErrorMessage(errorMessage);

        portfolioHealthViewModel.setState(currentState);
        portfolioHealthViewModel.firePropertyChanged();

        JOptionPane.showMessageDialog(null, errorMessage, "Portfolio Health Error", JOptionPane.ERROR_MESSAGE);
    }
}