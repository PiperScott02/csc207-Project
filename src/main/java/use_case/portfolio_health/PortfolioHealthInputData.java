package use_case.portfolio_health;

import entity.User;

/** The input data for the Portfolio Health use case. */
public class PortfolioHealthInputData {
    private final User user;

    /** Constructs a new PortfolioHealthInputData object with the provided user.
     * @param user the user entity.
     */
    public PortfolioHealthInputData(User user) {
        this.user = user;
    }

    /** Returns the user entity.
     * @return the user object.
     */
    public User getUser() {
        return user;
    }
}