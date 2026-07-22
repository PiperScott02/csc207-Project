package use_case.similar_search;

/**
 * The Output Boundary for the Similar Search Use Case.
 */
public interface SimilarSearchOutputBoundary {

    /**
     * Prepares the success view for the Similar Search Use Case.
     * @param similarSearchOutputData
     */
    void prepareSuccessView(SimilarSearchOutputData similarSearchOutputData);

    /**
     * Prepares the failure view for the Similar Search Use Case.
     * @param errorMessage
     */
    void prepareFailView(String errorMessage);

}
