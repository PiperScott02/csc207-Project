package use_case.portfolio_health;

import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import use_case.analysis.PortfolioFinancialService;
import use_case.stock.StockDataAccessInterface;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioHealthInteractorTest {

    private StockDataAccessInterface stockDAO;
    private PortfolioHealthOutputBoundary presenter;
    private MockedStatic<PortfolioFinancialService> mockedFinancialService;

    @BeforeEach
    void setUp() {
        stockDAO = mock(StockDataAccessInterface.class);
        presenter = mock(PortfolioHealthOutputBoundary.class);
        mockedFinancialService = mockStatic(PortfolioFinancialService.class);
    }

    @AfterEach
    void tearDown() {
        mockedFinancialService.close();
    }

    @Test
    void testExecuteEmptyHoldingsFails() {
        User user = new CommonUser("Piper", "123");
        Portfolio portfolio = new Portfolio();
        user.setPortfolio(portfolio);

        PortfolioHealthInputData inputData = new PortfolioHealthInputData(user);
        PortfolioHealthInteractor interactor = new PortfolioHealthInteractor(stockDAO, presenter);

        interactor.execute(inputData);

        verify(presenter).prepareFailView("Your portfolio has no holdings. Please add a holding first.");
        verify(presenter, never()).prepareSuccessView(any());
    }

    @Test
    void testExecuteNullStockInHoldingFails() {
        User user = new CommonUser("Piper", "123");
        Portfolio portfolio = new Portfolio();
        portfolio.addHolding(null); // Null holding element
        user.setPortfolio(portfolio);

        PortfolioHealthInputData inputData = new PortfolioHealthInputData(user);
        PortfolioHealthInteractor interactor = new PortfolioHealthInteractor(stockDAO, presenter);

        interactor.execute(inputData);

        verify(presenter).prepareFailView("Your portfolio has no holdings. Please add a holding first.");
        verify(presenter, never()).prepareSuccessView(any());
    }

    @Test
    void testExecuteSuccess() {
        // Setup Mocks
        Stock spy = new Stock();
        spy.setTickerSymbol("SPY");
        when(stockDAO.get("SPY")).thenReturn(spy);

        User user = new CommonUser("Piper", "123");
        RiskProfile riskProfile = new RiskProfile();
        riskProfile.setRiskLevel(RiskLevel.MODERATE);
        user.setRiskProfile(riskProfile);

        Stock aapl = new Stock();
        aapl.setTickerSymbol("AAPL");

        StockHolding holding = new StockHolding();
        holding.setStock(aapl);

        Portfolio portfolio = mock(Portfolio.class);
        when(portfolio.getHoldings()).thenReturn(java.util.Collections.singletonList(holding));
        when(portfolio.getTrueBeta()).thenReturn(1.10);
        when(portfolio.getAlpha()).thenReturn(0.05);
        when(portfolio.getSharpeRatio()).thenReturn(1.50);
        user.setPortfolio(portfolio);

        // Mock static method calls on PortfolioFinancialService
        mockedFinancialService.when(() -> PortfolioFinancialService.calculateAndAssignMetrics(any(), any()))
                .thenAnswer(invocation -> null);
        mockedFinancialService.when(() -> PortfolioFinancialService.calculateCdr(any()))
                .thenReturn(0.85);

        PortfolioHealthInputData inputData = new PortfolioHealthInputData(user);
        PortfolioHealthInteractor interactor = new PortfolioHealthInteractor(stockDAO, presenter);

        interactor.execute(inputData);

        verify(presenter).prepareSuccessView(argThat(outputData ->
                "MODERATE".equals(outputData.getRiskPreference()) &&
                        !outputData.isUseCaseFailed() &&
                        outputData.getPortfolioHealthScore() != null
        ));
        verify(presenter, never()).prepareFailView(any());
    }

    @Test
    void testExecuteExceptionTriggersCatchBlock() {
        PortfolioHealthInteractor interactor = new PortfolioHealthInteractor(stockDAO, presenter);

        // Passing null input causes NullPointerException caught inside execute()
        interactor.execute(null);

        verify(presenter).prepareFailView(startsWith("Failed to calculate portfolio health:"));
        verify(presenter, never()).prepareSuccessView(any());
    }
}