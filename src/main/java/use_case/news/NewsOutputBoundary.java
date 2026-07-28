package use_case.news;

/**
 * The output boundary for the news use case.
 */
public interface NewsOutputBoundary {

    /**
     * Prepares a successful news search result.
     *
     * @param outputData the completed news result
     */
    void prepareSuccessView(NewsOutputData outputData);

    /**
     * Prepares an error message.
     *
     * @param errorMessage the error to display
     */
    void prepareFailView(String errorMessage);
}