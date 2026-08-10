package interface_adapter.stress_test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StressTestState {
    private String scenarioName = "";
    private BigDecimal currentValue = BigDecimal.ZERO;
    private BigDecimal stressedValue = BigDecimal.ZERO;
    private BigDecimal estimatedLoss = BigDecimal.ZERO;
    private BigDecimal impactPercentage = BigDecimal.ZERO;

    private List<String> tickers = new ArrayList<>();
    private List<String> sectors = new ArrayList<>();
    private List<BigDecimal> currentPrices = new ArrayList<>();
    private List<BigDecimal> stressedPrices = new ArrayList<>();
    private List<BigDecimal> currentValues = new ArrayList<>();
    private List<BigDecimal> estimatedLosses = new ArrayList<>();

    public StressTestState(StressTestState copy) {
        this.scenarioName = copy.scenarioName;
        this.currentValue = copy.currentValue;
        this.stressedValue = copy.stressedValue;
        this.estimatedLoss = copy.estimatedLoss;
        this.impactPercentage = copy.impactPercentage;
        this.tickers = new ArrayList<>(copy.tickers);
        this.sectors = new ArrayList<>(copy.sectors);
        this.currentPrices = new ArrayList<>(copy.currentPrices);
        this.stressedPrices = new ArrayList<>(copy.stressedPrices);
        this.currentValues = new ArrayList<>(copy.currentValues);
        this.estimatedLosses = new ArrayList<>(copy.estimatedLosses);
    }

    public StressTestState() {}

    // Getters and Setters
    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }

    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }

    public BigDecimal getStressedValue() { return stressedValue; }
    public void setStressedValue(BigDecimal stressedValue) { this.stressedValue = stressedValue; }

    public BigDecimal getEstimatedLoss() { return estimatedLoss; }
    public void setEstimatedLoss(BigDecimal estimatedLoss) { this.estimatedLoss = estimatedLoss; }

    public BigDecimal getImpactPercentage() { return impactPercentage; }
    public void setImpactPercentage(BigDecimal impactPercentage) { this.impactPercentage = impactPercentage; }

    public List<String> getTickers() { return tickers; }
    public void setTickers(List<String> tickers) { this.tickers = tickers; }

    public List<String> getSectors() { return sectors; }
    public void setSectors(List<String> sectors) { this.sectors = sectors; }

    public List<BigDecimal> getCurrentPrices() { return currentPrices; }
    public void setCurrentPrices(List<BigDecimal> currentPrices) { this.currentPrices = currentPrices; }

    public List<BigDecimal> getStressedPrices() { return stressedPrices; }
    public void setStressedPrices(List<BigDecimal> stressedPrices) { this.stressedPrices = stressedPrices; }

    public List<BigDecimal> getCurrentValues() { return currentValues; }
    public void setCurrentValues(List<BigDecimal> currentValues) { this.currentValues = currentValues; }

    public List<BigDecimal> getEstimatedLosses() { return estimatedLosses; }
    public void setEstimatedLosses(List<BigDecimal> estimatedLosses) { this.estimatedLosses = estimatedLosses; }
}