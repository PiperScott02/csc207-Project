package interface_adapter.currency_conversion;

import interface_adapter.ViewModel;

/**
 * View model for portfolio currency conversion.
 */
public class CurrencyConversionViewModel
        extends ViewModel<CurrencyConversionState> {

    public CurrencyConversionViewModel() {
        super("currency conversion");
        setState(new CurrencyConversionState());
    }
}