package interface_adapter.similar_search;

import interface_adapter.ViewManagerModel;
import use_case.similar_search.SimilarSearchOutputBoundary;
import use_case.similar_search.SimilarSearchOutputData;

public class SimilarSearchPresenter implements SimilarSearchOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final SimilarSearchViewModel similarSearchViewModel;

    public SimilarSearchPresenter(ViewManagerModel viewManagerModel, SimilarSearchViewModel similarSearchViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.similarSearchViewModel = similarSearchViewModel;
    }

    @Override
    public void prepareSuccessView(SimilarSearchOutputData[] similarSearchOutputList) {
        final SimilarSearchState state = similarSearchViewModel.getState();
        state.setSimilarSearchOutputData(similarSearchOutputList);
        state.setUseCaseFailed(false);
        this.similarSearchViewModel.setState(state);
        similarSearchViewModel.firePropertyChanged("similar search");

        viewManagerModel.setState(similarSearchViewModel.getViewName());
        viewManagerModel.firePropertyChanged("similar search");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SimilarSearchState state = similarSearchViewModel.getState();
        state.setUseCaseFailed(true);
        this.similarSearchViewModel.setState(state);
        similarSearchViewModel.firePropertyChanged();

        viewManagerModel.setState(similarSearchViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

}
