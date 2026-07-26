package data_access;

import use_case.similar_search.SimilarSearchDataAccessInterface;

import java.io.IOException;
import java.util.List;

public class SimilarSearchDataAccessObject implements SimilarSearchDataAccessInterface {
    @Override
    public List<String> similarNames(String tickerSymbol) throws IOException, InterruptedException {
        return List.of();
    }
}
