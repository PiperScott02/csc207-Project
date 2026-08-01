package use_case.add_watchlist;

public interface AddWatchlistOutputBoundary {
    void prepareSuccessView(AddWatchlistOutputData outputData);
    void prepareFailView(String error);
}