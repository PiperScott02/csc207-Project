    package app;

    import java.awt.CardLayout;

    import javax.swing.JFrame;
    import javax.swing.JPanel;
    import javax.swing.WindowConstants;

    import use_case.portfolio_health.PortfolioHealthDataAccessInterface;
    import data_access.FileStockDataAccessObject;
    import data_access.InMemoryUserDataAccessObject;
    import data_access.similar_search.SimilarSearchDataAccessObject;
    import data_access.stock_daily.StockService;

    import interface_adapter.ViewManagerModel;
    import interface_adapter.logged_in.LoggedInViewModel;
    import interface_adapter.login.LoginViewModel;
    import interface_adapter.news.NewsViewModel;
    import interface_adapter.portfolio_health.PortfolioHealthController;
    import interface_adapter.portfolio_health.PortfolioHealthViewModel;
    import interface_adapter.signup.SignupViewModel;
    import interface_adapter.similar_search.SimilarSearchViewModel;
    import interface_adapter.stock.StockController;
    import interface_adapter.stock.StockViewModel;
    import interface_adapter.ticker_search.TickerSearchViewModel;

    import use_case.StockDailyDataAccessInterface;
    import use_case.portfolio_health.PortfolioHealthDataAccessInterface;
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

            /*
             * Alpha Vantage API key
             */
            final String apiKey = "NKH8SNZW8I690AJQ";

            // ==========================================
            // 2. Data Access Objects
            // ==========================================
            final InMemoryUserDataAccessObject userDataAccessObject =
                    new InMemoryUserDataAccessObject();

            final StockDailyDataAccessInterface stockDailyDataAccessObject =
                    new StockService(apiKey);

            final SimilarSearchDataAccessInterface similarSearchDataAccessObject =
                    new SimilarSearchDataAccessObject(apiKey);

            final StockDataAccessInterface stockDataAccessObject =
                    new FileStockDataAccessObject();


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
                            stockDataAccessObject
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
            final LoginView loginView =
                    LoginUseCaseFactory.create(
                            viewManagerModel,
                            loginViewModel,
                            loggedInViewModel,
                            userDataAccessObject
                    );
            views.add(loginView, loginView.getViewName());

            // 3. Logged In View
            final LoggedInView loggedInView =
                    new LoggedInView(
                            loggedInViewModel,
                            viewManagerModel,
                            portfolioHealthController
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
                            stockDailyDataAccessObject,
                            similarSearchDataAccessObject,
                            stockController
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
                    new RiskPreferenceView(viewManagerModel);
            views.add(riskPreferenceView, riskPreferenceView.getViewName());

            // ==========================================
            // 9. Startup Configuration
            // ==========================================
            viewManagerModel.setState(signupView.getViewName());
            viewManagerModel.firePropertyChanged();

            application.pack();
            application.setSize(1250, 750);
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        }
    }