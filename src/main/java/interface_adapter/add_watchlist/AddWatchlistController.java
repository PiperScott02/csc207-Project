package interface_adapter.add_watchlist;

import use_case.add_watchlist.AddWatchlistInputBoundary;
import use_case.add_watchlist.AddWatchlistInputData;

/**
 * The Controller for the Add Watchlist Item use case, handling user input and triggering the interactor.
 */
public class AddWatchlistController {
    private final AddWatchlistInputBoundary addWatchlistUseCaseInteractor;

    public AddWatchlistController(AddWatchlistInputBoundary addWatchlistUseCaseInteractor) {
        this.addWatchlistUseCaseInteractor = addWatchlistUseCaseInteractor;
    }

    /**
     * Executes the Add Watchlist Item use case with the user-specified stock ticker.
     *
     * @param ticker the stock ticker symbol entered by the user
     */
    public void execute(String ticker) {
        AddWatchlistInputData inputData = new AddWatchlistInputData(ticker);
        addWatchlistUseCaseInteractor.execute(inputData);
    }
}