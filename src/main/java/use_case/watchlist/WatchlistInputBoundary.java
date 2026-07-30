package use_case.watchlist;

/** Input boundary for watchlist use cases. */
public interface WatchlistInputBoundary {

    /** Executes the watchlist use case with the provided input data.
     * @param inputData the input data containing the username or parameters.
     */
    void execute(WatchlistInputData inputData);
}