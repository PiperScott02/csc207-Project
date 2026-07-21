package use_case.similar_search;

import java.io.IOException;

public interface SimilarSearchInputBoundary {

    void execute(SimilarSearchInputData similarSearchInputData) throws IOException, InterruptedException;

}
