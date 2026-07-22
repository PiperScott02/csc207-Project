package use_case.similar_search;

import java.io.IOException;

/**
 * Input Boundary for actions relating to finding stocks similar to keywords.
 */
public interface SimilarSearchInputBoundary {

    /**
     * Executes the similar search use case.
     * @param similarSearchInputData
     * @throws IOException
     * @throws InterruptedException
     */
    void execute(SimilarSearchInputData similarSearchInputData) throws IOException, InterruptedException;

}
