package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.InMemoryUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import view.LoggedInView;
import view.LoginView;
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
        final JFrame application = new JFrame("PortfolioPilot");

        application.setDefaultCloseOperation(
                WindowConstants.EXIT_ON_CLOSE
        );

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
        final ViewManagerModel viewManagerModel =
                new ViewManagerModel();

        new ViewManager(
                views,
                cardLayout,
                viewManagerModel
        );

        /*
         * Each page has a ViewModel containing the information
         * displayed by that page.
         */
        final LoginViewModel loginViewModel =
                new LoginViewModel();

        final SignupViewModel signupViewModel =
                new SignupViewModel();

        final LoggedInViewModel loggedInViewModel =
                new LoggedInViewModel();

        /*
         * Both signup and login must use the same DAO.
         *
         * That allows a user created during signup to be found
         * later during login.
         */
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

        /*
         * Start on signup for now because the existing signup page
         * already has a button that switches to the login page.
         */
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setSize(1250, 750);
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}