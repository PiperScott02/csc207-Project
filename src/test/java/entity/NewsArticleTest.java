package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NewsArticleTest {

    @Test
    void testGettersReturnConstructorValues() {
        final NewsArticle article = new NewsArticle(
                "Alphabet announces a new product",
                "The company announced a new product today.",
                "https://example.com/article",
                "Example Finance",
                0.42,
                0.85,
                NewsSentiment.BULLISH
        );

        assertAll(
                () -> assertEquals(
                        "Alphabet announces a new product",
                        article.getTitle()
                ),
                () -> assertEquals(
                        "The company announced a new product today.",
                        article.getSummary()
                ),
                () -> assertEquals(
                        "https://example.com/article",
                        article.getUrl()
                ),
                () -> assertEquals(
                        "Example Finance",
                        article.getSource()
                ),
                () -> assertEquals(0.42, article.getSentimentScore()),
                () -> assertEquals(0.85, article.getRelevanceScore()),
                () -> assertEquals(
                        NewsSentiment.BULLISH,
                        article.getSentiment()
                )
        );
    }
}
