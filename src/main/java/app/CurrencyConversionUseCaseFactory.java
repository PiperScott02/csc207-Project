package app;

import data_access.CurrencyConversionDataAccessObject;
import interface_adapter.currency_conversion.CurrencyConversionController;
import interface_adapter.currency_conversion.CurrencyConversionPresenter;
import interface_adapter.currency_conversion.CurrencyConversionViewModel;
import use_case.currency_conversion.CurrencyConversionDataAccessInterface;
import use_case.currency_conversion.CurrencyConversionInputBoundary;
import use_case.currency_conversion.CurrencyConversionInteractor;
import use_case.currency_conversion.CurrencyConversionOutputBoundary;

/**
 * Factory for creating the Currency Conversion use case.
 */
public final class CurrencyConversionUseCaseFactory {

    private CurrencyConversionUseCaseFactory() {
        // Prevent instantiation.
    }

    /**
     * Creates the controller and wires the currency conversion use case.
     *
     * @param viewModel currency conversion view model
     * @return the currency conversion controller
     */
    public static CurrencyConversionController create(
            CurrencyConversionViewModel viewModel) {

        final CurrencyConversionDataAccessInterface dataAccessObject =
                new CurrencyConversionDataAccessObject();

        final CurrencyConversionOutputBoundary presenter =
                new CurrencyConversionPresenter(viewModel);

        final CurrencyConversionInputBoundary interactor =
                new CurrencyConversionInteractor(
                        dataAccessObject,
                        presenter
                );

        return new CurrencyConversionController(interactor);
    }
}