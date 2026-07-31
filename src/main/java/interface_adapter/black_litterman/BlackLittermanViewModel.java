package interface_adapter.black_litterman;

import interface_adapter.ViewModel;

/**ViewModel for managing and observing Black-Litterman model interactions in the user interface.**/
public class BlackLittermanViewModel extends ViewModel<BlackLittermanState> {

    /**
     * Constructs a new BlackLittermanViewModel with the default view name and initial state.
     */
    public BlackLittermanViewModel() {
        super("Black-Litterman view");
        setState(new BlackLittermanState());
    }
}