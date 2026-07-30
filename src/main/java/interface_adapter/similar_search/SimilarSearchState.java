package interface_adapter.similar_search;

import use_case.similar_search.SimilarSearchOutputData;

public class SimilarSearchState {

    private SimilarSearchOutputData[] similarSearchOutputData;
    private boolean useCaseFailed;

    public SimilarSearchOutputData[] getSimilarSearchOutputData() {
        return similarSearchOutputData;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    public void setUseCaseFailed(boolean useCaseFailed) {
        this.useCaseFailed = useCaseFailed;
    }

    public void setSimilarSearchOutputData(SimilarSearchOutputData[] similarSearchOutputData) {
        this.similarSearchOutputData = similarSearchOutputData;
    }
}
