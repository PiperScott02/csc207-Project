package use_case.ticker_search;

import entity.Stock;
import use_case.TickerSearchDataAccessInterface;

import java.io.IOException;

/**
 * The Ticker Search Interactor.
 */
public class TickerSearchInteractor implements TickerSearchInputBoundary {

    private final TickerSearchDataAccessInterface tickerSearchDataAccessObject;
    private final TickerSearchOutputBoundary tickerSearchOutputPresenter;

    public TickerSearchInteractor(TickerSearchDataAccessInterface tickerSearchDataAccessObject,
                                  TickerSearchOutputBoundary tickerSearchOutputBoundary) {
        this.tickerSearchDataAccessObject = tickerSearchDataAccessObject;
        this.tickerSearchOutputPresenter = tickerSearchOutputBoundary;
    }

    @Override
    public void execute(TickerSearchInputData tickerSearchInputData) throws IOException, InterruptedException {
        final String tickerSymbol = tickerSearchInputData.getTickerSymbol();
        final Stock tickerStock = tickerSearchDataAccessObject.createBasicStock(tickerSymbol);

        if (tickerStock == null) { // TODO: this is meant to check if the tickerSymbol was actually valid
            tickerSearchOutputPresenter.prepareFailView("No Exact Match for Ticker Symbol");
        }
        else {
            final TickerSearchOutputData tickerSearchOutputData =
                    new TickerSearchOutputData(tickerStock.getTickerSymbol(),
                            tickerStock.getCompanyName(),
                            tickerStock.getCountry(),
                            tickerStock.getIndustry(),
                            tickerStock.getPreviousClose(),
                            false);
            tickerSearchOutputPresenter.prepareSuccessView(tickerSearchOutputData);
        }
    }
}
