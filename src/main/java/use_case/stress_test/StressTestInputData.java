package use_case.stress_test;
import entity.StressScenario;

public class StressTestInputData {
    private final StressScenario scenario;
    public StressTestInputData(StressScenario scenario) { this.scenario = scenario; }
    public StressScenario getScenario() { return scenario; }
}