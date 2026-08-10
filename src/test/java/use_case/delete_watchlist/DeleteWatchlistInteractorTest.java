package use_case.delete_watchlist;

import entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DeleteWatchlistInteractorTest {

    @Test
    void testGettersReturnCorrectValues() {
        User user = mock(User.class);
        DeleteWatchlistInputData inputData = new DeleteWatchlistInputData("AAPL", user);

        assertEquals("AAPL", inputData.getTicker());
        assertEquals(user, inputData.getUser());
    }

    @Test
    void testNullValues() {
        DeleteWatchlistInputData inputData = new DeleteWatchlistInputData(null, null);

        assertNull(inputData.getTicker());
        assertNull(inputData.getUser());
    }
}