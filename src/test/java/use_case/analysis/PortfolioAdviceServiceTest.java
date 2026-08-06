package use_case.analysis;

import entity.RiskLevel;
import entity.RiskProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioAdviceServiceTest {

    @Test
    void testGetSharpeAdvice() {
        // Test null
        assertTrue(PortfolioAdviceService.getSharpeAdvice(null).contains("unavailable"));

        // Test thresholds
        assertTrue(PortfolioAdviceService.getSharpeAdvice(25.0).contains("Exceptional"));
        assertTrue(PortfolioAdviceService.getSharpeAdvice(20.0).contains("Strong"));
        assertTrue(PortfolioAdviceService.getSharpeAdvice(15.0).contains("Good"));
        assertTrue(PortfolioAdviceService.getSharpeAdvice(10.0).contains("Sub-optimal"));
        assertTrue(PortfolioAdviceService.getSharpeAdvice(5.0).contains("Critical deficiency"));
    }

    @Test
    void testGetRiskAlignmentAdvice() {
        RiskProfile profile = new RiskProfile();
        profile.setRiskLevel(RiskLevel.MODERATE);

        // Test nulls
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(null, profile).contains("incomplete"));
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(25.0, null).contains("incomplete"));

        // Test thresholds
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(25.0, profile).contains("Perfect"));
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(20.0, profile).contains("Minor deviation"));
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(15.0, profile).contains("Moderate misalignment"));
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(10.0, profile).contains("High misalignment"));
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(5.0, profile).contains("Significant risk mismatch"));
        assertTrue(PortfolioAdviceService.getRiskAlignmentAdvice(2.0, profile).contains("Severe misalignment"));
    }

    @Test
    void testGetDiversificationAdvice() {
        // Test null
        assertTrue(PortfolioAdviceService.getDiversificationAdvice(null).contains("unavailable"));

        // Test thresholds
        assertTrue(PortfolioAdviceService.getDiversificationAdvice(25.0).contains("Outstanding"));
        assertTrue(PortfolioAdviceService.getDiversificationAdvice(20.0).contains("High diversification"));
        assertTrue(PortfolioAdviceService.getDiversificationAdvice(15.0).contains("Good diversification"));
        assertTrue(PortfolioAdviceService.getDiversificationAdvice(10.0).contains("Moderate diversification"));
        assertTrue(PortfolioAdviceService.getDiversificationAdvice(5.0).contains("Low diversification"));
        assertTrue(PortfolioAdviceService.getDiversificationAdvice(2.0).contains("No diversification"));
    }

    @Test
    void testGetNewsAdviceAllTiers() {
        // Null safety check
        assertNotNull(PortfolioAdviceService.getNewsAdvice(null));

        // Strong Bullish (>= 20.0)
        String bullish = PortfolioAdviceService.getNewsAdvice(22.5);
        assertNotNull(bullish);
        assertTrue(bullish.contains("Strong Bullish Sentiment"));

        // Moderately Bullish (15.0 - 19.9)
        String modBullish = PortfolioAdviceService.getNewsAdvice(17.0);
        assertNotNull(modBullish);
        assertTrue(modBullish.contains("Moderately Bullish Sentiment"));

        // Neutral (10.0 - 14.9)
        String neutral = PortfolioAdviceService.getNewsAdvice(12.5);
        assertNotNull(neutral);
        assertTrue(neutral.contains("Neutral Sentiment"));

        // Cautious / Bearish (5.0 - 9.9)
        String bearish = PortfolioAdviceService.getNewsAdvice(7.5);
        assertNotNull(bearish);
        assertTrue(bearish.contains("Cautious/Bearish Sentiment"));

        // Strong Bearish (< 5.0)
        String strongBearish = PortfolioAdviceService.getNewsAdvice(2.0);
        assertNotNull(strongBearish);
        assertTrue(strongBearish.contains("Strong Bearish Sentiment"));
    }
}