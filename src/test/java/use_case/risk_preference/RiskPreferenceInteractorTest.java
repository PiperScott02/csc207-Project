package use_case.risk_preference;

import entity.CommonUser;
import entity.RiskLevel;
import entity.RiskProfile;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RiskPreferenceInteractorTest {

    private RiskPreferenceUserDataAccessInterface dao;
    private RiskPreferenceOutputBoundary presenter;
    private RiskPreferenceInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = mock(RiskPreferenceUserDataAccessInterface.class);
        presenter = mock(RiskPreferenceOutputBoundary.class);

        interactor = new RiskPreferenceInteractor(dao, presenter);
    }

    @Test
    void testSaveRiskPreferenceSuccess() {
        User user = new CommonUser("Selina", "123");

        when(dao.getCurrentUser()).thenReturn("Selina");
        when(dao.get("Selina")).thenReturn(user);

        RiskPreferenceInputData inputData =
                new RiskPreferenceInputData(RiskLevel.AGGRESSIVE);

        interactor.execute(inputData);

        verify(dao).save(user);
        verify(presenter).prepareSuccessView(any());

        assertEquals(
                RiskLevel.AGGRESSIVE,
                user.getRiskProfile().getRiskLevel()
        );
    }

    @Test
    void testNoRiskLevelFails() {
        RiskPreferenceInputData inputData =
                new RiskPreferenceInputData(null);

        interactor.execute(inputData);

        verify(presenter)
                .prepareFailView("Please select a risk level.");
    }

    @Test
    void testLoadRiskPreferenceSuccess() {
        User user = new CommonUser("Selina", "123");

        user.setRiskProfile(
                new RiskProfile(RiskLevel.CONSERVATIVE)
        );

        when(dao.getCurrentUser()).thenReturn("Selina");
        when(dao.get("Selina")).thenReturn(user);

        interactor.load();

        verify(presenter)
                .prepareSuccessView(any());
    }
}