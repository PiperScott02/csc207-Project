package use_case.news;

import entity.NewsArticle;

import java.util.List;

/**
 * The data access interface for retrieving stock news.
 */
public interface NewsDataAccessInterface {

    /**
     * Retrieves news articles for the given stock ticker.
     *
     * @param ticker the stock ticker, such as AAPL
     * @return a list of news articles
     */
    List<NewsArticle> getNews(String ticker);
}