package app;

import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.stress_test.StressTestController;
import interface_adapter.stress_test.StressTestViewModel;
import interface_adapter.stress_test.StressTestPresenter;
import use_case.stress_test.StressTestInputBoundary;
import use_case.stress_test.StressTestInteractor;
import use_case.stress_test.StressTestOutputBoundary;

/**
 * Factory for creating the Stress Test use case.
 */
public final class StressTestUseCaseFactory {

    private StressTestUseCaseFactory() {
        // Prevent instantiation.
    }

    public static StressTestController create(
            StressTestViewModel stressTestViewModel,
            LoggedInViewModel loggedInViewModel) {

        final StressTestOutputBoundary stressTestOutputBoundary =
                new StressTestPresenter(stressTestViewModel);

        final StressTestInputBoundary stressTestInteractor =
                new StressTestInteractor(
                        stressTestOutputBoundary,
                        loggedInViewModel
                );

        return new StressTestController(stressTestInteractor);
    }
}