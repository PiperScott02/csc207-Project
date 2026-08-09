package use_case.add_watchlist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddWatchlistInputDataTest {

    @Test
    void testGetTickerReturnsCorrectValue() {
        AddWatchlistInputData inputData = new AddWatchlistInputData("AAPL");

        assertNotNull(inputData.getTicker());
        assertEquals("AAPL", inputData.getTicker());
    }

    @Test
    void testNullTicker() {
        AddWatchlistInputData inputData = new AddWatchlistInputData(null);

        assertNull(inputData.getTicker());
    }
}