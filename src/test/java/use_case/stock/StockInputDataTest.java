package use_case.stock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockInputDataTest {

    @Test
    void testGetTickerSymbolReturnsCorrectTicker() {
        StockInputData inputData = new StockInputData("AAPL");

        assertNotNull(inputData.getTickerSymbol());
        assertEquals("AAPL", inputData.getTickerSymbol());
    }

    @Test
    void testEmptyTickerSymbol() {
        StockInputData inputData = new StockInputData("");

        assertNotNull(inputData.getTickerSymbol());
        assertEquals("", inputData.getTickerSymbol());
    }

    @Test
    void testNullTickerSymbol() {
        StockInputData inputData = new StockInputData(null);

        assertNull(inputData.getTickerSymbol());
    }
}