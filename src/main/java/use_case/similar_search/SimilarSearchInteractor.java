package use_case.similar_search;

import entity.Stock;
import use_case.StockDailyDataAccessInterface;

import java.io.IOException;
import java.util.List;

/**
 * The Similar Search Interactor.
 */
public class SimilarSearchInteractor implements SimilarSearchInputBoundary{

    private final SimilarSearchDataAccessInterface similarSearchDataAccessObject;
    private final StockDailyDataAccessInterface stockDailyDataAccessObject;
    private final SimilarSearchOutputBoundary similarSearchPresenter;

    public SimilarSearchInteractor(SimilarSearchDataAccessInterface similarSearchAccessInterface,
                                   StockDailyDataAccessInterface stockDailyDataAccessInterface,
                                   SimilarSearchOutputBoundary similarSearchOutputBoundary) {
        this.similarSearchDataAccessObject = similarSearchAccessInterface;
        this.stockDailyDataAccessObject = stockDailyDataAccessInterface;
        this.similarSearchPresenter = similarSearchOutputBoundary;
    }

    /*
     * Returns a list of Stock objects that AlphaVantage API listed as similar to tickerSymbol.
     */
    @Override
    public void execute(SimilarSearchInputData similarSearchInputData) throws IOException, InterruptedException {
        final String tickerSymbol = similarSearchInputData.getTickerSymbol();
        final String[] similarCompanyNames = similarSearchDataAccessObject.similarNames(tickerSymbol);

        if (similarCompanyNames == null || similarCompanyNames.length == 0) {
            similarSearchPresenter.prepareFailView("No Similar Items.");
        }
        else {
            Stock[] similarStocks = new Stock[similarCompanyNames.length];
            for (int i = 0; i < similarCompanyNames.length; i++) {
                similarStocks[i] = stockDailyDataAccessObject.createStockAndHistory(similarCompanyNames[i]);
            }

            SimilarSearchOutputData[] similarSearchOutputList =
                    new SimilarSearchOutputData[similarCompanyNames.length];
            for (int i = 0; i < similarCompanyNames.length; i++) {
                similarSearchOutputList[i] =
                        new SimilarSearchOutputData(
                                similarStocks[i].getTickerSymbol(),
                                similarStocks[i].getCompanyName(),
                                null,
                                null,
                                similarStocks[i].getPreviousClose(),
                                false);
            }

            similarSearchPresenter.prepareSuccessView(similarSearchOutputList);
        }
    }
}
