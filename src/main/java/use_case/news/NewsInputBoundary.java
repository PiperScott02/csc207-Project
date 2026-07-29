package use_case.news;

/**
 * The input boundary for the news use case.
 */
public interface NewsInputBoundary {

    /**
     * Executes the news search use case.
     *
     * @param newsInputData the input data containing the stock ticker
     */
    void execute(NewsInputData newsInputData);
}