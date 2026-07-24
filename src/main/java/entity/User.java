package entity;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Returns the user's saved risk profile.
     *
     * @return the user's risk profile
     */

    /** Returns the user's portfolio.
     * @return the user's portfolio entity.
     */
    Portfolio getPortfolio();

    RiskProfile getRiskProfile();

    /**
     * Updates the user's saved risk profile.
     *
     * @param riskProfile the new risk profile
     */
    void setRiskProfile(RiskProfile riskProfile);
}
