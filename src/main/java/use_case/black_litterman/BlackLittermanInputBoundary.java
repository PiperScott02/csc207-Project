package use_case.black_litterman;

/** Input boundary for the Black-Litterman use case. */
public interface BlackLittermanInputBoundary {

    /** Executes the Black-Litterman use case with the provided input data.
     * @param inputData the input data containing the user entity.
     */
    void execute(BlackLittermanInputData inputData);
}