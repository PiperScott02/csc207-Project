package interface_adapter.currency_conversion;

import java.math.BigDecimal;

/**
 * Stores the data displayed for currency conversion.
 */
public class CurrencyConversionState {

    private BigDecimal originalValue = BigDecimal.ZERO;
    private BigDecimal convertedValue = BigDecimal.ZERO;
    private BigDecimal exchangeRate = BigDecimal.ONE;

    private String fromCurrency = "USD";
    private String toCurrency = "USD";

    private String error = "";

    public BigDecimal getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(BigDecimal originalValue) {
        this.originalValue = originalValue;
    }

    public BigDecimal getConvertedValue() {
        return convertedValue;
    }

    public void setConvertedValue(BigDecimal convertedValue) {
        this.convertedValue = convertedValue;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}