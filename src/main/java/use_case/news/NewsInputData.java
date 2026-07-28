package use_case.news;

/**
 * The input data for the news use case.
 */
public class NewsInputData {

    private final String ticker;

    public NewsInputData(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }
}