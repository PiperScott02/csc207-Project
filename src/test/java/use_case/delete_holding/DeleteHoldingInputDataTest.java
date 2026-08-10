package use_case.delete_holding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteHoldingInputDataTest {

    @Test
    void testGetTickerReturnsCorrectValue() {
        DeleteHoldingInputData inputData = new DeleteHoldingInputData("AAPL");

        assertNotNull(inputData.getTicker());
        assertEquals("AAPL", inputData.getTicker());
    }

    @Test
    void testNullTicker() {
        DeleteHoldingInputData inputData = new DeleteHoldingInputData(null);

        assertNull(inputData.getTicker());
    }
}