package interface_adapter.similar_search;

import use_case.similar_search.SimilarSearchInputBoundary;
import use_case.similar_search.SimilarSearchInputData;

/**
 * Controller for Similar Search Use Case.
 */
public class SimilarSearchController {

    private final SimilarSearchInputBoundary similarSearchInteractor;

    public SimilarSearchController(SimilarSearchInputBoundary similarSearchInteractor) {
        this.similarSearchInteractor = similarSearchInteractor;
    }

    /**
     * Executes the Similar Search Use Case.
     * @param tickerSymbol the ticker symbol to find similar tickers for
     */
    public void execute(String tickerSymbol) {
        final SimilarSearchInputData similarSearchInputData = new SimilarSearchInputData(tickerSymbol);
        similarSearchInteractor.execute(similarSearchInputData);
    }

}