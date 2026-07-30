package interface_adapter.logged_in;

import java.util.List;
import entity.StockHolding;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";

    private String password = "";
    private String passwordError;
    private List<StockHolding> holdings;

    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPassword() {
        return password;
    }

    // Getters and setters for holdings
    public List<StockHolding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<StockHolding> holdings) {
        this.holdings = holdings;
    }
}
