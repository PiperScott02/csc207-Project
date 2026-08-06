package use_case.add_watchlist;

/**
 * The Add Watchlist Use Case.
 */
public interface AddWatchlistInputBoundary {

    /**
     * Execute the Add Watchlist Use Case.
     * @param addWatchlistInputData the input data for this use case
     */
    void execute(AddWatchlistInputData addWatchlistInputData);
}
