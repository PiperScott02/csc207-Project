package app;

import interface_adapter.delete_holding.DeleteHoldingController;
import interface_adapter.delete_holding.DeleteHoldingPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.delete_holding.DeleteHoldingUserDataAccessInterface;
import use_case.delete_holding.DeleteHoldingInputBoundary;
import use_case.delete_holding.DeleteHoldingInteractor;
import use_case.delete_holding.DeleteHoldingOutputBoundary;

public class DeleteHoldingUseCaseFactory {

    private DeleteHoldingUseCaseFactory() {}

    public static DeleteHoldingController create(
            LoggedInViewModel loggedInViewModel,
            DeleteHoldingUserDataAccessInterface userDataAccessObject) {

        DeleteHoldingOutputBoundary outputBoundary = new DeleteHoldingPresenter(loggedInViewModel);
        DeleteHoldingInputBoundary interactor = new DeleteHoldingInteractor(userDataAccessObject, outputBoundary);

        return new DeleteHoldingController(interactor);
    }
}