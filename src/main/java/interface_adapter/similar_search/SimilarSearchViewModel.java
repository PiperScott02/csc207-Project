package interface_adapter.similar_search;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Similar Search Use Case.
 */
public class SimilarSearchViewModel extends ViewModel<SimilarSearchState> {

    public SimilarSearchViewModel() {
        super("similar search");
        setState(new SimilarSearchState());
    }

}
