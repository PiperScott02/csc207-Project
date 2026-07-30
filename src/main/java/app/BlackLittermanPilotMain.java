package app;

import data_access.FileStockDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.black_litterman.BlackLittermanPresenter;
import interface_adapter.black_litterman.BlackLittermanState;
import interface_adapter.black_litterman.BlackLittermanViewModel;
import use_case.analysis.BlackLittermanService;
import use_case.black_litterman.BlackLittermanDataAccessInterface;
import use_case.black_litterman.BlackLittermanInputBoundary;
import use_case.black_litterman.BlackLittermanInteractor;
import use_case.black_litterman.BlackLittermanOutputBoundary;
import use_case.stock.StockDataAccessInterface;
import view.BlackLittermanView;

import javax.swing.*;
import java.time.LocalDate;

public class BlackLittermanPilotMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Create top-level Swing Frame
            JFrame frame = new JFrame("Black-Litterman Model Pilot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 600);
            frame.setLocationRelativeTo(null);

            // 2. Instantiate Data Access, Services, and View Models
            final FileStockDataAccessObject fileStockDAO = new FileStockDataAccessObject();
            final BlackLittermanDataAccessInterface dataAccessObject = (fileStockDAO instanceof BlackLittermanDataAccessInterface)
                    ? (BlackLittermanDataAccessInterface) fileStockDAO
                    : null;

            final BlackLittermanService blackLittermanService = new BlackLittermanService();
            final ViewManagerModel viewManagerModel = new ViewManagerModel();
            final BlackLittermanViewModel blackLittermanViewModel = new BlackLittermanViewModel();

            // 3. Create a test user with holdings
            final User testUser = new CommonUser("testUser", "password");
            final RiskProfile mockRiskProfile = new RiskProfile();
            mockRiskProfile.setRiskLevel(RiskLevel.MODERATE);
            testUser.setRiskProfile(mockRiskProfile);

            final Portfolio portfolio = testUser.getPortfolio();
            final StockDataAccessInterface stockDataAccessObject = fileStockDAO;
            final StockHoldingFactory stockHoldingFactory = new StockHoldingFactory(stockDataAccessObject);

            // Set the test user into the state initially
            BlackLittermanState initialState = blackLittermanViewModel.getState();
            initialState.setUser(testUser);
            blackLittermanViewModel.setState(initialState);

            // Add sample holdings to populate top tickers
            for (String ticker : new String[]{"GOOG", "AAPL", "MSFT", "AMZN", "NVDA"}) {
                try {
                    StockHolding holding = stockHoldingFactory.create(ticker);
                    Stock stock = holding.getStock();
                    if (stock != null && !stock.getDatesSorted().isEmpty()) {
                        LocalDate pastDate = stock.getDatesSorted().get(Math.min(5, stock.getDatesSorted().size() - 1));
                        holding.makeTransaction(stock, 100.0, pastDate, TransactionType.BUY);
                        portfolio.addHolding(holding);
                    }
                } catch (Exception ignored) {
                }
            }

            // 4. Construct Presenter, Interactor, and Controller
            final BlackLittermanOutputBoundary blackLittermanPresenter =
                    new BlackLittermanPresenter(viewManagerModel, blackLittermanViewModel);

            final BlackLittermanInputBoundary blackLittermanInteractor =
                    new BlackLittermanInteractor(dataAccessObject, blackLittermanService, blackLittermanPresenter);

            final BlackLittermanController blackLittermanController =
                    new BlackLittermanController(blackLittermanInteractor);

            // 5. Instantiate View with ViewManagerModel and wire Controller
            final BlackLittermanView blackLittermanView = new BlackLittermanView(
                    viewManagerModel,
                    blackLittermanViewModel,
                    blackLittermanController
            );

            // 6. Automatically trigger initial market data load so labels populate right away
            blackLittermanController.loadMarketData(testUser);

            // 7. Display Frame
            frame.add(blackLittermanView);
            frame.setVisible(true);
        });
    }
}