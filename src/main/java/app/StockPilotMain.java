package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockPresenter;
import interface_adapter.stock.StockViewModel;
import view.StockView;
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
         * Instantiate ViewModels.
         */
        final StockViewModel stockViewModel = new StockViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();

        /*
         * Instantiate the Data Access Object.
         */
        final StockDataAccessInterface stockDataAccessObject =
                new FileStockDataAccessObject();

        /*
         * Manually wire the Clean Architecture layers together.
         */
        final StockPresenter stockPresenter = new StockPresenter(viewManagerModel, stockViewModel);
        final StockInteractor stockInteractor = new StockInteractor(stockDataAccessObject, stockPresenter);
        final StockController stockController = new StockController(stockInteractor);

        /*
         * Instantiate StockView with the 3 required constructor arguments.
         */
        final StockView stockView = new StockView(
                stockViewModel,
                viewManagerModel,
                loggedInViewModel
        );

        /*
         * Add StockView to CardLayout using its getter method.
         */
        views.add(stockView, stockView.getViewName());

        /*
         * Add a placeholder LoggedIn panel so pressing the back button has a target view.
         */
        final JPanel placeholderLoggedInView = new JPanel();
        placeholderLoggedInView.add(new JLabel("Profile / Logged In View Placeholder", SwingConstants.CENTER));
        views.add(placeholderLoggedInView, loggedInViewModel.getViewName());

        /*
         * Set the initial view to be the Stock View.
         */
        viewManagerModel.setState(stockView.getViewName());
        viewManagerModel.firePropertyChanged();

        /*
         * Trigger the controller to fetch and display data for a test stock!
         */
        stockController.execute("AAPL");
        application.pack();
        application.setSize(600, 400);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}