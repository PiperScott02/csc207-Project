package use_case.currency_conversion;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Handles portfolio-value currency conversion.
 */
public class CurrencyConversionInteractor
        implements CurrencyConversionInputBoundary {

    private static final int MONEY_SCALE = 2;

    private final CurrencyConversionDataAccessInterface
            currencyDataAccessObject;

    private final CurrencyConversionOutputBoundary presenter;

    /**
     * Creates the currency-conversion interactor.
     *
     * @param currencyDataAccessObject exchange-rate data access
     * @param presenter output presenter
     */
    public CurrencyConversionInteractor(
            CurrencyConversionDataAccessInterface
                    currencyDataAccessObject,
            CurrencyConversionOutputBoundary presenter) {

        this.currencyDataAccessObject =
                currencyDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(CurrencyConversionInputData inputData) {
        final BigDecimal portfolioValue =
                inputData.getPortfolioValue();

        if (portfolioValue == null) {
            presenter.prepareFailView(
                    "Portfolio value is unavailable.");
            return;
        }

        if (portfolioValue.compareTo(BigDecimal.ZERO) < 0) {
            presenter.prepareFailView(
                    "Portfolio value cannot be negative.");
            return;
        }

        final String fromCurrency =
                inputData.getFromCurrency();

        final String toCurrency =
                inputData.getToCurrency();

        if (fromCurrency == null || toCurrency == null) {
            presenter.prepareFailView(
                    "Please select valid currencies.");
            return;
        }

        try {
            final BigDecimal exchangeRate;

            if (fromCurrency.equals(toCurrency)) {
                exchangeRate = BigDecimal.ONE;
            }
            else {
                exchangeRate =
                        currencyDataAccessObject.getExchangeRate(
                                fromCurrency,
                                toCurrency
                        );
            }

            final BigDecimal convertedValue =
                    portfolioValue
                            .multiply(exchangeRate)
                            .setScale(
                                    MONEY_SCALE,
                                    RoundingMode.HALF_UP
                            );

            final CurrencyConversionOutputData outputData =
                    new CurrencyConversionOutputData(
                            portfolioValue,
                            convertedValue,
                            exchangeRate,
                            fromCurrency,
                            toCurrency
                    );

            presenter.prepareSuccessView(outputData);
        }
        catch (Exception exception) {
            presenter.prepareFailView(
                    "Unable to retrieve the exchange rate.");
        }
    }
}