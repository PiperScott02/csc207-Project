package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import interface_adapter.ViewManagerModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockPresenter;
import interface_adapter.stock.StockViewModel;
import interface_adapter.stock.StockView;
import use_case.stock.StockInteractor;
import use_case.stock.StockDataAccessInterface;
import view.ViewManager;
import data_access.FileStockDataAccessObject;

/**
 * Starts a test environment specifically for the Stock display UI.
 */
public final class StockPilotMain {

    private StockPilotMain() {
        // Prevent this utility class from being instantiated.
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        final JFrame application = new JFrame("Stock Viewer Pilot");
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
         * Instantiate the ViewModel for the stock page.
         */
        final StockViewModel stockViewModel = new StockViewModel();

        /*
         * Instantiate the Data Access Object.
         * NOTE: Replace this with whatever your actual Stock DAO class is named!
         */
        final StockDataAccessInterface stockDataAccessObject =
                new FileStockDataAccessObject();

        /*
         * Manually wire the Clean Architecture layers together.
         * (If you create a StockUseCaseFactory later, you can replace this block with it).
         */
        final StockPresenter stockPresenter = new StockPresenter(viewManagerModel, stockViewModel);
        final StockInteractor stockInteractor = new StockInteractor(stockDataAccessObject, stockPresenter);
        final StockController stockController = new StockController(stockInteractor);

        /*
         * Instantiate the View, passing in the ViewModel it needs to listen to.
         */
        final StockView stockView = new StockView(stockViewModel);

        /*
         * Add the View to the CardLayout stack.
         */
        views.add(stockView, stockView.viewName);

        /*
         * Set the initial view to be the Stock View.
         */
        viewManagerModel.setState(stockView.viewName);
        viewManagerModel.firePropertyChanged();

        /*
         * Trigger the controller to fetch and display data for a test stock!
         * Without this, the view will just show empty labels.
         */
        stockController.execute("AAPL");
        application.pack();
        application.setSize(600, 400);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}