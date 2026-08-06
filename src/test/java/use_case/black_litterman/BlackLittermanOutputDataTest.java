package use_case.black_litterman;

import entity.CommonUser;
import entity.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BlackLittermanOutputDataTest {

    @Test
    void testGettersReturnCorrectValuesOnSuccess() {
        User user = new CommonUser("Piper", "123");
        List<String> topTickers = Arrays.asList("AAPL", "MSFT", "GOOGL", "AMZN", "NVDA");

        Map<String, Double> marketReturns = new HashMap<>();
        marketReturns.put("AAPL", 0.07);
        marketReturns.put("MSFT", 0.06);

        Map<String, Double> adjustedReturns = new HashMap<>();
        adjustedReturns.put("AAPL", 0.085);
        adjustedReturns.put("MSFT", 0.065);

        BlackLittermanOutputData outputData = new BlackLittermanOutputData(
                user, topTickers, marketReturns, adjustedReturns, false
        );

        assertNotNull(outputData.getUser());
        assertEquals("Piper", outputData.getUser().getName());
        assertEquals(user, outputData.getUser());

        assertNotNull(outputData.getTopTickers());
        assertEquals(5, outputData.getTopTickers().size());
        assertEquals("AAPL", outputData.getTopTickers().get(0));

        assertNotNull(outputData.getMarketReturns());
        assertEquals(0.07, outputData.getMarketReturns().get("AAPL"));

        assertNotNull(outputData.getAdjustedReturns());
        assertEquals(0.085, outputData.getAdjustedReturns().get("AAPL"));

        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void testUseCaseFailedFlagWhenTrue() {
        User user = new CommonUser("Piper", "123");

        BlackLittermanOutputData outputData = new BlackLittermanOutputData(
                user, null, null, null, true
        );

        assertTrue(outputData.isUseCaseFailed());
    }

    @Test
    void testNullValues() {
        BlackLittermanOutputData outputData = new BlackLittermanOutputData(
                null, null, null, null, false
        );

        assertNull(outputData.getUser());
        assertNull(outputData.getTopTickers());
        assertNull(outputData.getMarketReturns());
        assertNull(outputData.getAdjustedReturns());
        assertFalse(outputData.isUseCaseFailed());
    }
}