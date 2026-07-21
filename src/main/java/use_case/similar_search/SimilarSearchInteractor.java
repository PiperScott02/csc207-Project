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
        final List<String> similarCompanyNames = similarSearchDataAccessObject.similarNames(tickerSymbol);

        if (similarCompanyNames.isEmpty()) {
            similarSearchPresenter.prepareFailView("No Similar Items.");
        }
        else {
            Stock[] similarStocks = new Stock[similarCompanyNames.size()];

            for (int i = 0; i < similarCompanyNames.size(); i++) {
                similarStocks[i] = stockDailyDataAccessObject.createStockAndHistory(similarCompanyNames.get(i));
            }

            final SimilarSearchOutputData similarSearchOutputData = new SimilarSearchOutputData(similarStocks, false);
            similarSearchPresenter.prepareSuccessView(similarSearchOutputData);
        }
    }
}
