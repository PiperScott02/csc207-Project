package interface_adapter.delete_watchlist;

import entity.User;
import use_case.delete_watchlist.DeleteWatchlistInputBoundary;
import use_case.delete_watchlist.DeleteWatchlistInputData;
import use_case.login.LoginUserDataAccessInterface;

public class DeleteWatchlistController {
    private final DeleteWatchlistInputBoundary deleteWatchlistUseCaseInteractor;
    private final LoginUserDataAccessInterface userDataAccessObject;

    public DeleteWatchlistController(DeleteWatchlistInputBoundary deleteWatchlistUseCaseInteractor,
                                     LoginUserDataAccessInterface userDataAccessObject) {
        this.deleteWatchlistUseCaseInteractor = deleteWatchlistUseCaseInteractor;
        this.userDataAccessObject = userDataAccessObject;
    }

    public void execute(String ticker) {
        // Fetch the currently logged-in user from DAO session
        String currentUsername = userDataAccessObject.getCurrentUser();
        User currentUser = userDataAccessObject.get(currentUsername);

        DeleteWatchlistInputData inputData = new DeleteWatchlistInputData(ticker, currentUser);
        deleteWatchlistUseCaseInteractor.execute(inputData);
    }
}