package use_case.add_holding;

import entity.StockHolding;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddHoldingOutputDataTest {

    @Test
    void testGettersReturnCorrectValues() {
        List<StockHolding> holdings = new ArrayList<>();
        AddHoldingOutputData outputData = new AddHoldingOutputData("AAPL", 10.5, holdings, false);

        assertEquals("AAPL", outputData.getTicker());
        assertEquals(10.5, outputData.getShares());
        assertEquals(holdings, outputData.getHoldings());
        assertFalse(outputData.isUseCaseFailed());
    }
}