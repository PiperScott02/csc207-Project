package use_case.black_litterman;

import entity.User;

import java.util.Map;

/** The input data for the Black-Litterman use case. */
public class BlackLittermanInputData {
    private final User user;
    private final Map<String, Double> userViews;
    private final Map<String, String> confidenceLevels;

    /**
     * Constructs a new BlackLittermanInputData object with the provided user, views, and confidence levels.
     *
     * @param user             the user entity.
     * @param userViews        a map of asset tickers to the user's expected return opinions.
     * @param confidenceLevels a map of asset tickers to the user's confidence levels.
     */
    public BlackLittermanInputData(User user, Map<String, Double> userViews, Map<String, String> confidenceLevels) {
        this.user = user;
        this.userViews = userViews;
        this.confidenceLevels = confidenceLevels;
    }

    /**
     * Returns the user entity.
     *
     * @return the user object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the user's expected return opinions for the assets.
     *
     * @return a map of asset tickers to expected return values.
     */
    public Map<String, Double> getUserViews() {
        return userViews;
    }

    /**
     * Returns the user's confidence levels for the views.
     *
     * @return a map of asset tickers to confidence level strings.
     */
    public Map<String, String> getConfidenceLevels() {
        return confidenceLevels;
    }
}