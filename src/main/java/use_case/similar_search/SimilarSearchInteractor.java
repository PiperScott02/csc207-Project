package use_case.similar_search;

import entity.SimilarStocks;
import entity.Stock;
import use_case.TickerSearchDataAccessInterface;

/**
 * The Similar Search Interactor.
 */
public class SimilarSearchInteractor implements SimilarSearchInputBoundary{

    private final SimilarSearchDataAccessInterface similarSearchDataAccessObject;
    private final TickerSearchDataAccessInterface tickerSearchDataAccessObject;
    private final SimilarSearchOutputBoundary similarSearchPresenter;

    public SimilarSearchInteractor(SimilarSearchDataAccessInterface similarSearchAccessInterface,
                                   TickerSearchDataAccessInterface tickerSearchDataAccessObject,
                                   SimilarSearchOutputBoundary similarSearchOutputBoundary) {
        this.similarSearchDataAccessObject = similarSearchAccessInterface;
        this.tickerSearchDataAccessObject = tickerSearchDataAccessObject;
        this.similarSearchPresenter = similarSearchOutputBoundary;
    }

    /**
     * Calls presenter to update similar search area with list of Stock objects that are listed as similar to ticker
     * symbol in similarSearchInputData, or call fail view if a RuntimeException occurs when fetching this data.
     * @param similarSearchInputData contains the ticker symbol used to find similar tickers
     */
    @Override
    public void execute(SimilarSearchInputData similarSearchInputData) {
        final String tickerSymbol = similarSearchInputData.getTickerSymbol();

        if (tickerSymbol == null || tickerSymbol.trim().isEmpty()) {
            similarSearchPresenter.prepareFailView("Please Enter a Stock Ticker");
            return;
        }

        final String cleanTickerSymbol = tickerSymbol.trim().toUpperCase();
        final SimilarStocks similarStockInfo;

        try {
            similarStockInfo = similarSearchDataAccessObject.similarStockInfo(tickerSymbol);
        } catch (RuntimeException e) {
            similarSearchPresenter.prepareFailView(e.getMessage());
            return;
        }

        if (similarStockInfo == null || similarStockInfo.getLength() == 0) {
            similarSearchPresenter.prepareFailView("No Similar Items for " + cleanTickerSymbol);
            return;
        }

        Stock[] similarStocks = new Stock[similarStockInfo.getLength()];
        for (int i = 0; i < similarStockInfo.getLength(); i++) {
            try {
                similarStocks[i] = tickerSearchDataAccessObject
                        .createBasicStock(similarStockInfo.getSymbol(i));
            } catch (RuntimeException e) {
                similarSearchPresenter.prepareFailView(e.getMessage());
                return;
            }
        }

        SimilarSearchOutputData similarSearchOutputList = new SimilarSearchOutputData(similarStockInfo.getLength());
        for (int i = 0; i < similarStockInfo.getLength(); i++) {
            similarSearchOutputList.setSimilarSearchStockInfo(i,
                    similarStockInfo.getSymbol(i),
                    similarStockInfo.getName(i),
                    similarStockInfo.getRegion(i),
                    similarStocks[i].getIndustry(),
                    similarStocks[i].getPreviousClose());
        }

        similarSearchPresenter.prepareSuccessView(similarSearchOutputList);
    }
}
