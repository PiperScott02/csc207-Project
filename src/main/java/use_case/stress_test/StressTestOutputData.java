package use_case.stress_test;
import java.math.BigDecimal;

public class StressTestOutputData {
    private final String scenarioName;
    private final BigDecimal totalCurrentValue;
    private final BigDecimal totalStressedValue;
    private final BigDecimal estimatedLoss;
    private final BigDecimal portfolioImpactPercentage;

    public StressTestOutputData(String scenarioName, BigDecimal totalCurrentValue,
                                BigDecimal totalStressedValue, BigDecimal estimatedLoss,
                                BigDecimal portfolioImpactPercentage) {
        this.scenarioName = scenarioName;
        this.totalCurrentValue = totalCurrentValue;
        this.totalStressedValue = totalStressedValue;
        this.estimatedLoss = estimatedLoss;
        this.portfolioImpactPercentage = portfolioImpactPercentage;
    }

    public String getScenarioName() { return scenarioName; }
    public BigDecimal getTotalCurrentValue() { return totalCurrentValue; }
    public BigDecimal getTotalStressedValue() { return totalStressedValue; }
    public BigDecimal getEstimatedLoss() { return estimatedLoss; }
    public BigDecimal getPortfolioImpactPercentage() { return portfolioImpactPercentage; }
}