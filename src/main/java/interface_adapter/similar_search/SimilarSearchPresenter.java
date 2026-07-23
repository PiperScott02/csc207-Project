package interface_adapter.similar_search;

import use_case.similar_search.SimilarSearchOutputBoundary;
import use_case.similar_search.SimilarSearchOutputData;

public class SimilarSearchPresenter implements SimilarSearchOutputBoundary {

    private final SimilarSearchViewModel similarSearchViewModel;

    public SimilarSearchPresenter(SimilarSearchViewModel similarSearchViewModel) {
        this.similarSearchViewModel = similarSearchViewModel;
    }

    @Override
    public void prepareSuccessView(SimilarSearchOutputData[] similarSearchOutputList) {
        final SimilarSearchState state = similarSearchViewModel.getState();
        state.setSimilarSearchOutputData(similarSearchOutputList);
        state.setUseCaseFailed(false);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SimilarSearchState state = similarSearchViewModel.getState();
        state.setUseCaseFailed(true);
    }

}
