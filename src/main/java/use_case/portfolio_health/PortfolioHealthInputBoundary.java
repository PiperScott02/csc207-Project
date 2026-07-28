package use_case.portfolio_health;

/** Input boundary for the Portfolio Health use case. */
public interface PortfolioHealthInputBoundary {

    /** Executes the portfolio health use case with the provided input data.
     * @param inputData the input data containing the user entity.
     */
    void execute(PortfolioHealthInputData inputData);
}