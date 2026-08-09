package use_case.add_watchlist;

import entity.WatchlistStockItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddWatchlistOutputDataTest {

    @Test
    void testGettersReturnCorrectValues() {
        List<WatchlistStockItem> watchlist = new ArrayList<>();
        watchlist.add(new WatchlistStockItem("AAPL", "Apple Inc.", new BigDecimal("150.00"), new BigDecimal("2.50")));

        AddWatchlistOutputData outputData = new AddWatchlistOutputData("AAPL", watchlist, false);

        assertEquals("AAPL", outputData.getTicker());
        assertEquals(watchlist, outputData.getWatchlist());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void testFailureState() {
        AddWatchlistOutputData outputData = new AddWatchlistOutputData(null, null, true);

        assertNull(outputData.getTicker());
        assertNull(outputData.getWatchlist());
        assertTrue(outputData.isUseCaseFailed());
    }
}