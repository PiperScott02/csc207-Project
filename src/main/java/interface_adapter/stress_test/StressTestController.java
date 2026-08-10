package interface_adapter.stress_test;

import entity.StressScenario;
import use_case.stress_test.StressTestInputBoundary;
import use_case.stress_test.StressTestInputData;

public class StressTestController {
    private final StressTestInputBoundary interactor;

    public StressTestController(StressTestInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(StressScenario scenario) {
        StressTestInputData inputData = new StressTestInputData(scenario);
        interactor.execute(inputData);
    }
}