package interface_adapter.currency_conversion;

import use_case.currency_conversion.CurrencyConversionOutputBoundary;
import use_case.currency_conversion.CurrencyConversionOutputData;

/**
 * Presenter for portfolio currency conversion.
 */
public class CurrencyConversionPresenter
        implements CurrencyConversionOutputBoundary {

    private final CurrencyConversionViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel currency conversion view model
     */
    public CurrencyConversionPresenter(
            CurrencyConversionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(
            CurrencyConversionOutputData outputData) {

        final CurrencyConversionState state =
                viewModel.getState();

        state.setOriginalValue(
                outputData.getOriginalValue()
        );

        state.setConvertedValue(
                outputData.getConvertedValue()
        );

        state.setExchangeRate(
                outputData.getExchangeRate()
        );

        state.setFromCurrency(
                outputData.getFromCurrency()
        );

        state.setToCurrency(
                outputData.getToCurrency()
        );

        state.setError("");

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final CurrencyConversionState state =
                viewModel.getState();

        state.setError(error);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}