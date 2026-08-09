/**package use_case.black_litterman;

import entity.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.analysis.BlackLittermanService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

class BlackLittermanInteractorTest {

    private BlackLittermanDataAccessInterface dao;
    private BlackLittermanService service;
    private BlackLittermanOutputBoundary presenter;

    @BeforeEach
    void setUp() {
        dao = mock(BlackLittermanDataAccessInterface.class);
        service = mock(BlackLittermanService.class);
        presenter = mock(BlackLittermanOutputBoundary.class);
    }

    @Test
    void testExecuteNullUserFails() {
        BlackLittermanInputData inputData = new BlackLittermanInputData(null, null, null);
        BlackLittermanInteractor interactor = new BlackLittermanInteractor(dao, service, presenter);

        interactor.execute(inputData);

        verify(presenter).prepareFailView("User or portfolio cannot be null.");
        verify(presenter, never()).prepareSuccessView(any());
    }

    @Test
    void testExecuteNullPortfolioFails() {
        User user = new CommonUser("Piper", "123");
        user.setPortfolio(null);

        BlackLittermanInputData inputData = new BlackLittermanInputData(user, null, null);
        BlackLittermanInteractor interactor = new BlackLittermanInteractor(dao, service, presenter);

        interactor.execute(inputData);

        verify(presenter).prepareFailView("User or portfolio cannot be null.");
        verify(presenter, never()).prepareSuccessView(any());
    }

    @Test
    void testExecuteSuccessWithUserViews() {
        User user = new CommonUser("Piper", "123");
        Portfolio portfolio = new Portfolio();

        Stock aapl = new Stock();
        aapl.setTickerSymbol("AAPL");
        StockHolding aaplHolding = new StockHolding();
        aaplHolding.setStock(aapl);

        Stock msft = new Stock();
        msft.setTickerSymbol("MSFT");
        StockHolding msftHolding = new StockHolding();
        msftHolding.setStock(msft);

        portfolio.addHolding(aaplHolding);
        portfolio.addHolding(msftHolding);
        user.setPortfolio(portfolio);

        // 1. Mock Market Weight Caps
        Map<String, Double> weightCaps = new HashMap<>();
        weightCaps.put("AAPL", 0.60);
        weightCaps.put("MSFT", 0.40);
        when(service.computeMarketWeightCaps(any())).thenReturn(weightCaps);

        // 2. Mock Implied Equilibrium Matrix (2x1 matrix)
        double[][] piData = {{0.0003}, {0.0002}}; // Daily returns
        RealMatrix piMatrix = new Array2DRowRealMatrix(piData);
        when(service.impliedEquilibriumExpectedReturn(any())).thenReturn(piMatrix);

        // 3. Mock Adjusted Returns
        Map<String, Double> userViews = new HashMap<>();
        userViews.put("AAPL", 0.10);
        Map<String, String> confidenceLevels = new HashMap<>();
        confidenceLevels.put("AAPL", "HIGH");

        Map<String, Double> adjustedReturns = new HashMap<>();
        adjustedReturns.put("AAPL", 0.095);
        adjustedReturns.put("MSFT", 0.052);

        when(service.computeAdjustedReturns(any(), eq(userViews), eq(confidenceLevels)))
                .thenReturn(adjustedReturns);

        BlackLittermanInputData inputData = new BlackLittermanInputData(user, userViews, confidenceLevels);
        BlackLittermanInteractor interactor = new BlackLittermanInteractor(dao, service, presenter);

        interactor.execute(inputData);

        // Verify custom views set on portfolio
        assertTrue(portfolio.hasCustomViews());
        assertEquals(adjustedReturns, portfolio.getCustomViews());

        // Verify presenter output data
        verify(presenter).prepareSuccessView(argThat(outputData ->
                outputData.getUser().equals(user) &&
                        outputData.getTopTickers().contains("AAPL") &&
                        outputData.getTopTickers().contains("MSFT") &&
                        outputData.getAdjustedReturns().get("AAPL") == 0.095 &&
                        !outputData.isUseCaseFailed()
        ));
        verify(presenter, never()).prepareFailView(any());
    }

    @Test
    void testExecuteSuccessNoUserViews() {
        User user = new CommonUser("Piper", "123");
        Portfolio portfolio = new Portfolio();

        Stock aapl = new Stock();
        aapl.setTickerSymbol("AAPL");
        StockHolding aaplHolding = new StockHolding();
        aaplHolding.setStock(aapl);

        portfolio.addHolding(aaplHolding);
        user.setPortfolio(portfolio);

        Map<String, Double> weightCaps = Collections.singletonMap("AAPL", 1.0);
        when(service.computeMarketWeightCaps(any())).thenReturn(weightCaps);

        double[][] piData = {{0.0004}};
        RealMatrix piMatrix = new Array2DRowRealMatrix(piData);
        when(service.impliedEquilibriumExpectedReturn(any())).thenReturn(piMatrix);

        BlackLittermanInputData inputData = new BlackLittermanInputData(user, null, null);
        BlackLittermanInteractor interactor = new BlackLittermanInteractor(dao, service, presenter);

        interactor.execute(inputData);

        // Verify custom views cleared on portfolio
        assertFalse(portfolio.hasCustomViews());
        assertNull(portfolio.getCustomViews());

        verify(presenter).prepareSuccessView(argThat(outputData ->
                outputData.getUser().equals(user) &&
                        outputData.getAdjustedReturns().isEmpty() &&
                        !outputData.isUseCaseFailed()
        ));
        verify(presenter, never()).prepareFailView(any());
    }

    @Test
    void testExecuteExceptionTriggersCatchBlock() {
        User user = new CommonUser("Piper", "123");
        user.setPortfolio(new Portfolio());

        // Force service to throw exception
        when(service.computeMarketWeightCaps(any())).thenThrow(new RuntimeException("Matrix calculation error"));

        BlackLittermanInputData inputData = new BlackLittermanInputData(user, null, null);
        BlackLittermanInteractor interactor = new BlackLittermanInteractor(dao, service, presenter);

        interactor.execute(inputData);

        verify(presenter).prepareFailView(startsWith("Failed to process Black-Litterman model: Matrix calculation error"));
        verify(presenter, never()).prepareSuccessView(any());
    }
}**/