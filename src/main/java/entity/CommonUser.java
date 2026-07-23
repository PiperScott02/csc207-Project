package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private RiskProfile riskProfile;

    /**
     * Creates a new user.
     *
     * @param name the username
     * @param password the password
     */
    public CommonUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.riskProfile = new RiskProfile();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    @Override
    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }
}
