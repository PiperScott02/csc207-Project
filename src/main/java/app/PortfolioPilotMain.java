package app;

import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.AlphaVantageNewsDataAccessObject;
import data_access.FileStockDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.similar_search.SimilarSearchDataAccessObject;
import data_access.stock_daily.StockService;
import data_access.ticker_search.TickerSearchDataAccessObject;
import data_access.FileWatchlistDataAccessObject;
import entity.CommonUserFactory;

import interface_adapter.ViewManagerModel;
import interface_adapter.add_holding.AddHoldingViewModel;
import interface_adapter.add_watchlist.AddWatchlistViewModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.black_litterman.BlackLittermanViewModel;
import interface_adapter.currency_conversion.CurrencyConversionController;
import interface_adapter.currency_conversion.CurrencyConversionViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.news.NewsViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import interface_adapter.risk_preference.RiskPreferenceViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.similar_search.SimilarSearchViewModel;
import interface_adapter.stock.StockController;
import interface_adapter.stock.StockViewModel;
import interface_adapter.ticker_search.TickerSearchViewModel;
import interface_adapter.watchlist.WatchlistController;
import interface_adapter.watchlist.WatchlistViewModel;
import interface_adapter.delete_holding.DeleteHoldingController;

import use_case.StockDailyDataAccessInterface;
import use_case.TickerSearchDataAccessInterface;
import use_case.analysis.BlackLittermanService;
import use_case.black_litterman.BlackLittermanDataAccessInterface;
import use_case.news.NewsDataAccessInterface;
import use_case.similar_search.SimilarSearchDataAccessInterface;
import use_case.stock.StockDataAccessInterface;
import use_case.watchlist.WatchlistDataAccessInterface;

import view.*;

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

        // ==========================================
        // 1. View Models
        // ==========================================
        final LoginViewModel loginViewModel = new LoginViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        final NewsViewModel newsViewModel = new NewsViewModel();
        final SimilarSearchViewModel similarSearchViewModel = new SimilarSearchViewModel();
        final TickerSearchViewModel tickerSearchViewModel = new TickerSearchViewModel();
        final StockViewModel stockViewModel = new StockViewModel();
        final PortfolioHealthViewModel portfolioHealthViewModel = new PortfolioHealthViewModel();
        final RiskPreferenceViewModel riskPreferenceViewModel = new RiskPreferenceViewModel();
        final WatchlistViewModel watchlistViewModel = new WatchlistViewModel();
        final BlackLittermanViewModel blackLittermanViewModel = new BlackLittermanViewModel();
        final AddHoldingViewModel addHoldingViewModel = new AddHoldingViewModel();
        final AddWatchlistViewModel addWatchlistViewModel = new AddWatchlistViewModel();
        final CurrencyConversionViewModel currencyConversionViewModel =
                new CurrencyConversionViewModel();

        /*
         * Alpha Vantage API key
         */
        final String apiKey = "INSERT_API_KEY";

        // ==========================================
        // 2. Data Access Objects
        // ==========================================
        final FileUserDataAccessObject userDataAccessObject;

        try {
            userDataAccessObject =
                    new FileUserDataAccessObject(
                            "data/users.csv",
                            new CommonUserFactory()
                    );
        }
        catch (IOException exception) {
            throw new RuntimeException(
                    "Unable to initialize user storage.",
                    exception
            );
        }

        final StockDailyDataAccessInterface stockDailyDataAccessObject =
                new StockService(apiKey);

        final SimilarSearchDataAccessInterface similarSearchDataAccessObject =
                new SimilarSearchDataAccessObject(apiKey);

        final TickerSearchDataAccessInterface tickerSearchDataAccessObject =
                new TickerSearchDataAccessObject(apiKey);

        final StockDataAccessInterface stockDataAccessObject =
                new FileStockDataAccessObject();

        final NewsDataAccessInterface newsDataAccessObject =
                new AlphaVantageNewsDataAccessObject(apiKey);

        final WatchlistDataAccessInterface watchlistDataAccessObject =
                new FileWatchlistDataAccessObject("data/watchlist.csv");

        // ==========================================
        // 3. Controllers
        // ==========================================
        final StockController stockController =
                StockUseCaseFactory.createStockUseCase(
                        viewManagerModel,
                        stockViewModel,
                        stockDataAccessObject
                );

        final PortfolioHealthController portfolioHealthController =
                PortfolioHealthUseCaseFactory.createPortfolioHealthUseCase(
                        viewManagerModel,
                        portfolioHealthViewModel,
                        stockDataAccessObject,
                        newsDataAccessObject
                );

        final WatchlistController watchlistController =
                WatchlistUseCaseFactory.createWatchlistUseCase(
                        viewManagerModel,
                        watchlistViewModel,
                        loggedInViewModel,
                        stockDataAccessObject
                );

        final DeleteHoldingController deleteHoldingController =
                DeleteHoldingUseCaseFactory.create(
                        loggedInViewModel,
                        userDataAccessObject
                );

        // Instantiate dependencies for Black-Litterman
        final BlackLittermanDataAccessInterface blackLittermanDataAccessObject =
                (BlackLittermanDataAccessInterface) stockDataAccessObject;
        final BlackLittermanService blackLittermanService = new BlackLittermanService();

        // Black-Litterman Controller Creation with correct parameters
        final BlackLittermanController blackLittermanController =
                BlackLittermanUseCaseFactory.createBlackLittermanUseCase(
                        viewManagerModel,
                        blackLittermanViewModel,
                        blackLittermanDataAccessObject,
                        blackLittermanService
                );

        final CurrencyConversionController currencyConversionController =
                CurrencyConversionUseCaseFactory.create(
                        currencyConversionViewModel
                );

        // ==========================================
        // 4. View Creation & Assembly
        // ==========================================

        // 1. Signup View
        final SignupView signupView =
                SignupUseCaseFactory.create(
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
                signupViewModel,
                watchlistViewModel,
                userDataAccessObject
        );
        views.add(loginView, loginView.getViewName());

        // 3. Logged In View
        final LoggedInView loggedInView =
                new LoggedInView(
                        loggedInViewModel,
                        viewManagerModel,
                        portfolioHealthController,
                        blackLittermanController
                );
        views.add(loggedInView, loggedInView.getViewName());

        // 4. Portfolio Health View
        final PortfolioHealthView portfolioHealthView =
                PortfolioHealthUseCaseFactory.create(
                        viewManagerModel,
                        portfolioHealthViewModel,
                        loggedInViewModel
                );
        views.add(portfolioHealthView, portfolioHealthView.viewName);

        // 5. News View
        final NewsView newsView =
                NewsUseCaseFactory.create(
                        newsViewModel,
                        viewManagerModel,
                        apiKey
                );
        views.add(newsView, newsView.getViewName());

        // 6. Search View
        final SearchView searchView =
                SearchUseCaseFactory.create(
                        viewManagerModel,
                        similarSearchViewModel,
                        tickerSearchViewModel,
                        stockViewModel,
                        tickerSearchDataAccessObject,
                        similarSearchDataAccessObject,
                        stockController,
                        loggedInViewModel
                );
        views.add(searchView, searchView.getViewName());

        // 7. Stock View
        final StockView stockView =
                StockUseCaseFactory.create(
                        viewManagerModel,
                        stockViewModel,
                        loggedInViewModel,
                        stockDataAccessObject
                );
        views.add(stockView, stockView.getViewName());

        // 8. Risk Preference View
        final RiskPreferenceView riskPreferenceView =
                RiskPreferenceUseCaseFactory.create(
                        viewManagerModel,
                        riskPreferenceViewModel,
                        userDataAccessObject
                );
        views.add(riskPreferenceView, riskPreferenceView.getViewName());

        // 9. Watchlist View
        final WatchlistView watchlistView =
                WatchlistUseCaseFactory.create(
                        viewManagerModel,
                        watchlistViewModel,
                        loggedInViewModel,
                        stockDataAccessObject,
                        userDataAccessObject
                );
        views.add(watchlistView, watchlistView.getViewName());

        // 10. Black-Litterman View
        final BlackLittermanView blackLittermanView =
                BlackLittermanUseCaseFactory.create(
                        viewManagerModel,
                        blackLittermanViewModel,
                        blackLittermanDataAccessObject,
                        blackLittermanService,
                        loggedInViewModel
                );
        views.add(blackLittermanView, blackLittermanView.getViewName());

        // 11. Add Holding View
        final AddHoldingView addHoldingView =
                AddHoldingUseCaseFactory.create(
                        viewManagerModel,
                        addHoldingViewModel,
                        loggedInViewModel,
                        stockDailyDataAccessObject,
                        userDataAccessObject
                );
        views.add(addHoldingView, addHoldingView.getViewName());

        // 12. Add Watchlist View
        final AddWatchlistView addWatchlistView =
                AddWatchlistUseCaseFactory.create(
                        viewManagerModel,
                        addWatchlistViewModel,
                        watchlistViewModel,
                        loggedInViewModel,
                        stockDailyDataAccessObject,
                        userDataAccessObject
                );
        views.add(addWatchlistView, addWatchlistView.viewName);

        // 13. Currency Conversion View
        final CurrencyConversionView currencyConversionView =
                new CurrencyConversionView(
                        viewManagerModel,
                        currencyConversionController,
                        currencyConversionViewModel,
                        loggedInViewModel
                );
        views.add(
                currencyConversionView,
                currencyConversionView.getViewName()
        );

        // 14. Holdings View
        final HoldingsView holdingsView = new HoldingsView(
                viewManagerModel,
                loggedInViewModel,
                deleteHoldingController
        );
        views.add(holdingsView, holdingsView.getViewName());

        // ==========================================
        // 15. Startup Configuration
        // ==========================================
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setSize(1250, 750);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}