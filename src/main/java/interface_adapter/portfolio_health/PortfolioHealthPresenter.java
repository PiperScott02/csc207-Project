package interface_adapter.portfolio_health;

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
        currentState.setAlpha(outputData.getAlpha());
        currentState.setSharpeRatio(outputData.getSharpeRatio());
        currentState.setErrorMessage(null); // Clear any previous errors

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
    }
}