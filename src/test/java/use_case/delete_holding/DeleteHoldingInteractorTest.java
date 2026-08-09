package use_case.delete_holding;

import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteHoldingInteractorTest {

    @Test
    void successTest() {
        // Arrange
        DeleteHoldingInputData inputData = new DeleteHoldingInputData("AAPL");

        DeleteHoldingUserDataAccessInterface dataAccessObject = mock(DeleteHoldingUserDataAccessInterface.class);
        DeleteHoldingOutputBoundary outputBoundary = mock(DeleteHoldingOutputBoundary.class);

        User user = mock(User.class);
        Portfolio portfolio = mock(Portfolio.class);
        List<StockHolding> holdings = new ArrayList<>();

        // Mock a holding containing AAPL
        StockHolding mockHolding = mock(StockHolding.class);
        Stock mockStock = mock(Stock.class);
        when(mockStock.getTickerSymbol()).thenReturn("AAPL");
        when(mockHolding.getStock()).thenReturn(mockStock);
        holdings.add(mockHolding);

        when(dataAccessObject.getCurrentUser()).thenReturn("Piper");
        when(dataAccessObject.get("Piper")).thenReturn(user);
        when(user.getPortfolio()).thenReturn(portfolio);
        when(portfolio.getHoldings()).thenReturn(holdings);

        DeleteHoldingInputBoundary interactor = new DeleteHoldingInteractor(dataAccessObject, outputBoundary);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(dataAccessObject, times(1)).save(user);
        verify(outputBoundary, times(1)).prepareSuccessView(any(DeleteHoldingOutputData.class));

        // Verify the holding was removed from the list
        assertTrue(holdings.isEmpty());
    }

    @Test
    void userOrPortfolioNotFoundTest() {
        // Arrange
        DeleteHoldingInputData inputData = new DeleteHoldingInputData("AAPL");

        DeleteHoldingUserDataAccessInterface dataAccessObject = mock(DeleteHoldingUserDataAccessInterface.class);
        DeleteHoldingOutputBoundary outputBoundary = mock(DeleteHoldingOutputBoundary.class);

        when(dataAccessObject.getCurrentUser()).thenReturn("Piper");
        when(dataAccessObject.get("Piper")).thenReturn(null); // User not found

        DeleteHoldingInputBoundary interactor = new DeleteHoldingInteractor(dataAccessObject, outputBoundary);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(dataAccessObject, never()).save(any(User.class));
        verify(outputBoundary, times(1)).prepareFailView("Could not find current user or portfolio.");
    }
}