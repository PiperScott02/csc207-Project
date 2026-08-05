package use_case.watchlist;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WatchlistOutputDataTest {

    @Test
    void testOutputDataAndNestedItemGetters() {
        WatchlistOutputData.WatchlistStockOutputItem item =
                new WatchlistOutputData.WatchlistStockOutputItem("AAPL", "Apple Inc.", "150.00", "2.50");

        assertEquals("AAPL", item.getTicker());
        assertEquals("Apple Inc.", item.getCompanyName());
        assertEquals("150.00", item.getClose());
        assertEquals("2.50", item.getDailyPriceChange());

        WatchlistOutputData outputData = new WatchlistOutputData(List.of(item), false);

        assertEquals(1, outputData.getItems().size());
        assertEquals("AAPL", outputData.getItems().get(0).getTicker());
        assertFalse(outputData.isUseCaseFailed());
    }
}