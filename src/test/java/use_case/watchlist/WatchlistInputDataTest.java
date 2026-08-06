package use_case.watchlist;

import entity.CommonUser;
import entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchlistInputDataTest {

    @Test
    void testGetUserReturnsCorrectUser() {
        User user = new CommonUser("Piper", "123");
        WatchlistInputData inputData = new WatchlistInputData(user);

        assertNotNull(inputData.getUser());
        assertEquals("Piper", inputData.getUser().getName());
        assertEquals(user, inputData.getUser());
    }

    @Test
    void testNullUser() {
        WatchlistInputData inputData = new WatchlistInputData(null);

        assertNull(inputData.getUser());
    }
}