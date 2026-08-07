package use_case.login;

import entity.CommonUser;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginInteractorTest {

    private LoginUserDataAccessInterface dao;
    private LoginOutputBoundary presenter;
    private LoginInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = mock(LoginUserDataAccessInterface.class);
        presenter = mock(LoginOutputBoundary.class);
        interactor = new LoginInteractor(dao, presenter);
    }

    @Test
    void testLoginSuccess() {
        User user = new CommonUser("Selina", "123");

        when(dao.existsByName("Selina")).thenReturn(true);
        when(dao.get("Selina")).thenReturn(user);

        LoginInputData inputData =
                new LoginInputData("Selina", "123");

        interactor.execute(inputData);

        verify(dao).setCurrentUser("Selina");
        verify(presenter)
                .prepareSuccessView(any(LoginOutputData.class));
    }

    @Test
    void testIncorrectPasswordFails() {
        User user = new CommonUser("Selina", "123");

        when(dao.existsByName("Selina")).thenReturn(true);
        when(dao.get("Selina")).thenReturn(user);

        LoginInputData inputData =
                new LoginInputData("Selina", "wrong");

        interactor.execute(inputData);

        verify(presenter)
                .prepareFailView(
                        "Incorrect password for \"Selina\"."
                );

        verify(presenter, never())
                .prepareSuccessView(any());
    }
}