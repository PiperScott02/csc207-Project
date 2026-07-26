package interface_adapter.similar_search;

import use_case.similar_search.SimilarSearchInputBoundary;
import use_case.similar_search.SimilarSearchInputData;

import java.io.IOException;

public class SimilarSearchController {

    private final SimilarSearchInputBoundary similarSearchInteractor;

    public SimilarSearchController(SimilarSearchInputBoundary similarSearchInteractor) {
        this.similarSearchInteractor = similarSearchInteractor;
    }

    public void execute(String tickerSymbol) throws InterruptedException, IOException {
        final SimilarSearchInputData similarSearchInputData = new SimilarSearchInputData(tickerSymbol);
        similarSearchInteractor.execute(similarSearchInputData);
    }

}