package use_case.delete_watchlist;

import org.junit.jupiter.api.Test;
import use_case.watchlist.WatchlistOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteWatchlistOutputDataTest {

    @Test
    void testGettersReturnCorrectValues() {
        List<WatchlistOutputData.WatchlistStockOutputItem> items = new ArrayList<>();
        WatchlistOutputData.WatchlistStockOutputItem stockItem =
                new WatchlistOutputData.WatchlistStockOutputItem("AAPL", "Apple Inc.", "150.00", "2.50");
        items.add(stockItem);

        WatchlistOutputData outputData = new WatchlistOutputData(items, false);

        // Assert WatchlistOutputData fields
        assertEquals(items, outputData.getItems());
        assertFalse(outputData.isUseCaseFailed());

        // Assert WatchlistStockOutputItem nested class fields
        assertEquals("AAPL", stockItem.getTicker());
        assertEquals("Apple Inc.", stockItem.getCompanyName());
        assertEquals("150.00", stockItem.getClose());
        assertEquals("2.50", stockItem.getDailyPriceChange());
    }

    @Test
    void testFailureState() {
        WatchlistOutputData outputData = new WatchlistOutputData(null, true);

        assertNull(outputData.getItems());
        assertTrue(outputData.isUseCaseFailed());
    }
}