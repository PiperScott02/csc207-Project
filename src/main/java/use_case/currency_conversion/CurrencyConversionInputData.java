package use_case.currency_conversion;

import java.math.BigDecimal;

/**
 * Input data for currency conversion.
 */
public class CurrencyConversionInputData {

    private final BigDecimal portfolioValue;
    private final String fromCurrency;
    private final String toCurrency;

    /**
     * Creates a currency-conversion request.
     *
     * @param portfolioValue original portfolio value
     * @param fromCurrency original currency code
     * @param toCurrency target currency code
     */
    public CurrencyConversionInputData(
            BigDecimal portfolioValue,
            String fromCurrency,
            String toCurrency) {

        this.portfolioValue = portfolioValue;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public BigDecimal getPortfolioValue() {
        return portfolioValue;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }
}