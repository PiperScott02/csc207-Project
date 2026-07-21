package use_case.similar_search;

import java.io.IOException;
import java.util.List;

public interface SimilarSearchDataAccessInterface {

    List<String> similarNames(String tickerSymbol) throws IOException, InterruptedException;

}
