package use_case.similar_search;

public interface SimilarSearchOutputBoundary {

    void prepareSuccessView(SimilarSearchOutputData similarSearchOutputData);

    void prepareFailView(String errorMessage);

}
