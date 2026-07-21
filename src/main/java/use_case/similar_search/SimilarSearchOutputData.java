package use_case.similar_search;

import entity.Stock;

import java.util.List;

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
