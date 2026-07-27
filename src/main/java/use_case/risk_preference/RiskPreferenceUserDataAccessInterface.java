package use_case.risk_preference;

import entity.User;

/**
 * Data access required by the risk-preference use case.
 */
public interface RiskPreferenceUserDataAccessInterface {

    /**
     * Returns the currently logged-in username.
     *
     * @return the current username
     */
    String getCurrentUser();

    /**
     * Returns the user with the given username.
     *
     * @param username the username
     * @return the matching user
     */
    User get(String username);

    /**
     * Saves the updated user.
     *
     * @param user the updated user
     */
    void save(User user);
}