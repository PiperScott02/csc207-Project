package interface_adapter.news;

import use_case.news.NewsInputBoundary;
import use_case.news.NewsInputData;

/**
 * The controller for the stock news use case.
 */
public class NewsController {

    private final NewsInputBoundary newsInteractor;

    public NewsController(NewsInputBoundary newsInteractor) {
        this.newsInteractor = newsInteractor;
    }

    /**
     * Executes the stock news search.
     *
     * @param ticker the stock ticker entered by the user
     */
    public void execute(String ticker) {
        final NewsInputData inputData = new NewsInputData(ticker);
        newsInteractor.execute(inputData);
    }
}