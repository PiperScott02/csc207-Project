package use_case.similar_search;

/*
 * The input data for the Similar Search Use Case.
 */
public class SimilarSearchInputData {

    private String tickerSymbol;

    public SimilarSearchInputData(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }
}
