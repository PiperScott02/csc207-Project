package interface_adapter.stress_test;
import java.math.BigDecimal;

public class StressTestState {
    private String scenarioName = "Select a scenario";
    private BigDecimal currentValue = BigDecimal.ZERO;
    private BigDecimal stressedValue = BigDecimal.ZERO;
    private BigDecimal estimatedLoss = BigDecimal.ZERO;
    private BigDecimal impactPercentage = BigDecimal.ZERO;

    // Getters and setters
    public void setScenarioName(String name) { this.scenarioName = name; }
    public String getScenarioName() { return scenarioName; }
    public void setCurrentValue(BigDecimal val) { this.currentValue = val; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setStressedValue(BigDecimal val) { this.stressedValue = val; }
    public BigDecimal getStressedValue() { return stressedValue; }
    public void setEstimatedLoss(BigDecimal val) { this.estimatedLoss = val; }
    public BigDecimal getEstimatedLoss() { return estimatedLoss; }
    public void setImpactPercentage(BigDecimal val) { this.impactPercentage = val; }
    public BigDecimal getImpactPercentage() { return impactPercentage; }
}