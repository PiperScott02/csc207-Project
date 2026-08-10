package app;

import data_access.FileStockDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entity.CommonUserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_watchlist.AddWatchlistViewModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.black_litterman.BlackLittermanViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.analysis.BlackLittermanService;
import use_case.black_litterman.BlackLittermanDataAccessInterface;
import use_case.news.NewsDataAccessInterface;
import use_case.portfolio_health.PortfolioHealthDataAccessInterface;
import use_case.stock.StockDataAccessInterface;
import use_case.watchlist.WatchlistDataAccessInterface;
import view.*;

import javax.swing.*;
import java.awt.*;

/**
 * The version of Main with an in-memory database used to persist user data.
 */
public class MainWithInMemory {

    /**
     * The main method for starting the program with an in-memory database used to persist user data.
     * @param args input to main
     */
    public static void main(String[] args) {
        // Build the main program window, the main panel containing the
        // various cards, and the layout, and stitch them together.

        // The main application window.
        final JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final CardLayout cardLayout = new CardLayout();

        // The various View objects. Only one view is visible at a time.
        final JPanel views = new JPanel(cardLayout);
        application.add(views);

        // This keeps track of and manages which view is currently showing.
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // The data for the views, such as username and password, are in the ViewModels.
        final LoginViewModel loginViewModel = new LoginViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final WatchlistViewModel watchlistViewModel = new WatchlistViewModel();
        final AddWatchlistViewModel addWatchlistViewModel = new AddWatchlistViewModel();
        final BlackLittermanViewModel blackLittermanViewModel = new BlackLittermanViewModel();
        final PortfolioHealthViewModel portfolioHealthViewModel = new PortfolioHealthViewModel();

        final InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();
        final StockDataAccessInterface stockDataAccessObject = new FileStockDataAccessObject();

        // Instantiate services and view models needed for Black-Litterman
        final BlackLittermanService blackLittermanService = new BlackLittermanService();

        // 1. Get the Black-Litterman Controller to pass into the Watchlist factory
        final BlackLittermanController blackLittermanController = BlackLittermanUseCaseFactory.createBlackLittermanUseCase(
                viewManagerModel,
                blackLittermanViewModel,
                (BlackLittermanDataAccessInterface) stockDataAccessObject,
                blackLittermanService
        );

        // 2. Create the Black-Litterman View and add it to the card layout panel
        final BlackLittermanView blackLittermanView = BlackLittermanUseCaseFactory.create(
                viewManagerModel,
                blackLittermanViewModel,
                (BlackLittermanDataAccessInterface) stockDataAccessObject,
                blackLittermanService,
                loggedInViewModel
        );
        views.add(blackLittermanView, blackLittermanView.getViewName());

        // 3. Get the Portfolio Health Controller (takes stock and news DAOs)
        final PortfolioHealthController portfolioHealthController = PortfolioHealthUseCaseFactory.createPortfolioHealthUseCase(
                viewManagerModel,
                portfolioHealthViewModel,
                stockDataAccessObject,
                (NewsDataAccessInterface) stockDataAccessObject
        );

        // 4. Create the Portfolio Health View (takes ONLY 3 arguments: viewManagerModel, viewModel, loggedInViewModel)
        final PortfolioHealthView portfolioHealthView = PortfolioHealthUseCaseFactory.create(
                viewManagerModel,
                portfolioHealthViewModel,
                loggedInViewModel,
                portfolioHealthController
        );
        views.add(portfolioHealthView, "portfolio health");

        final SignupView signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel,
                signupViewModel, userDataAccessObject);
        views.add(signupView, signupView.getViewName());

        final LoginView loginView = LoginUseCaseFactory.create(
                viewManagerModel,
                loginViewModel,
                loggedInViewModel,
                signupViewModel,
                watchlistViewModel,
                userDataAccessObject
        );
        views.add(loginView, loginView.getViewName());

        final LoggedInView loggedInView = ChangePasswordUseCaseFactory.create(viewManagerModel,
                loggedInViewModel, userDataAccessObject);
        views.add(loggedInView, loggedInView.getViewName());

        // Watchlist View wired with all 8 required arguments
        final WatchlistView watchlistView = WatchlistUseCaseFactory.create(
                viewManagerModel,
                watchlistViewModel,
                loggedInViewModel,
                addWatchlistViewModel,
                stockDataAccessObject,
                (WatchlistDataAccessInterface) userDataAccessObject,
                blackLittermanController,
                portfolioHealthController
        );
        views.add(watchlistView, watchlistView.getViewName());

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setVisible(true);
    }
}