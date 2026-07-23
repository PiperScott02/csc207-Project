package interface_adapter.similar_search;

import interface_adapter.ViewModel;

public class SimilarSearchViewModel extends ViewModel<SimilarSearchState> {

    public SimilarSearchViewModel() {
        super("similar search");
        setState(new SimilarSearchState());
    }

}
