package use_case.similar_search;

/**
 * The input data for the Similar Search Use Case.
 */
public class SimilarSearchInputData {

    private final String tickerSymbol;

    public SimilarSearchInputData(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }
}
