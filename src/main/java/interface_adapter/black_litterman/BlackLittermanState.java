package interface_adapter.black_litterman;

import entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The State for the Black-Litterman view, storing user-entered opinions, confidence levels,
 * market returns, adjusted expected returns, top stock tickers, and error messages using maps.
 */
public class BlackLittermanState {
    private User user;
    private List<String> topTickers = new ArrayList<>();
    private Map<String, Double> userViews = new HashMap<>();
    private Map<String, String> confidenceLevels = new HashMap<>();
    private Map<String, Double> marketReturns = new HashMap<>();
    private Map<String, Double> adjustedReturns = new HashMap<>();
    private String errorMessage = "";

    /** Copy constructor to create a new BlackLittermanState from an existing one.
     * @param copy the BlackLittermanState to copy from.
     */
    public BlackLittermanState(BlackLittermanState copy) {
        this.user = copy.user;
        this.topTickers = new ArrayList<>(copy.topTickers);
        this.userViews = new HashMap<>(copy.userViews);
        this.confidenceLevels = new HashMap<>(copy.confidenceLevels);
        this.marketReturns = new HashMap<>(copy.marketReturns);
        this.adjustedReturns = new HashMap<>(copy.adjustedReturns);
        this.errorMessage = copy.errorMessage;
    }

    /** Default constructor to create an empty BlackLittermanState. */
    public BlackLittermanState() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getTopTickers() {
        return topTickers;
    }

    public void setTopTickers(List<String> topTickers) {
        this.topTickers = topTickers;
    }

    public Map<String, Double> getUserViews() {
        return userViews;
    }

    public void setUserViews(Map<String, Double> userViews) {
        this.userViews = userViews;
    }

    public Map<String, String> getConfidenceLevels() {
        return confidenceLevels;
    }

    public void setConfidenceLevels(Map<String, String> confidenceLevels) {
        this.confidenceLevels = confidenceLevels;
    }

    public Map<String, Double> getMarketReturns() {
        return marketReturns;
    }

    public void setMarketReturns(Map<String, Double> marketReturns) {
        this.marketReturns = marketReturns;
    }

    public Map<String, Double> getAdjustedReturns() {
        return adjustedReturns;
    }

    public void setAdjustedReturns(Map<String, Double> adjustedReturns) {
        this.adjustedReturns = adjustedReturns;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}