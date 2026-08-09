package use_case.delete_holding;

import entity.Portfolio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteHoldingOutputDataTest {

    @Test
    void testGettersReturnCorrectValues() {
        Portfolio portfolio = new Portfolio();
        String message = "Holding deleted successfully.";
        DeleteHoldingOutputData outputData = new DeleteHoldingOutputData(portfolio, message);

        assertEquals(portfolio, outputData.getPortfolio());
        assertEquals(message, outputData.getMessage());
    }

    @Test
    void testNullValues() {
        DeleteHoldingOutputData outputData = new DeleteHoldingOutputData(null, null);

        assertNull(outputData.getPortfolio());
        assertNull(outputData.getMessage());
    }
}