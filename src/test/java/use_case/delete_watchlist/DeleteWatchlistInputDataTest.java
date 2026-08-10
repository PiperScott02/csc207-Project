package use_case.delete_watchlist;

import entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DeleteWatchlistInputDataTest {

    @Test
    void testGetters() {
        User mockUser = mock(User.class);
        DeleteWatchlistInputData inputData = new DeleteWatchlistInputData("AAPL", mockUser);

        assertEquals("AAPL", inputData.getTicker());
        assertEquals(mockUser, inputData.getUser());
    }

    @Test
    void testNullValues() {
        DeleteWatchlistInputData inputData = new DeleteWatchlistInputData(null, null);

        assertNull(inputData.getTicker());
        assertNull(inputData.getUser());
    }
}