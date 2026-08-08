package interface_adapter.delete_watchlist;

import use_case.delete_watchlist.DeleteWatchlistInputBoundary;
import use_case.delete_watchlist.DeleteWatchlistInputData;

public class DeleteWatchlistController {
    private final DeleteWatchlistInputBoundary deleteWatchlistUseCaseInteractor;

    public DeleteWatchlistController(DeleteWatchlistInputBoundary deleteWatchlistUseCaseInteractor) {
        this.deleteWatchlistUseCaseInteractor = deleteWatchlistUseCaseInteractor;
    }

    public void execute(String ticker) {
        DeleteWatchlistInputData inputData = new DeleteWatchlistInputData(ticker);
        deleteWatchlistUseCaseInteractor.execute(inputData);
    }
}