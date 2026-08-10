package use_case.delete_watchlist;

public interface DeleteWatchlistOutputBoundary {
    void prepareSuccessView(DeleteWatchlistOutputData outputData);
    void prepareFailView(String error);
}