package entity;

import java.util.List;

public class NewsSentimentCalculator {

    /**
     * Calculates the raw relevance-weighted sentiment score between -1.0 and +1.0.
     */
    public static double calculateRawSentiment(List<NewsArticle> articles) {
        if (articles == null || articles.isEmpty()) {
            return 0.0; // Default to neutral
        }

        double weightedScoreTotal = 0.0;
        double totalRelevance = 0.0;
        double simpleScoreTotal = 0.0;

        for (NewsArticle article : articles) {
            double relevance = article.getRelevanceScore();
            double sentiment = article.getSentimentScore();

            simpleScoreTotal += sentiment;

            if (relevance > 0.0) {
                weightedScoreTotal += sentiment * relevance;
                totalRelevance += relevance;
            }
        }

        if (totalRelevance > 0.0) {
            return weightedScoreTotal / totalRelevance;
        } else {
            return simpleScoreTotal / articles.size();
        }
    }
}