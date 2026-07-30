package use_case.watchlist;

import entity.User;

/** Input data for the Watchlist use case, containing the username of the user. */
public class WatchlistInputData {
    private final User user;

    /** Constructs WatchlistInputData with the given username.
     * @param user the user.
     */
    public WatchlistInputData(User user) {
        this.user = user;
    }

    /** Returns the user.
     * @return the User object corresponding to the user.
     */
    public User getUser() {
        return user;
    }
}