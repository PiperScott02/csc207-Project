package use_case.watchlist;

/** Output boundary interface for the watchlist use case. */
public interface WatchlistOutputBoundary {

    /** Prepares the success view for the watchlist use case.
     * @param outputData the output data containing watchlist details.
     */
    void prepareSuccessView(WatchlistOutputData outputData);

    /** Prepares the failure view with an error message.
     * @param errorMessage the error description string.
     */
    void prepareFailView(String errorMessage);
}