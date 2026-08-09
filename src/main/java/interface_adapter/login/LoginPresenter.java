package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.watchlist.WatchlistState;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import interface_adapter.signup.SignupViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;
    private final WatchlistViewModel watchlistViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel,
                          WatchlistViewModel watchlistViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.watchlistViewModel = watchlistViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        loggedInState.setUser(response.getUser());

        if (response.getUser() != null && response.getUser().getPortfolio() != null) {
            loggedInState.setHoldings(response.getUser().getPortfolio().getHoldings());
            loggedInState.setWatchlist(response.getUser().getPortfolio().getWatchlist());

            // Pre-load the user's saved watchlist into the WatchlistViewModel
            // so it appears immediately when switching to the watchlist tab upon login.
            List<WatchlistState.WatchlistStockItem> stateItems = new ArrayList<>();
            for (entity.WatchlistStockItem item : response.getUser().getPortfolio().getWatchlist()) {
                String closeStr = (item.closePrice() != null) ? item.closePrice().toString() : "";
                String changeStr = (item.dailyPriceChange() != null) ? item.dailyPriceChange().toString() : "";

                stateItems.add(new WatchlistState.WatchlistStockItem(
                        item.ticker(),
                        item.companyName() != null ? item.companyName() : "",
                        closeStr,
                        changeStr
                ));
            }

            WatchlistState watchlistState = new WatchlistState();
            watchlistState.setItems(stateItems);
            this.watchlistViewModel.setState(watchlistState);
            this.watchlistViewModel.firePropertyChanged();

        }

        this.loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChanged();

        this.viewManagerModel.setState(loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignupView() {
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}