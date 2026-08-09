package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import data_access.FileStockDataAccessObject;
import data_access.FileUserDataAccessObject;
import entity.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistController;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.stock.StockDataAccessInterface;
import view.ViewManager;
import view.WatchlistView;

/**
 * Starts a test environment specifically for the Watchlist display UI.
 */
public final class WatchlistPilotMain {

    private WatchlistPilotMain() {
        // Prevent this utility class from being instantiated.
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        final JFrame application = new JFrame("Watchlist Viewer Pilot");
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
        final WatchlistViewModel watchlistViewModel = new WatchlistViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();

        /*
         * Instantiate the Data Access Objects.
         */
        final StockDataAccessInterface stockDataAccessObject =
                new FileStockDataAccessObject();

        final FileUserDataAccessObject userDataAccessObject;
        try {
            userDataAccessObject = new FileUserDataAccessObject(
                    "data/users.csv",
                    new CommonUserFactory()
            );
        } catch (java.io.IOException exception) {
            throw new RuntimeException("Unable to initialize user storage.", exception);
        }

        /*
         * Manually wire the Clean Architecture layers using the Factory.
         */
        final WatchlistController watchlistController = WatchlistUseCaseFactory.createWatchlistUseCase(
                viewManagerModel,
                watchlistViewModel,
                loggedInViewModel,
                stockDataAccessObject
        );

        /*
         * Instantiate WatchlistView with the required constructor arguments.
         */
        final WatchlistView watchlistView = WatchlistUseCaseFactory.create(
                viewManagerModel,
                watchlistViewModel,
                loggedInViewModel,
                stockDataAccessObject,
                userDataAccessObject
        );

        /*
         * Add WatchlistView to CardLayout using its getter method.
         */
        views.add(watchlistView, watchlistView.getViewName());

        /*
         * Add a placeholder LoggedIn panel so pressing the back button has a target view.
         */
        final JPanel placeholderLoggedInView = new JPanel();
        placeholderLoggedInView.add(new JLabel("Profile / Logged In View Placeholder", SwingConstants.CENTER));
        views.add(placeholderLoggedInView, loggedInViewModel.getViewName());

        /*
         * Set the initial view to be the Watchlist View.
         */
        viewManagerModel.setState(watchlistView.getViewName());
        viewManagerModel.firePropertyChanged();

        /*
         * Build a test user.
         */
        CommonUserFactory commonUserFactory = new CommonUserFactory();
        User testUser = commonUserFactory.create("Alex", "password");
        Portfolio portfolio = new Portfolio();

        // Fetch real or mock stock data using your stock DAO
        Stock stock = stockDataAccessObject.get("AAPL");

        if (stock != null) {
            // Create the watchlist item using data from the Stock entity
            WatchlistStockItem item = new WatchlistStockItem(
                    stock.getTickerSymbol(),
                    stock.getCompanyName(),
                    stock.getClose(),
                    stock.getDailyPriceChange()
            );

            testUser.getPortfolio().getWatchlist().add(item);

            // You can save testUser into your watchlistDataAccessObject here if needed for your DAO test

            /*
             * Trigger the controller to fetch and display data for a test user!
             */
            watchlistController.execute(testUser);
            application.pack();
            application.setSize(600, 400);
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        }
    }
}