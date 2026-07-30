package use_case.ticker_search;

import data_access.stock_daily.StockService;
import entity.Stock;
import use_case.StockDailyDataAccessInterface;

import java.io.IOException;

/**
 * The Ticker Search Interactor.
 */
public class TickerSearchInteractor implements TickerSearchInputBoundary {

    private final StockDailyDataAccessInterface stockDailyDataAccessObject;
    private final TickerSearchOutputBoundary tickerSearchOutputPresenter;

    public TickerSearchInteractor(StockDailyDataAccessInterface stockDailyDataAccessObject,
                                  TickerSearchOutputBoundary tickerSearchOutputBoundary) {
        this.stockDailyDataAccessObject = stockDailyDataAccessObject;
        this.tickerSearchOutputPresenter = tickerSearchOutputBoundary;
    }

    @Override
    public void execute(TickerSearchInputData tickerSearchInputData) throws IOException, InterruptedException {
        final String tickerSymbol = tickerSearchInputData.getTickerSymbol();
        try {
            final Stock tickerStock = stockDailyDataAccessObject.createStockAndHistory(tickerSymbol);
            final TickerSearchOutputData tickerSearchOutputData =
                    new TickerSearchOutputData(tickerStock.getTickerSymbol(),
                            tickerStock.getCompanyName(),
                            null,
                            null,
                            tickerStock.getPreviousClose(),
                            false);
            tickerSearchOutputPresenter.prepareSuccessView(tickerSearchOutputData);
        } catch (StockService.InvalidInputToAPI e) {
            tickerSearchOutputPresenter.prepareFailView("Invalid Input To API");
        }
    }
}
