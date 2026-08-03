package use_case.similar_search;

import entity.Stock;
import use_case.TickerSearchDataAccessInterface;

import java.io.IOException;

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

    /*
     * Returns a list of Stock objects that AlphaVantage API listed as similar to tickerSymbol.
     */
    @Override
    public void execute(SimilarSearchInputData similarSearchInputData) throws IOException, InterruptedException {
        final String tickerSymbol = similarSearchInputData.getTickerSymbol();
        final String[][] similarStockInfo = similarSearchDataAccessObject.similarStockInfo(tickerSymbol);

        if (similarStockInfo == null || similarStockInfo.length == 0) {
            similarSearchPresenter.prepareFailView("No Similar Items.");
        }
        else {
            Stock[] similarStocks = new Stock[similarStockInfo.length];
            for (int i = 0; i < similarStockInfo.length; i++) {
                similarStocks[i] = tickerSearchDataAccessObject.createBasicStock(similarStockInfo[i][0]);
            }

            SimilarSearchOutputData[] similarSearchOutputList =
                    new SimilarSearchOutputData[similarStockInfo.length];
            for (int i = 0; i < similarStockInfo.length; i++) {
                similarSearchOutputList[i] =
                        new SimilarSearchOutputData(
                                similarStockInfo[i][0],
                                similarStockInfo[i][1],
                                similarStockInfo[i][2],
                                similarStocks[i].getIndustry(),
                                similarStocks[i].getPreviousClose(),
                                false);
            }

            similarSearchPresenter.prepareSuccessView(similarSearchOutputList);
        }
    }
}
