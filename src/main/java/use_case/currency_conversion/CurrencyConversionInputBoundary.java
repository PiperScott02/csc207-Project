package use_case.currency_conversion;

/**
 * Input boundary for converting a portfolio value between currencies.
 */
public interface CurrencyConversionInputBoundary {

    /**
     * Converts the given portfolio value.
     *
     * @param inputData conversion request
     */
    void execute(CurrencyConversionInputData inputData);
}