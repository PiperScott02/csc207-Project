package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.InMemoryUserDataAccessObject;
import data_access.similar_search.SimilarSearchDataAccessObject;
import data_access.stock_daily.StockService;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.news.NewsViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.ticker_search.TickerSearchViewModel;
import use_case.StockDailyDataAccessInterface;
import use_case.similar_search.SimilarSearchDataAccessInterface;
import view.LoggedInView;
import view.LoginView;
import view.NewsView;
import view.RiskPreferenceView;
import view.SearchView;
import view.SignupView;
import view.ViewManager;

/**
 * Starts PortfolioPilot with signup and login functionality.
 */
public final class PortfolioPilotMain {

    private PortfolioPilotMain() {
        // Prevent this utility class from being instantiated.
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        final JFrame application =
                new JFrame("PortfolioPilot");

        application.setDefaultCloseOperation(
                WindowConstants.EXIT_ON_CLOSE
        );

        final CardLayout cardLayout = new CardLayout();
        final JPanel views = new JPanel(cardLayout);

        application.add(views);

        final ViewManagerModel viewManagerModel =
                new ViewManagerModel();

        new ViewManager(
                views,
                cardLayout,
                viewManagerModel
        );

        final LoginViewModel loginViewModel =
                new LoginViewModel();

        final SignupViewModel signupViewModel =
                new SignupViewModel();

        final LoggedInViewModel loggedInViewModel =
                new LoggedInViewModel();

        final NewsViewModel newsViewModel =
                new NewsViewModel();

        final SimilarSearchViewModel similarSearchViewModel =
                new SimilarSearchViewModel();

        final TickerSearchViewModel tickerSearchViewModel =
                new TickerSearchViewModel();

        /*
         * Read the Alpha Vantage API key once.
         */
        final String apiKey =
                "NKH8SNZW8I690AJQ";

        final InMemoryUserDataAccessObject userDataAccessObject =
                new InMemoryUserDataAccessObject();

        final SignupView signupView =
                SignupUseCaseFactory.create(
                        viewManagerModel,
                        loginViewModel,
                        signupViewModel,
                        userDataAccessObject
                );

        views.add(
                signupView,
                signupView.getViewName()
        );

        final LoginView loginView =
                LoginUseCaseFactory.create(
                        viewManagerModel,
                        loginViewModel,
                        loggedInViewModel,
                        userDataAccessObject
                );

        views.add(
                loginView,
                loginView.getViewName()
        );

        final LoggedInView loggedInView =
                new LoggedInView(
                        loggedInViewModel,
                        viewManagerModel
                );

        views.add(
                loggedInView,
                loggedInView.getViewName()
        );

        final NewsView newsView =
                NewsUseCaseFactory.create(
                        newsViewModel,
                        viewManagerModel,
                        apiKey
                );

        views.add(
                newsView,
                newsView.getViewName()
        );

        final StockDailyDataAccessInterface
                stockDailyDataAccessObject =
                new StockService(apiKey);

        final SimilarSearchDataAccessInterface
                similarSearchDataAccessObject =
                new SimilarSearchDataAccessObject(apiKey);

        final SearchView searchView =
                SearchUseCaseFactory.create(
                        viewManagerModel,
                        similarSearchViewModel,
                        tickerSearchViewModel,
                        stockDailyDataAccessObject,
                        similarSearchDataAccessObject
                );

        views.add(
                searchView,
                searchView.getViewName()
        );

        final RiskPreferenceView riskPreferenceView =
                new RiskPreferenceView(viewManagerModel);

        views.add(
                riskPreferenceView,
                riskPreferenceView.getViewName()
        );

        viewManagerModel.setState(
                signupView.getViewName()
        );

        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setSize(1250, 750);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
