package use_case.currency_conversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CurrencyConversionInteractorTest {

    private CurrencyConversionDataAccessInterface dao;
    private CurrencyConversionOutputBoundary presenter;
    private CurrencyConversionInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = mock(CurrencyConversionDataAccessInterface.class);
        presenter = mock(CurrencyConversionOutputBoundary.class);
        interactor = new CurrencyConversionInteractor(dao, presenter);
    }

    @Test
    void testCurrencyConversionSuccess() throws Exception {
        when(dao.getExchangeRate("USD", "CAD"))
                .thenReturn(new BigDecimal("1.35"));

        CurrencyConversionInputData inputData =
                new CurrencyConversionInputData(
                        new BigDecimal("100.00"),
                        "USD",
                        "CAD"
                );

        interactor.execute(inputData);

        verify(dao).getExchangeRate("USD", "CAD");
        verify(presenter)
                .prepareSuccessView(any(CurrencyConversionOutputData.class));
    }
}