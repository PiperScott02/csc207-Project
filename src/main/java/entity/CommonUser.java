package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private RiskProfile riskProfile;
    private Portfolio portfolio;

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
        this.portfolio = new Portfolio();
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
    public Portfolio getPortfolio() {return portfolio;}

    @Override
    public void setPortfolio(Portfolio portfolio) {this.portfolio = portfolio;}

    @Override
    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }
}
