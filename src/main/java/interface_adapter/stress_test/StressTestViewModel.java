package interface_adapter.stress_test;
import interface_adapter.ViewModel;

public class StressTestViewModel extends ViewModel<StressTestState> {
    public static final String TITLE_LABEL = "Stress Test";

    public StressTestViewModel() {
        super("stress test");
        setState(new StressTestState());
    }
}