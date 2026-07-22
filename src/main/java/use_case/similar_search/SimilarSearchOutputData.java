package use_case.similar_search;

import entity.Stock;

/**
 * Output Data for the Similar Search Use Case.
 */
public class SimilarSearchOutputData {

    private final Stock[] similarStocks;
    private final boolean useCaseFailed;

    public SimilarSearchOutputData(Stock[] similarStocks, boolean useCaseFailed) {
        this.similarStocks = similarStocks;
        this.useCaseFailed = useCaseFailed;
    }

    public Stock[] getSimilarStocks() {
        return this.similarStocks;
    }

    public boolean getUseCaseFailed() {
        return this.useCaseFailed;
    }
}
