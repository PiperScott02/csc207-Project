package use_case.currency_conversion;

/**
 * Output boundary for currency conversion.
 */
public interface CurrencyConversionOutputBoundary {

    /**
     * Prepares a successful conversion result.
     *
     * @param outputData conversion result
     */
    void prepareSuccessView(
            CurrencyConversionOutputData outputData);

    /**
     * Prepares a failed conversion result.
     *
     * @param error error message
     */
    void prepareFailView(String error);
}