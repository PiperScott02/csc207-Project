package use_case.stress_test;

import entity.StressScenario;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class StressTestInputDataTest {

    @Test
    void testInputDataInitializationAndGetters() {
        StressScenario scenario = new StressScenario(
                "Market Crash",
                "1 Month",
                "Severe market downturn",
                new BigDecimal("-0.20")
        );
        StressTestInputData inputData = new StressTestInputData(scenario);

        assertEquals(scenario, inputData.getScenario());
        assertEquals("Market Crash", inputData.getScenario().getName());
        assertEquals("1 Month", inputData.getScenario().getTimeline());
        assertEquals("Severe market downturn", inputData.getScenario().getDescription());
        assertEquals(new BigDecimal("-0.20"), inputData.getScenario().getShockPercentage());
    }
}