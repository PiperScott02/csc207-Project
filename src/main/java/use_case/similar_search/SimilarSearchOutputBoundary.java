package use_case.similar_search;

/**
 * The Output Boundary for the Similar Search Use Case.
 */
public interface SimilarSearchOutputBoundary {

    /**
     * Prepares the success view for the Similar Search Use Case.
     * @param similarSearchOutputList list of similar search output data to be displayed
     */
    void prepareSuccessView(SimilarSearchOutputData similarSearchOutputList);

    /**
     * Prepares the failure view for the Similar Search Use Case.
     * @param errorMessage the message to display for the given error
     */
    void prepareFailView(String errorMessage);

}
