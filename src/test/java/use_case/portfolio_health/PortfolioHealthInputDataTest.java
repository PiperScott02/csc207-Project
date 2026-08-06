package use_case.portfolio_health;

import entity.CommonUser;
import entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioHealthInputDataTest {

    @Test
    void testGetUserReturnsCorrectUser() {
        User user = new CommonUser("Scott", "password123");
        PortfolioHealthInputData inputData = new PortfolioHealthInputData(user);

        assertNotNull(inputData.getUser());
        assertEquals("Scott", inputData.getUser().getName());
        assertEquals(user, inputData.getUser());
    }

    @Test
    void testNullUser() {
        PortfolioHealthInputData inputData = new PortfolioHealthInputData(null);

        assertNull(inputData.getUser());
    }
}