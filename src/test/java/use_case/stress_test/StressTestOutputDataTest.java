package use_case.stress_test;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StressTestOutputDataTest {

    @Test
    void testOutputDataGettersAndValues() {
        String scenarioName = "Mild Correction";
        BigDecimal totalCurrent = new BigDecimal("1000.00");
        BigDecimal totalStressed = new BigDecimal("900.00");
        BigDecimal estimatedLoss = new BigDecimal("-100.00");
        BigDecimal impact = new BigDecimal("-10.00");

        List<String> tickers = Arrays.asList("AAPL");
        List<String> sectors = Arrays.asList("Technology");
        List<BigDecimal> currentPrices = Arrays.asList(new BigDecimal("100.00"));
        List<BigDecimal> stressedPrices = Arrays.asList(new BigDecimal("90.00"));
        List<BigDecimal> currentValues = Arrays.asList(new BigDecimal("1000.00"));
        List<BigDecimal> estimatedLosses = Arrays.asList(new BigDecimal("-100.00"));

        StressTestOutputData outputData = new StressTestOutputData(
                scenarioName, totalCurrent, totalStressed, estimatedLoss, impact,
                tickers, sectors, currentPrices, stressedPrices, currentValues, estimatedLosses
        );

        assertEquals(scenarioName, outputData.getScenarioName());
        assertEquals(totalCurrent, outputData.getTotalCurrentValue());
        assertEquals(totalStressed, outputData.getTotalStressedValue());
        assertEquals(estimatedLoss, outputData.getEstimatedLoss());
        assertEquals(impact, outputData.getPortfolioImpactPercentage());
        assertEquals(tickers, outputData.getTickers());
        assertEquals(sectors, outputData.getSectors());
        assertEquals(currentPrices, outputData.getCurrentPrices());
        assertEquals(stressedPrices, outputData.getStressedPrices());
        assertEquals(currentValues, outputData.getCurrentValues());
        assertEquals(estimatedLosses, outputData.getEstimatedLosses());
    }
}