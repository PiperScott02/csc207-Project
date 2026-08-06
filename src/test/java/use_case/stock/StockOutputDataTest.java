package use_case.stock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockOutputDataTest {

    @Test
    void testGettersReturnCorrectValuesOnSuccess() {
        StockOutputData outputData = new StockOutputData(
                "AAPL",
                "Apple Inc.",
                "150.00",
                "2.50",
                "1.20",
                "0.05",
                "1.85",
                false
        );

        assertEquals("AAPL", outputData.getTickerSymbol());
        assertEquals("Apple Inc.", outputData.getCompanyName());
        assertEquals("150.00", outputData.getClose());
        assertEquals("2.50", outputData.getDailyPriceChange());
        assertEquals("1.20", outputData.getBeta());
        assertEquals("0.05", outputData.getAnnualizedAlpha());
        assertEquals("1.85", outputData.getAnnualizedSharpeRatio());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void testUseCaseFailedFlagWhenTrue() {
        StockOutputData outputData = new StockOutputData(
                "AAPL",
                "Apple Inc.",
                "0.00",
                "0.00",
                "N/A",
                "N/A",
                "N/A",
                true
        );

        assertTrue(outputData.isUseCaseFailed());
    }

    @Test
    void testNullValues() {
        StockOutputData outputData = new StockOutputData(
                null, null, null, null, null, null, null, false
        );

        assertNull(outputData.getTickerSymbol());
        assertNull(outputData.getCompanyName());
        assertNull(outputData.getClose());
        assertNull(outputData.getDailyPriceChange());
        assertNull(outputData.getBeta());
        assertNull(outputData.getAnnualizedAlpha());
        assertNull(outputData.getAnnualizedSharpeRatio());
        assertFalse(outputData.isUseCaseFailed());
    }
}