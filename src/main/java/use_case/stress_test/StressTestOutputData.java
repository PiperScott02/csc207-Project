package use_case.stress_test;

import java.math.BigDecimal;
import java.util.List;

public class StressTestOutputData {
    private final String scenarioName;
    private final BigDecimal totalCurrentValue;
    private final BigDecimal totalStressedValue;
    private final BigDecimal estimatedLoss;
    private final BigDecimal portfolioImpactPercentage;

    private final List<String> tickers;
    private final List<String> sectors;
    private final List<BigDecimal> currentPrices;
    private final List<BigDecimal> stressedPrices;
    private final List<BigDecimal> currentValues;
    private final List<BigDecimal> estimatedLosses;

    public StressTestOutputData(String scenarioName, BigDecimal totalCurrentValue,
                                BigDecimal totalStressedValue, BigDecimal estimatedLoss,
                                BigDecimal portfolioImpactPercentage,
                                List<String> tickers, List<String> sectors,
                                List<BigDecimal> currentPrices, List<BigDecimal> stressedPrices,
                                List<BigDecimal> currentValues, List<BigDecimal> estimatedLosses) {
        this.scenarioName = scenarioName;
        this.totalCurrentValue = totalCurrentValue;
        this.totalStressedValue = totalStressedValue;
        this.estimatedLoss = estimatedLoss;
        this.portfolioImpactPercentage = portfolioImpactPercentage;
        this.tickers = tickers;
        this.sectors = sectors;
        this.currentPrices = currentPrices;
        this.stressedPrices = stressedPrices;
        this.currentValues = currentValues;
        this.estimatedLosses = estimatedLosses;
    }

    public String getScenarioName() { return scenarioName; }
    public BigDecimal getTotalCurrentValue() { return totalCurrentValue; }
    public BigDecimal getTotalStressedValue() { return totalStressedValue; }
    public BigDecimal getEstimatedLoss() { return estimatedLoss; }
    public BigDecimal getPortfolioImpactPercentage() { return portfolioImpactPercentage; }

    public List<String> getTickers() { return tickers; }
    public List<String> getSectors() { return sectors; }
    public List<BigDecimal> getCurrentPrices() { return currentPrices; }
    public List<BigDecimal> getStressedPrices() { return stressedPrices; }
    public List<BigDecimal> getCurrentValues() { return currentValues; }
    public List<BigDecimal> getEstimatedLosses() { return estimatedLosses; }
}