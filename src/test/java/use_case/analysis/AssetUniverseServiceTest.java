package use_case.analysis;

import entity.Stock;
import entity.StockHolding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssetUniverseServiceTest {

    private Stock stockApple;
    private Stock stockGoogle;
    private Stock stockMicrosoft;

    private StockHolding holdingApple;
    private StockHolding holdingGoogle;
    private StockHolding holdingMicrosoft;

    @BeforeEach
    void setUp() {
        stockApple = new Stock();
        stockApple.setTickerSymbol("AAPL");
        stockGoogle = new Stock();
        stockGoogle.setTickerSymbol("GOOG");
        stockMicrosoft = new Stock();
        stockMicrosoft.setTickerSymbol("MSFT");

        // Create Holdings in non-alphabetical order: MSFT, AAPL, GOOG
        holdingMicrosoft = new StockHolding();
        holdingMicrosoft.setStock(stockMicrosoft);
        holdingApple = new StockHolding();
        holdingApple.setStock(stockApple);
        holdingGoogle = new StockHolding();
        holdingGoogle.setStock(stockGoogle);
    }

    @Test
    void testStocksAreExtractedAndSortedAlphabetically() {
        // Pass holdings out of alphabetical order
        List<StockHolding> holdings = List.of(holdingMicrosoft, holdingApple, holdingGoogle);

        AssetUniverseService universeService = new AssetUniverseService(holdings);

        List<Stock> resultStocks = universeService.getStocks();

        // Check size
        assertEquals(3, resultStocks.size());

        // Check alphabetical order: AAPL -> GOOGL -> MSFT
        assertEquals("AAPL", resultStocks.get(0).getTickerSymbol());
        assertEquals("GOOG", resultStocks.get(1).getTickerSymbol());
        assertEquals("MSFT", resultStocks.get(2).getTickerSymbol());
    }

    @Test
    void testIndexOfReturnsCorrectPosition() {
        List<StockHolding> holdings = List.of(holdingMicrosoft, holdingApple, holdingGoogle);
        AssetUniverseService universeService = new AssetUniverseService(holdings);

        // Based on alphabetical sorting:
        // AAPL  -> index 0
        // GOOGL -> index 1
        // MSFT  -> index 2
        assertEquals(0, universeService.indexOf("AAPL"));
        assertEquals(1, universeService.indexOf("GOOG"));
        assertEquals(2, universeService.indexOf("MSFT"));
    }

    @Test
    void testSize() {
        List<StockHolding> holdings = List.of(holdingMicrosoft, holdingApple);
        AssetUniverseService universeService = new AssetUniverseService(holdings);

        assertEquals(2, universeService.size());
    }

    @Test
    void testIndexOfThrowsExceptionForUnknownTicker() {
        List<StockHolding> holdings = List.of(holdingApple);
        AssetUniverseService universeService = new AssetUniverseService(holdings);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> universeService.indexOf("UNKNOWN_TICKER")
        );

        assertTrue(exception.getMessage().contains("Ticker not found in universe: UNKNOWN_TICKER"));
    }
}