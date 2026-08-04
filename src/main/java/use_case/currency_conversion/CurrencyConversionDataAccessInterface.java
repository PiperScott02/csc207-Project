package use_case.currency_conversion;

import java.math.BigDecimal;

/**
 * Provides exchange-rate data.
 */
public interface CurrencyConversionDataAccessInterface {

    /**
     * Returns the exchange rate between two currencies.
     *
     * @param fromCurrency source currency code
     * @param toCurrency target currency code
     * @return exchange rate
     * @throws Exception if the rate cannot be retrieved
     */
    BigDecimal getExchangeRate(
            String fromCurrency,
            String toCurrency) throws Exception;
}