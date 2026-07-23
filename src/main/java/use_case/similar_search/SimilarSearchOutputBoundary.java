package use_case.similar_search;

/**
 * The Output Boundary for the Similar Search Use Case.
 */
public interface SimilarSearchOutputBoundary {

    /**
     * Prepares the success view for the Similar Search Use Case.
     * @param similarSearchOutputList
     */
    public void prepareSuccessView(SimilarSearchOutputData[] similarSearchOutputList);

    /**
     * Prepares the failure view for the Similar Search Use Case.
     * @param errorMessage
     */
    public void prepareFailView(String errorMessage);

}
