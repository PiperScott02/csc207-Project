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
        final String[] similarCompanyNames = similarSearchDataAccessObject.similarNames(tickerSymbol);

        if (similarCompanyNames == null || similarCompanyNames.length == 0) {
            similarSearchPresenter.prepareFailView("No Similar Items.");
        }
        else {
            Stock[] similarStocks = new Stock[similarCompanyNames.length];
            for (int i = 0; i < similarCompanyNames.length; i++) {
                similarStocks[i] = tickerSearchDataAccessObject.createBasicStock(similarCompanyNames[i]);
            }

            SimilarSearchOutputData[] similarSearchOutputList =
                    new SimilarSearchOutputData[similarCompanyNames.length];
            for (int i = 0; i < similarCompanyNames.length; i++) {
                similarSearchOutputList[i] =
                        new SimilarSearchOutputData(
                                similarStocks[i].getTickerSymbol(),
                                similarStocks[i].getCompanyName(),
                                similarStocks[i].getCountry(),
                                similarStocks[i].getIndustry(),
                                similarStocks[i].getPreviousClose(),
                                false);
            }

            similarSearchPresenter.prepareSuccessView(similarSearchOutputList);
        }
    }
}
