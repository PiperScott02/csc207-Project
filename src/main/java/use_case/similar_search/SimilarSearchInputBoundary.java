package use_case.similar_search;

/**
 * Input Boundary for actions relating to finding stocks similar to keywords.
 */
public interface SimilarSearchInputBoundary {

    /**
     * Executes the similar search use case.
     * @param similarSearchInputData input data for similar search
     */
    void execute(SimilarSearchInputData similarSearchInputData);

}
