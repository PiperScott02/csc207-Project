package use_case.news;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NewsInputDataTest {

    @Test
    void testGetTickerReturnsInputTicker() {
        final NewsInputData inputData = new NewsInputData("AAPL");

        assertEquals("AAPL", inputData.getTicker());
    }

    @Test
    void testNullTickerIsStored() {
        final NewsInputData inputData = new NewsInputData(null);

        assertNull(inputData.getTicker());
    }
}
