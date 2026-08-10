package use_case.stress_test;

import entity.*;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import org.junit.jupiter.api.Test;
import use_case.TickerSearchDataAccessInterface;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class StressTestInteractorTest {

    @Test
    void testNoActiveUserSessionFailure() {
        StressTestOutputBoundary outputBoundary = mock(StressTestOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        TickerSearchDataAccessInterface tickerDataAccess = mock(TickerSearchDataAccessInterface.class);

        when(loggedInViewModel.getState()).thenReturn(null);

        StressTestInteractor interactor = new StressTestInteractor(outputBoundary, loggedInViewModel, tickerDataAccess);
        StressTestInputData inputData = new StressTestInputData(
                new StressScenario("Crash", "1 Month", "Market downturn", new BigDecimal("-0.10"))
        );

        interactor.execute(inputData);

        verify(outputBoundary).prepareFailView("No active user session found.");
        verify(outputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testEmptyPortfolioFailure() {
        StressTestOutputBoundary outputBoundary = mock(StressTestOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        TickerSearchDataAccessInterface tickerDataAccess = mock(TickerSearchDataAccessInterface.class);

        // Portfolio with empty holdings list
        Portfolio portfolio = new Portfolio(new ArrayList<>());

        CommonUser user = mock(CommonUser.class);
        when(user.getPortfolio()).thenReturn(portfolio);

        LoggedInState state = new LoggedInState();
        state.setUser(user);
        when(loggedInViewModel.getState()).thenReturn(state);

        StressTestInteractor interactor = new StressTestInteractor(outputBoundary, loggedInViewModel, tickerDataAccess);
        StressTestInputData inputData = new StressTestInputData(
                new StressScenario("Crash", "1 Month", "Market downturn", new BigDecimal("-0.10"))
        );

        interactor.execute(inputData);

        verify(outputBoundary).prepareFailView("Stress test is not possible: portfolio is empty.");
        verify(outputBoundary, never()).prepareSuccessView(any());
    }

    @Test
    void testSuccessfulStressTestExecution() {
        StressTestOutputBoundary outputBoundary = mock(StressTestOutputBoundary.class);
        LoggedInViewModel loggedInViewModel = mock(LoggedInViewModel.class);
        TickerSearchDataAccessInterface tickerDataAccess = mock(TickerSearchDataAccessInterface.class);

        // 1. Create stock and holding
        Stock stock = new Stock();
        stock.setTickerSymbol("AAPL");
        stock.setCompanyName("Apple Inc");
        stock.setCountry("USA");
        stock.setIndustry("Technology");
        stock.setClose(new BigDecimal("150.00"));

        StockHolding holding = new StockHolding();
        holding.setStock(stock);
        holding.makeTransaction(stock, 10.0, TransactionType.BUY);

        List<StockHolding> holdings = new ArrayList<>();
        holdings.add(holding);

        // 2. Pass holdings list directly into the Portfolio constructor
        Portfolio portfolio = new Portfolio(holdings);

        // 3. Mock User to return our populated portfolio
        CommonUser user = mock(CommonUser.class);
        when(user.getPortfolio()).thenReturn(portfolio);

        LoggedInState state = new LoggedInState();
        state.setUser(user);
        when(loggedInViewModel.getState()).thenReturn(state);

        when(tickerDataAccess.createBasicStock("AAPL")).thenReturn(stock);

        StressTestInteractor interactor = new StressTestInteractor(outputBoundary, loggedInViewModel, tickerDataAccess);
        StressTestInputData inputData = new StressTestInputData(
                new StressScenario("Crash", "1 Month", "Market downturn", new BigDecimal("-0.10"))
        );

        interactor.execute(inputData);

        verify(outputBoundary).prepareSuccessView(any(StressTestOutputData.class));
        verify(outputBoundary, never()).prepareFailView(anyString());
    }
}