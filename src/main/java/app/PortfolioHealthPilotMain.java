package app;

import java.awt.CardLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.AlphaVantageNewsDataAccessObject;
import data_access.FileStockDataAccessObject;
import entity.CommonUser;
import entity.Portfolio;
import entity.RiskLevel;
import entity.RiskProfile;
import entity.Stock;
import entity.StockHolding;
import entity.TransactionType;
import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthPresenter;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import use_case.analysis.StockFinancialService;
import use_case.news.NewsDataAccessInterface;
import use_case.portfolio_health.PortfolioHealthDataAccessInterface;
import use_case.portfolio_health.PortfolioHealthInteractor;
import use_case.stock.StockDataAccessInterface;
import view.PortfolioHealthView;
import view.ViewManager;

/**
 * Starts a test environment specifically for the Portfolio Health display UI.
 */
public final class PortfolioHealthPilotMain {

    private PortfolioHealthPilotMain() {
        // Prevent this utility class from being instantiated.
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        final JFrame application = new JFrame("Portfolio Health Pilot");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*
         * CardLayout lets the application hold several pages while
         * displaying only one page at a time.
         */
        final CardLayout cardLayout = new CardLayout();
        final JPanel views = new JPanel(cardLayout);
        application.add(views);

        /*
         * ViewManagerModel stores the name of the page that should
         * currently be displayed.
         */
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        /*
         * Instantiate the ViewModels.
         */
        final PortfolioHealthViewModel portfolioHealthViewModel = new PortfolioHealthViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel(); // Needed for back button

        /*
         * Instantiate the Data Access Objects.
         */
        final StockDataAccessInterface stockDataAccessObject = new FileStockDataAccessObject();
        final NewsDataAccessInterface newsDataAccessObject =
                new AlphaVantageNewsDataAccessObject("API_KEY_PLACEHOLDER");

        /*
         * Manually wire the Clean Architecture layers together.
         */
        final PortfolioHealthPresenter portfolioHealthPresenter = new PortfolioHealthPresenter(
                viewManagerModel, portfolioHealthViewModel
        );

// Pass newsDataAccessObject directly (no casting needed!)
        final PortfolioHealthInteractor portfolioHealthInteractor = new PortfolioHealthInteractor(
                stockDataAccessObject,
                newsDataAccessObject,
                portfolioHealthPresenter
        );
        final PortfolioHealthController portfolioHealthController = new PortfolioHealthController(
                portfolioHealthInteractor
        );

        /*
         * Instantiate the View using the factory method or direct instantiation with LoggedInViewModel.
         */
        final PortfolioHealthView portfolioHealthView = PortfolioHealthUseCaseFactory.create(
                viewManagerModel,
                portfolioHealthViewModel,
                loggedInViewModel,
                portfolioHealthController
        );

        /*
         * Add the View to the CardLayout stack.
         */
        views.add(portfolioHealthView, portfolioHealthView.viewName);

        /*
         * Set the initial view to be the Portfolio Health View.
         */
        viewManagerModel.setState(portfolioHealthView.viewName);
        viewManagerModel.firePropertyChanged();

        /*
         * Create a test user with a Moderate risk profile using CommonUser.
         */
        final User testUser = new CommonUser("testUser", "password");

        // Give the user a mock risk profile so the risk alignment score triggers cleanly
        final RiskProfile mockRiskProfile = new RiskProfile();
        mockRiskProfile.setRiskLevel(RiskLevel.MODERATE);
        testUser.setRiskProfile(mockRiskProfile);

        final Portfolio portfolio = testUser.getPortfolio();

        /*
         * Use the factory to build and populate multiple holdings for a robust test portfolio.
         */
        final StockHoldingFactory stockHoldingFactory = new StockHoldingFactory(stockDataAccessObject);

        // 1. Holding: Google (GOOG)
        final StockHolding googleHolding = stockHoldingFactory.create("GOOG");
        final Stock googleStock = googleHolding.getStock();
        if (googleStock != null && !googleStock.getDatesSorted().isEmpty()) {
            LocalDate pastDate = googleStock.getDatesSorted().get(Math.min(5, googleStock.getDatesSorted().size() - 1));
            googleHolding.makeTransaction(googleStock, 150.0, pastDate, TransactionType.BUY);
            portfolio.addHolding(googleHolding);
        }

        // 2. Holding: Apple (AAPL)
        final StockHolding appleHolding = stockHoldingFactory.create("AAPL");
        final Stock appleStock = appleHolding.getStock();
        if (appleStock != null && !appleStock.getDatesSorted().isEmpty()) {
            LocalDate pastDate = appleStock.getDatesSorted().get(Math.min(5, appleStock.getDatesSorted().size() - 1));
            appleHolding.makeTransaction(appleStock, 200.0, pastDate, TransactionType.BUY);
            portfolio.addHolding(appleHolding);
        }

        // 3. Holding: Microsoft (MSFT)
        final StockHolding microsoftHolding = stockHoldingFactory.create("MSFT");
        final Stock microsoftStock = microsoftHolding.getStock();
        if (microsoftStock != null && !microsoftStock.getDatesSorted().isEmpty()) {
            LocalDate pastDate = microsoftStock.getDatesSorted().get(Math.min(5,
                    microsoftStock.getDatesSorted().size() - 1));
            microsoftHolding.makeTransaction(microsoftStock, 100.0, pastDate, TransactionType.BUY);
            portfolio.addHolding(microsoftHolding);
        }

        /*
         * Trigger the controller to calculate and display portfolio health metrics with the multi-stock portfolio!
         */
        portfolioHealthController.execute(testUser);

        application.pack();
        application.setSize(600, 400);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}