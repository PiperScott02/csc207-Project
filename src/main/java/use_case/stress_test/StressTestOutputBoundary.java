package use_case.stress_test;

public interface StressTestOutputBoundary {
    void prepareSuccessView(StressTestOutputData outputData);
    void prepareFailView(String errorMessage);
}