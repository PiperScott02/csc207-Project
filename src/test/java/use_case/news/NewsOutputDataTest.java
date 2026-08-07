package use_case.news;

import entity.NewsArticle;
import entity.NewsSentiment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class NewsOutputDataTest {

    @Test
    void testGettersReturnOutputValues() {
        final List<NewsArticle> articles = List.of(
                new NewsArticle(
                        "Test article",
                        "Test summary",
                        "https://example.com",
                        "Test source",
                        0.25,
                        0.80,
                        NewsSentiment.BULLISH
                )
        );
        final NewsOutputData outputData = new NewsOutputData(
                "MSFT",
                articles,
                NewsSentiment.BULLISH
        );

        assertEquals("MSFT", outputData.getTicker());
        assertSame(articles, outputData.getArticles());
        assertEquals(
                NewsSentiment.BULLISH,
                outputData.getOverallSentiment()
        );
    }
}
