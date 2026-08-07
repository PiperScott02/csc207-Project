package entity;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewsSentimentCalculatorTest {

    private static final double TOLERANCE = 0.0001;

    @Test
    void testWeightedAverageGivesMoreInfluenceToRelevantArticle() {
        final List<NewsArticle> articles = List.of(
                createArticle(0.60, 0.90),
                createArticle(-0.60, 0.10)
        );

        final double result =
                NewsSentimentCalculator.calculateRawSentiment(articles);

        assertEquals(0.48, result, TOLERANCE);
    }

    @Test
    void testSimpleAverageUsedWhenAllRelevanceScoresAreZero() {
        final List<NewsArticle> articles = List.of(
                createArticle(0.60, 0.0),
                createArticle(-0.20, 0.0)
        );

        final double result =
                NewsSentimentCalculator.calculateRawSentiment(articles);

        assertEquals(0.20, result, TOLERANCE);
    }

    @Test
    void testNullAndEmptyListsReturnNeutralScore() {
        assertEquals(
                0.0,
                NewsSentimentCalculator.calculateRawSentiment(null),
                TOLERANCE
        );
        assertEquals(
                0.0,
                NewsSentimentCalculator.calculateRawSentiment(
                        Collections.emptyList()
                ),
                TOLERANCE
        );
    }

    private NewsArticle createArticle(
            double sentimentScore,
            double relevanceScore) {

        return new NewsArticle(
                "Test article",
                "Test summary",
                "https://example.com",
                "Test source",
                sentimentScore,
                relevanceScore,
                NewsSentiment.NEUTRAL
        );
    }
}
