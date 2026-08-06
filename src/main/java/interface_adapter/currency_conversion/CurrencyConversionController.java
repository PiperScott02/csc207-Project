package interface_adapter.currency_conversion;

import java.math.BigDecimal;

import use_case.currency_conversion.CurrencyConversionInputBoundary;
import use_case.currency_conversion.CurrencyConversionInputData;

/**
 * Controller for portfolio currency conversion.
 */
public class CurrencyConversionController {

    private final CurrencyConversionInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor currency conversion use case
     */
    public CurrencyConversionController(
            CurrencyConversionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Converts a portfolio value between currencies.
     *
     * @param portfolioValue value to convert
     * @param fromCurrency source currency
     * @param toCurrency target currency
     */
    public void execute(
            BigDecimal portfolioValue,
            String fromCurrency,
            String toCurrency) {

        final CurrencyConversionInputData inputData =
                new CurrencyConversionInputData(
                        portfolioValue,
                        fromCurrency,
                        toCurrency
                );

        interactor.execute(inputData);
    }
}