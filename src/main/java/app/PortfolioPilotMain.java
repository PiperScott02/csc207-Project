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

import use_case.StockDailyDataAccessInterface;
import use_case.TickerSearchDataAccessInterface;
import use_case.analysis.BlackLittermanService;
import use_case.black_litterman.BlackLittermanDataAccessInterface;
import use_case.news.NewsDataAccessInterface;
import use_case.similar_search.SimilarSearchDataAccessInterface;
import use_case.stock.StockDataAccessInterface;

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
        System.out.println("[DEBUG] 1. Application main started.");

        // Wrap everything in a try-catch to catch initialization crashes
        try {
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    System.out.println("[DEBUG] 2. Inside Event Dispatch Thread (EDT). Creating JFrame...");
                    final JFrame application = new JFrame("PortfolioPilot");

                    application.setDefaultCloseOperation(
                            WindowConstants.EXIT_ON_CLOSE
                    );

                    final CardLayout cardLayout = new CardLayout();
                    final JPanel views = new JPanel(cardLayout);

                    application.add(views);

                    System.out.println("[DEBUG] 3. Initializing ViewManagerModel...");
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
                    System.out.println("[DEBUG] 4. Instantiating View Models...");
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

                    final String apiKey = "API_KEY_PLACEHOLDER";

                    // ==========================================
                    // 2. Data Access Objects
                    // ==========================================
                    System.out.println("[DEBUG] 5. Instantiating DAOs...");
                    final FileUserDataAccessObject userDataAccessObject;

                    try {
                        userDataAccessObject =
                                new FileUserDataAccessObject(
                                        "data/users.csv",
                                        new CommonUserFactory()
                                );
                    } catch (IOException exception) {
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

                    // ==========================================
                    // 3. Controllers
                    // ==========================================
                    System.out.println("[DEBUG] 6. Creating Controllers...");
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
                                    stockDataAccessObject
                            );

                    final BlackLittermanDataAccessInterface blackLittermanDataAccessObject =
                            (BlackLittermanDataAccessInterface) stockDataAccessObject;
                    final BlackLittermanService blackLittermanService = new BlackLittermanService();

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
                    System.out.println("[DEBUG] 7. Assembling Views...");

                    final SignupView signupView =
                            SignupUseCaseFactory.create(
                                    viewManagerModel,
                                    loginViewModel,
                                    signupViewModel,
                                    userDataAccessObject
                            );
                    views.add(signupView, signupView.getViewName());

                    final LoginView loginView =
                            LoginUseCaseFactory.create(
                                    viewManagerModel,
                                    loginViewModel,
                                    loggedInViewModel,
                                    userDataAccessObject
                            );
                    views.add(loginView, loginView.getViewName());

                    final LoggedInView loggedInView =
                            new LoggedInView(
                                    loggedInViewModel,
                                    viewManagerModel,
                                    portfolioHealthController,
                                    blackLittermanController
                            );
                    views.add(loggedInView, loggedInView.getViewName());

                    final PortfolioHealthView portfolioHealthView =
                            PortfolioHealthUseCaseFactory.create(
                                    viewManagerModel,
                                    portfolioHealthViewModel,
                                    loggedInViewModel
                            );
                    views.add(portfolioHealthView, portfolioHealthView.viewName);

                    final NewsView newsView =
                            NewsUseCaseFactory.create(
                                    newsViewModel,
                                    viewManagerModel,
                                    apiKey
                            );
                    views.add(newsView, newsView.getViewName());

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

                    final StockView stockView =
                            StockUseCaseFactory.create(
                                    viewManagerModel,
                                    stockViewModel,
                                    loggedInViewModel,
                                    stockDataAccessObject
                            );
                    views.add(stockView, stockView.getViewName());

                    final RiskPreferenceView riskPreferenceView =
                            RiskPreferenceUseCaseFactory.create(
                                    viewManagerModel,
                                    riskPreferenceViewModel,
                                    userDataAccessObject
                            );
                    views.add(riskPreferenceView, riskPreferenceView.getViewName());

                    final WatchlistView watchlistView =
                            WatchlistUseCaseFactory.create(
                                    viewManagerModel,
                                    watchlistViewModel,
                                    loggedInViewModel,
                                    stockDataAccessObject
                            );
                    views.add(watchlistView, watchlistView.getViewName());

                    final BlackLittermanView blackLittermanView =
                            BlackLittermanUseCaseFactory.create(
                                    viewManagerModel,
                                    blackLittermanViewModel,
                                    blackLittermanDataAccessObject,
                                    blackLittermanService
                            );
                    views.add(blackLittermanView, blackLittermanView.getViewName());

                    final AddHoldingView addHoldingView =
                            AddHoldingUseCaseFactory.create(
                                    viewManagerModel,
                                    addHoldingViewModel,
                                    loggedInViewModel,
                                    stockDailyDataAccessObject
                            );
                    views.add(addHoldingView, addHoldingView.getViewName());

                    final AddWatchlistView addWatchlistView =
                            AddWatchlistUseCaseFactory.create(
                                    viewManagerModel,
                                    addWatchlistViewModel,
                                    watchlistViewModel,
                                    loggedInViewModel,
                                    stockDailyDataAccessObject
                            );
                    views.add(addWatchlistView, addWatchlistView.viewName);

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

                    // ==========================================
                    // 14. Startup Configuration
                    // ==========================================
                    System.out.println("[DEBUG] 8. Setting initial view state and packing window...");
                    viewManagerModel.setState(signupView.getViewName());
                    viewManagerModel.firePropertyChanged();

                    application.pack();
                    application.setSize(1250, 750);
                    application.setLocationRelativeTo(null);

                    System.out.println("[DEBUG] 9. Setting application visible = true...");
                    application.setVisible(true);
                    System.out.println("[DEBUG] 10. Frame successfully initialized and rendered.");

                } catch (Throwable t) {
                    System.err.println("[ERROR] Exception caught inside Swing EDT:");
                    t.printStackTrace();
                }
            });
        } catch (Throwable t) {
            System.err.println("[ERROR] Exception caught in main thread:");
            t.printStackTrace();
        }
    }
}