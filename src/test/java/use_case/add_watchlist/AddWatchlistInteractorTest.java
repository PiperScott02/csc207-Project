package use_case.add_watchlist;

import data_access.FileUserDataAccessObject;
import entity.*;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import org.junit.jupiter.api.Test;
import use_case.StockDailyDataAccessInterface;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddWatchlistInteractorTest {

    @Test
    void successTest() {
        // Arrange
        AddWatchlistInputData inputData = new AddWatchlistInputData("AAPL");

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddWatchlistOutputBoundary successPresenter = mock(AddWatchlistOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        Portfolio portfolio = new Portfolio();
        user.setPortfolio(portfolio);

        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        Stock mockStock = mock(Stock.class);
        when(mockStock.getTickerSymbol()).thenReturn("AAPL");
        when(mockStock.getCompanyName()).thenReturn("Apple Inc.");
        when(mockStock.getClose()).thenReturn(new BigDecimal("150.00"));
        when(mockStock.getDailyPriceChange()).thenReturn(new BigDecimal("2.50"));

        try {
            when(stockDataAccessObject.createStockAndHistory("AAPL")).thenReturn(mockStock);
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown during mocking setup");
        }

        AddWatchlistInputBoundary interactor = new AddWatchlistInteractor(
                stockDataAccessObject, successPresenter, loggedInViewModel, userDataAccessObject
        );

        // Act
        interactor.execute(inputData);

        // Assert
        verify(userDataAccessObject, times(1)).save(user);
        verify(successPresenter, times(1)).prepareSuccessView(any(AddWatchlistOutputData.class));
        assertEquals(1, portfolio.getWatchlist().size());
        assertEquals("AAPL", portfolio.getWatchlist().get(0).ticker());
    }

    @Test
    void emptyTickerTest() {
        AddWatchlistInputData inputData = new AddWatchlistInputData("");

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddWatchlistOutputBoundary presenter = mock(AddWatchlistOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        AddWatchlistInputBoundary interactor = new AddWatchlistInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("Ticker symbol cannot be empty.");
    }

    @Test
    void noUserSessionTest() {
        AddWatchlistInputData inputData = new AddWatchlistInputData("AAPL");

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddWatchlistOutputBoundary presenter = mock(AddWatchlistOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        when(loggedInViewModel.getState()).thenReturn(null);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        AddWatchlistInputBoundary interactor = new AddWatchlistInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("No active user session found.");
    }

    @Test
    void alreadyExistsTest() {
        AddWatchlistInputData inputData = new AddWatchlistInputData("AAPL");

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddWatchlistOutputBoundary presenter = mock(AddWatchlistOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        Portfolio portfolio = new Portfolio();
        portfolio.addWatchlist(new WatchlistStockItem("AAPL", "Apple Inc.", new BigDecimal("150"), new BigDecimal("1")));
        user.setPortfolio(portfolio);

        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        AddWatchlistInputBoundary interactor = new AddWatchlistInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("Stock is already in your watchlist.");
    }

    @Test
    void stockNotFoundTest() {
        AddWatchlistInputData inputData = new AddWatchlistInputData("UNKNOWN");

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddWatchlistOutputBoundary presenter = mock(AddWatchlistOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        user.setPortfolio(new Portfolio());

        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        try {
            when(stockDataAccessObject.createStockAndHistory("UNKNOWN")).thenReturn(null);
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown during mocking setup");
        }

        AddWatchlistInputBoundary interactor = new AddWatchlistInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("Stock ticker not found.");
    }
}