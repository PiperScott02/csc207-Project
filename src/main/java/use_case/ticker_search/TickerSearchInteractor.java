package use_case.ticker_search;

import entity.Stock;
import use_case.TickerSearchDataAccessInterface;

/**
 * The Ticker Search Interactor.
 */
public class TickerSearchInteractor implements TickerSearchInputBoundary {

    private final TickerSearchDataAccessInterface tickerSearchDataAccessObject;
    private final TickerSearchOutputBoundary tickerSearchPresenter;

    public TickerSearchInteractor(TickerSearchDataAccessInterface tickerSearchDataAccessObject,
                                  TickerSearchOutputBoundary tickerSearchOutputBoundary) {
        this.tickerSearchDataAccessObject = tickerSearchDataAccessObject;
        this.tickerSearchPresenter = tickerSearchOutputBoundary;
    }

    /**
     * Sends presenter information of stock information for the ticker from tickerSearchInputData, or tell presenter
     * what error occurred in process of trying to do so.
     * @param tickerSearchInputData input data for ticker search
     */
    @Override
    public void execute(TickerSearchInputData tickerSearchInputData) {
        final String tickerSymbol = tickerSearchInputData.getTickerSymbol();

        if (tickerSymbol == null || tickerSymbol.trim().isEmpty()) {
            tickerSearchPresenter.prepareFailView("Please Enter a Stock Ticker.");
            return;
        }

        final String cleanTickerSymbol = tickerSymbol.trim().toUpperCase();
        final Stock tickerStock;

        try {
            tickerStock = tickerSearchDataAccessObject.createBasicStock(cleanTickerSymbol);
        } catch (RuntimeException e) {
            tickerSearchPresenter.prepareFailView(e.getMessage());
            return;
        }

        if (tickerStock == null) {
            tickerSearchPresenter.prepareFailView("No Exact Match for : " + cleanTickerSymbol);
            return;
        }

        final TickerSearchOutputData tickerSearchOutputData =
                new TickerSearchOutputData(tickerStock.getTickerSymbol(),
                        tickerStock.getCompanyName(),
                        tickerStock.getCountry(),
                        tickerStock.getIndustry(),
                        tickerStock.getPreviousClose());
        tickerSearchPresenter.prepareSuccessView(tickerSearchOutputData);
    }
}
