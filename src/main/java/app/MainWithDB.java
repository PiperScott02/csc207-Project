package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.DBUserDataAccessObject;
import data_access.FileStockDataAccessObject;
import data_access.similar_search.SimilarSearchDataAccessObject;
import data_access.stock_daily.StockService;
import entity.CommonUserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.news.NewsViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockPresenter;
import interface_adapter.stock.StockViewModel;
import interface_adapter.ticker_search.TickerSearchViewModel;
import use_case.StockDailyDataAccessInterface;
import use_case.similar_search.SimilarSearchDataAccessInterface;
import use_case.stock.StockDataAccessInterface;
import use_case.stock.StockInteractor;
import view.LoggedInView;
import view.LoginView;
import view.NewsView;
import view.RiskPreferenceView;
import view.SearchView;
import view.SignupView;
import view.StockView;
import view.ViewManager;

/**
 * The version of Main with an external database used to persist user data.
 */
public class MainWithDB {

    /**
     * The main method for starting the program with an external database used to persist user data.
     * @param args input to main
     */
    public static void main(String[] args) {
        // Build the main program window
        final JFrame application = new JFrame("PortfolioPilot");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final CardLayout cardLayout = new CardLayout();

        // The main panel containing the views
        final JPanel views = new JPanel(cardLayout);
        application.add(views);

        // ViewManager handles switching screens
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // Initialize View Models
        final LoginViewModel loginViewModel = new LoginViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final NewsViewModel newsViewModel = new NewsViewModel();
        final SimilarSearchViewModel similarSearchViewModel = new SimilarSearchViewModel();
        final TickerSearchViewModel tickerSearchViewModel = new TickerSearchViewModel();
        final StockViewModel stockViewModel = new StockViewModel();

        /*
         * Alpha Vantage API key used for stock/news queries.
         */
        final String apiKey = "";

        // Initialize Data Access Objects
        final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(new CommonUserFactory());
        final StockDailyDataAccessInterface stockDailyDataAccessObject = new StockService(apiKey);
        final SimilarSearchDataAccessInterface similarSearchDataAccessObject = new SimilarSearchDataAccessObject(apiKey);
        final StockDataAccessInterface stockDataAccessObject = new FileStockDataAccessObject();

        // Setup Stock Use Case (Presenter -> Interactor -> Controller)
        final StockPresenter stockPresenter = new StockPresenter(viewManagerModel, stockViewModel);
        final StockInteractor stockInteractor = new StockInteractor(stockDataAccessObject, stockPresenter);
        final StockController stockController = new StockController(stockInteractor);

        // 1. Signup View
        final SignupView signupView = SignupUseCaseFactory.create(
                viewManagerModel,
                loginViewModel,
                signupViewModel,
                userDataAccessObject
        );
        views.add(signupView, signupView.getViewName());

        // 2. Login View
        final LoginView loginView = LoginUseCaseFactory.create(
                viewManagerModel,
                loginViewModel,
                loggedInViewModel,
                userDataAccessObject
        );
        views.add(loginView, loginView.getViewName());

        // 3. Logged In View
        final LoggedInView loggedInView = ChangePasswordUseCaseFactory.create(
                viewManagerModel,
                loggedInViewModel,
                userDataAccessObject
        );
        views.add(loggedInView, loggedInView.getViewName());

        // 4. News View
        final NewsView newsView = NewsUseCaseFactory.create(
                newsViewModel,
                viewManagerModel,
                apiKey
        );
        views.add(newsView, newsView.getViewName());

        // 5. Search View
        final SearchView searchView = SearchUseCaseFactory.create(
                viewManagerModel,
                similarSearchViewModel,
                tickerSearchViewModel,
                stockViewModel,
                stockDailyDataAccessObject,
                similarSearchDataAccessObject,
                stockController
        );
        views.add(searchView, searchView.getViewName());

        // 6. Stock View (Passes required View Models & View Manager for back navigation)
        final StockView stockView = new StockView(
                stockViewModel,
                viewManagerModel,
                loggedInViewModel
        );
        views.add(stockView, stockView.getViewName());

        // 7. Risk Preference View
        final RiskPreferenceView riskPreferenceView = new RiskPreferenceView(viewManagerModel);
        views.add(riskPreferenceView, riskPreferenceView.getViewName());

        // Set initial starting view
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setSize(1250, 750);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}