package use_case.black_litterman;

import entity.CommonUser;
import entity.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BlackLittermanInputDataTest {

    @Test
    void testGettersReturnCorrectValues() {
        User user = new CommonUser("Piper", "123");

        Map<String, Double> userViews = new HashMap<>();
        userViews.put("AAPL", 0.08);
        userViews.put("MSFT", 0.05);

        Map<String, String> confidenceLevels = new HashMap<>();
        confidenceLevels.put("AAPL", "HIGH");
        confidenceLevels.put("MSFT", "MEDIUM");

        BlackLittermanInputData inputData = new BlackLittermanInputData(user, userViews, confidenceLevels);

        assertNotNull(inputData.getUser());
        assertEquals("Piper", inputData.getUser().getName());
        assertEquals(user, inputData.getUser());

        assertNotNull(inputData.getUserViews());
        assertEquals(2, inputData.getUserViews().size());
        assertEquals(0.08, inputData.getUserViews().get("AAPL"));

        assertNotNull(inputData.getConfidenceLevels());
        assertEquals(2, inputData.getConfidenceLevels().size());
        assertEquals("HIGH", inputData.getConfidenceLevels().get("AAPL"));
    }

    @Test
    void testEmptyMaps() {
        User user = new CommonUser("Piper", "123");
        Map<String, Double> emptyViews = new HashMap<>();
        Map<String, String> emptyConfidences = new HashMap<>();

        BlackLittermanInputData inputData = new BlackLittermanInputData(user, emptyViews, emptyConfidences);

        assertNotNull(inputData.getUserViews());
        assertTrue(inputData.getUserViews().isEmpty());

        assertNotNull(inputData.getConfidenceLevels());
        assertTrue(inputData.getConfidenceLevels().isEmpty());
    }

    @Test
    void testNullValues() {
        BlackLittermanInputData inputData = new BlackLittermanInputData(null, null, null);

        assertNull(inputData.getUser());
        assertNull(inputData.getUserViews());
        assertNull(inputData.getConfidenceLevels());
    }
}