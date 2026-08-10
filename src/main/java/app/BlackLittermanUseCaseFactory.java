package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.black_litterman.BlackLittermanPresenter;
import interface_adapter.black_litterman.BlackLittermanViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.analysis.BlackLittermanService;
import use_case.black_litterman.BlackLittermanDataAccessInterface;
import use_case.black_litterman.BlackLittermanInputBoundary;
import use_case.black_litterman.BlackLittermanInteractor;
import use_case.black_litterman.BlackLittermanOutputBoundary;
import view.BlackLittermanView;

public final class BlackLittermanUseCaseFactory {

    private BlackLittermanUseCaseFactory() {
    }

    /**
     * Creates and returns a BlackLittermanView with its associated controller wired up.
     *
     * @param viewManagerModel         the view manager model for screen navigation
     * @param blackLittermanViewModel  the view model containing the black-litterman state
     * @param dataAccessInterface      the data access interface
     * @param blackLittermanService    the service handling the quantitative matrix math
     * @param loggedInViewModel        the logged-in view model for sidebar session data
     * @return a fully constructed BlackLittermanView
     */
    public static BlackLittermanView create(
            ViewManagerModel viewManagerModel,
            BlackLittermanViewModel blackLittermanViewModel,
            BlackLittermanDataAccessInterface dataAccessInterface,
            BlackLittermanService blackLittermanService,
            LoggedInViewModel loggedInViewModel) {

        final BlackLittermanController blackLittermanController =
                createBlackLittermanUseCase(viewManagerModel, blackLittermanViewModel, dataAccessInterface, blackLittermanService);

        return new BlackLittermanView(viewManagerModel, blackLittermanViewModel, blackLittermanController, loggedInViewModel);
    }

    /**
     * Helper method to wire up the controller, interactor, and presenter layers.
     */
    static BlackLittermanController createBlackLittermanUseCase(
            ViewManagerModel viewManagerModel,
            BlackLittermanViewModel blackLittermanViewModel,
            BlackLittermanDataAccessInterface dataAccessInterface,
            BlackLittermanService blackLittermanService) {

        final BlackLittermanOutputBoundary presenter =
                new BlackLittermanPresenter(viewManagerModel, blackLittermanViewModel);

        final BlackLittermanInputBoundary interactor =
                new BlackLittermanInteractor(
                        dataAccessInterface,
                        blackLittermanService,
                        presenter
                );

        return new BlackLittermanController(interactor);
    }
}