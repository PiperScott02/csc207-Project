package data_access;

import entity.NewsArticle;
import entity.NewsSentiment;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.news.NewsDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves stock news from the Alpha Vantage News Sentiment API.
 */
public class AlphaVantageNewsDataAccessObject
        implements NewsDataAccessInterface {

    private static final String BASE_URL =
            "https://www.alphavantage.co/query"
                    + "?function=NEWS_SENTIMENT"
                    + "&tickers=%s"
                    + "&limit=20"
                    + "&apikey=%s";

    private static final double BULLISH_THRESHOLD = 0.15;
    private static final double BEARISH_THRESHOLD = -0.15;

    private final String apiKey;
    private final HttpClient httpClient;

    /**
     * Creates the Alpha Vantage news DAO.
     *
     * @param apiKey the Alpha Vantage API key
     */
    public AlphaVantageNewsDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public List<NewsArticle> getNews(String ticker) {
        final List<NewsArticle> articles = new ArrayList<>();

        try {
            final String url = String.format(BASE_URL, ticker, apiKey);

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            final HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Alpha Vantage returned status code "
                                + response.statusCode()
                );
            }

            final JSONObject responseBody =
                    new JSONObject(response.body());

            checkForApiError(responseBody);

            final JSONArray feed =
                    responseBody.optJSONArray("feed");

            if (feed == null) {
                return articles;
            }

            for (int index = 0; index < feed.length(); index++) {
                final JSONObject articleJson =
                        feed.getJSONObject(index);

                final double sentimentScore =
                        findTickerSentimentScore(articleJson, ticker);

                /*
                 * A score of NaN means that the article does not contain
                 * sentiment information specifically for this ticker.
                 */
                if (!Double.isNaN(sentimentScore)) {
                    final NewsArticle article = createNewsArticle(
                            articleJson,
                            sentimentScore
                    );

                    articles.add(article);
                }
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(
                    "Could not connect to Alpha Vantage.",
                    exception
            );
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "The news request was interrupted.",
                    exception
            );
        }

        return articles;
    }

    /**
     * Finds the sentiment score belonging to the requested ticker.
     *
     * @param articleJson the JSON for one news article
     * @param ticker the requested stock ticker
     * @return the ticker's sentiment score, or NaN when not found
     */
    private double findTickerSentimentScore(
            JSONObject articleJson,
            String ticker) {

        final JSONArray tickerSentiments =
                articleJson.optJSONArray("ticker_sentiment");

        if (tickerSentiments == null) {
            return Double.NaN;
        }

        for (int index = 0;
             index < tickerSentiments.length();
             index++) {

            final JSONObject tickerData =
                    tickerSentiments.getJSONObject(index);

            final String articleTicker =
                    tickerData.optString("ticker");

            if (articleTicker.equalsIgnoreCase(ticker)) {
                final String scoreText =
                        tickerData.optString(
                                "ticker_sentiment_score",
                                "0.0"
                        );

                return Double.parseDouble(scoreText);
            }
        }

        return Double.NaN;
    }

    /**
     * Converts article JSON into a NewsArticle entity.
     *
     * @param articleJson the JSON for one article
     * @param sentimentScore the sentiment score for the requested ticker
     * @return the completed NewsArticle
     */
    private NewsArticle createNewsArticle(
            JSONObject articleJson,
            double sentimentScore) {

        final String title =
                articleJson.optString("title", "No title");

        final String summary =
                articleJson.optString("summary", "No summary available.");

        final String url =
                articleJson.optString("url", "");

        final String source =
                articleJson.optString("source", "Unknown source");

        final NewsSentiment sentiment =
                classifySentiment(sentimentScore);

        return new NewsArticle(
                title,
                summary,
                url,
                source,
                sentimentScore,
                sentiment
        );
    }

    /**
     * Converts a numerical score into one of our sentiment categories.
     *
     * @param score the Alpha Vantage sentiment score
     * @return bearish, neutral, or bullish
     */
    private NewsSentiment classifySentiment(double score) {
        if (score >= BULLISH_THRESHOLD) {
            return NewsSentiment.BULLISH;
        }
        else if (score <= BEARISH_THRESHOLD) {
            return NewsSentiment.BEARISH;
        }
        else {
            return NewsSentiment.NEUTRAL;
        }
    }

    /**
     * Detects common Alpha Vantage error responses.
     *
     * @param responseBody the complete API response
     */
    private void checkForApiError(JSONObject responseBody) {
        if (responseBody.has("Error Message")) {
            throw new RuntimeException(
                    responseBody.getString("Error Message")
            );
        }

        if (responseBody.has("Information")) {
            throw new RuntimeException(
                    responseBody.getString("Information")
            );
        }

        if (responseBody.has("Note")) {
            throw new RuntimeException(
                    responseBody.getString("Note")
            );
        }
    }
}