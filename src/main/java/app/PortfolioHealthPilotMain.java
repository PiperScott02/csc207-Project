package app;

import java.awt.CardLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.FileStockDataAccessObject;
import entity.CommonUser;
import entity.Portfolio;
import entity.Stock;
import entity.StockHolding;
import entity.TransactionType;
import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthPresenter;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import use_case.analysis.StockFinancialService;
import use_case.portfolio_health.PortfolioHealthDataAccessInterface;
import use_case.portfolio_health.PortfolioHealthInteractor;
import use_case.stock.StockDataAccessInterface;
import app.StockHoldingFactory;
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
         * Instantiate the ViewModel for the portfolio health page.
         */
        final PortfolioHealthViewModel portfolioHealthViewModel = new PortfolioHealthViewModel();

        /*
         * Instantiate the Data Access Objects.
         */
        final StockDataAccessInterface stockDataAccessObject = new FileStockDataAccessObject();
        final PortfolioHealthDataAccessInterface portfolioHealthDataAccessObject = null;

        /*
         * Manually wire the Clean Architecture layers together.
         */
        final PortfolioHealthPresenter portfolioHealthPresenter = new PortfolioHealthPresenter(
                viewManagerModel, portfolioHealthViewModel
        );
        final PortfolioHealthInteractor portfolioHealthInteractor = new PortfolioHealthInteractor(
                portfolioHealthDataAccessObject, stockDataAccessObject, portfolioHealthPresenter
        );
        final PortfolioHealthController portfolioHealthController = new PortfolioHealthController(
                portfolioHealthInteractor
        );

        /*
         * Instantiate the View, passing in the ViewModel it needs to listen to.
         */
        final PortfolioHealthView portfolioHealthView = new PortfolioHealthView(portfolioHealthViewModel);

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
         * Create a test user using CommonUser.
         */
        final User testUser = new CommonUser("testUser", "password");
        final Portfolio portfolio = testUser.getPortfolio();

        /*
         * Create a stock holding for AAPL using the factory, make a 200 share purchase, and add it to the portfolio.
         */
        final StockHoldingFactory stockHoldingFactory = new StockHoldingFactory(stockDataAccessObject);
        final StockHolding testHolding = stockHoldingFactory.create("GOOG");

        final Stock testStock = testHolding.getStock();

        List<LocalDate> stockDates = testStock.getDatesSorted();

        LocalDate pastDate = stockDates.get(5);


        testHolding.makeTransaction(testStock, 200.0, pastDate, TransactionType.BUY);

        portfolio.addHolding(testHolding);

        /*
         * Trigger the controller to calculate and display portfolio health metrics with the populated portfolio!
         */
        portfolioHealthController.execute(testUser);

        application.pack();
        application.setSize(600, 400);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}