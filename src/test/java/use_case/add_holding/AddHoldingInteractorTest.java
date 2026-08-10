package use_case.add_holding;

import data_access.FileUserDataAccessObject;
import entity.*;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import org.junit.jupiter.api.Test;
import use_case.StockDailyDataAccessInterface;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddHoldingInteractorTest {

    @Test
    void successTest() {
        // Arrange
        AddHoldingInputData inputData = new AddHoldingInputData("AAPL", 10.0, LocalDate.now().minusDays(1));

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddHoldingOutputBoundary successPresenter = mock(AddHoldingOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        Portfolio portfolio = new Portfolio();
        user.setPortfolio(portfolio);

        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        Stock mockStock = new Stock();
        try {
            when(stockDataAccessObject.createStockAndHistory("AAPL")).thenReturn(mockStock);
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown during mocking setup");
        }

        AddHoldingInputBoundary interactor = new AddHoldingInteractor(
                stockDataAccessObject, successPresenter, loggedInViewModel, userDataAccessObject
        );

        // Act
        interactor.execute(inputData);

        // Assert
        verify(userDataAccessObject, times(1)).save(user);
        verify(successPresenter, times(1)).prepareSuccessView(any(AddHoldingOutputData.class));
    }

    @Test
    void emptyTickerTest() {
        AddHoldingInputData inputData = new AddHoldingInputData("", 10.0, LocalDate.now().minusDays(1));

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddHoldingOutputBoundary presenter = mock(AddHoldingOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        AddHoldingInputBoundary interactor = new AddHoldingInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("Ticker symbol cannot be empty.");
    }

    @Test
    void invalidSharesTest() {
        AddHoldingInputData inputData = new AddHoldingInputData("AAPL", 0.0, LocalDate.now().minusDays(1));

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddHoldingOutputBoundary presenter = mock(AddHoldingOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        AddHoldingInputBoundary interactor = new AddHoldingInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("Number of shares must be greater than zero.");
    }

    @Test
    void futureDateTest() {
        AddHoldingInputData inputData = new AddHoldingInputData("AAPL", 10.0, LocalDate.now().plusDays(1));

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddHoldingOutputBoundary presenter = mock(AddHoldingOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        AddHoldingInputBoundary interactor = new AddHoldingInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("Purchase date cannot be in the future.");
    }

    @Test
    void stockNotFoundTest() {
        AddHoldingInputData inputData = new AddHoldingInputData("UNKNOWN", 10.0, LocalDate.now().minusDays(1));

        StockDailyDataAccessInterface stockDataAccessObject = mock(StockDailyDataAccessInterface.class);
        AddHoldingOutputBoundary presenter = mock(AddHoldingOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        LoggedInState loggedInState = mock(LoggedInState.class);
        FileUserDataAccessObject userDataAccessObject = mock(FileUserDataAccessObject.class);

        User user = new CommonUser("Hana", "123");
        when(loggedInViewModel.getState()).thenReturn(loggedInState);
        when(loggedInState.getUser()).thenReturn(user);

        try {
            when(stockDataAccessObject.createStockAndHistory("UNKNOWN")).thenReturn(null);
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown during mocking setup");
        }

        AddHoldingInputBoundary interactor = new AddHoldingInteractor(
                stockDataAccessObject, presenter, loggedInViewModel, userDataAccessObject
        );

        interactor.execute(inputData);

        verify(presenter, times(1)).prepareFailView("Stock ticker not found.");
    }
}