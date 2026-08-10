package interface_adapter.stress_test;

import use_case.stress_test.StressTestOutputBoundary;
import use_case.stress_test.StressTestOutputData;

public class StressTestPresenter implements StressTestOutputBoundary {
    private final StressTestViewModel viewModel;

    public StressTestPresenter(StressTestViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(StressTestOutputData outputData) {
        StressTestState state = viewModel.getState();
        state.setScenarioName(outputData.getScenarioName());
        state.setCurrentValue(outputData.getTotalCurrentValue());
        state.setStressedValue(outputData.getTotalStressedValue());
        state.setEstimatedLoss(outputData.getEstimatedLoss());
        state.setImpactPercentage(outputData.getPortfolioImpactPercentage());

        // Pass the real lists containing actual sectors and calculations
        state.setTickers(outputData.getTickers());
        state.setSectors(outputData.getSectors());
        state.setCurrentPrices(outputData.getCurrentPrices());
        state.setStressedPrices(outputData.getStressedPrices());
        state.setCurrentValues(outputData.getCurrentValues());
        state.setEstimatedLosses(outputData.getEstimatedLosses());

        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
    }
}