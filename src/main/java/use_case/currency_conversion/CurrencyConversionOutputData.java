package use_case.currency_conversion;

import java.math.BigDecimal;

/**
 * Output data after converting a portfolio value.
 */
public class CurrencyConversionOutputData {

    private final BigDecimal originalValue;
    private final BigDecimal convertedValue;
    private final BigDecimal exchangeRate;
    private final String fromCurrency;
    private final String toCurrency;

    /**
     * Creates currency-conversion output.
     *
     * @param originalValue original portfolio value
     * @param convertedValue converted portfolio value
     * @param exchangeRate rate used
     * @param fromCurrency source currency
     * @param toCurrency target currency
     */
    public CurrencyConversionOutputData(
            BigDecimal originalValue,
            BigDecimal convertedValue,
            BigDecimal exchangeRate,
            String fromCurrency,
            String toCurrency) {

        this.originalValue = originalValue;
        this.convertedValue = convertedValue;
        this.exchangeRate = exchangeRate;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public BigDecimal getOriginalValue() {
        return originalValue;
    }

    public BigDecimal getConvertedValue() {
        return convertedValue;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }
}