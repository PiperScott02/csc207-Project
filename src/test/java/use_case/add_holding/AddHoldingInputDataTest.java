package use_case.add_holding;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AddHoldingInputDataTest {

    @Test
    void testGettersReturnCorrectValues() {
        LocalDate date = LocalDate.of(2026, 1, 15);
        AddHoldingInputData inputData = new AddHoldingInputData("AAPL", 10.5, date);

        assertEquals("AAPL", inputData.getTicker());
        assertEquals(10.5, inputData.getShares());
        assertEquals(date, inputData.getPurchaseDate());
    }
}