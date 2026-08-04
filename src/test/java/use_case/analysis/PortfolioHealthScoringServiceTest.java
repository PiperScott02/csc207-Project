package use_case.analysis;

import entity.Portfolio;
import entity.RiskLevel;
import entity.RiskProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PortfolioHealthScoringServiceTest {

    private RiskProfile mockRiskProfile;
    private Portfolio mockPortfolio;

    @BeforeEach
    void setUp() {
        mockRiskProfile = Mockito.mock(RiskProfile.class);
        mockPortfolio = Mockito.mock(Portfolio.class);
    }

    // ==========================================
    // Sharpe Score Tests
    // ==========================================

    @Test
    void calculateSharpeScore() {
        assertEquals(0.00, PortfolioHealthScoringService.calculateSharpeScore(null));
        assertEquals(0.00, PortfolioHealthScoringService.calculateSharpeScore(-0.5));
        assertEquals(10.00, PortfolioHealthScoringService.calculateSharpeScore(0.5));
        assertEquals(15.00, PortfolioHealthScoringService.calculateSharpeScore(1.5));
        assertEquals(20.00, PortfolioHealthScoringService.calculateSharpeScore(2.5));
        assertEquals(25.00, PortfolioHealthScoringService.calculateSharpeScore(3.5));
    }

    // ==========================================
    // Risk Alignment Score Tests
    // ==========================================

    @Test
    void calculateRiskAlignmentScore() {
        // Null checks
        assertEquals(0.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(null, mockRiskProfile));
        assertEquals(0.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(1.0, null));

        // CONSERVATIVE Profile Tests
        when(mockRiskProfile.getRiskLevel()).thenReturn(RiskLevel.CONSERVATIVE);
        assertEquals(25.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(0.7, mockRiskProfile));
        assertEquals(20.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(0.9, mockRiskProfile));
        assertEquals(0.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(1.5, mockRiskProfile));

        // MODERATE Profile Tests
        when(mockRiskProfile.getRiskLevel()).thenReturn(RiskLevel.MODERATE);
        assertEquals(25.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(1.0, mockRiskProfile));
        assertEquals(25.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(1.1, mockRiskProfile));
        assertEquals(20.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(1.35, mockRiskProfile));

        // AGGRESSIVE Profile Tests
        when(mockRiskProfile.getRiskLevel()).thenReturn(RiskLevel.AGGRESSIVE);
        assertEquals(25.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(1.3, mockRiskProfile));
        assertEquals(20.0, PortfolioHealthScoringService.calculateRiskAlignmentScore(1.1, mockRiskProfile));
    }

    // ==========================================
    // Diversification Score Tests
    // ==========================================

    @Test
    void calculateDiversificationScore() {
        assertEquals(0.0, PortfolioHealthScoringService.calculateDiversificationScore(null));
        assertEquals(0.0, PortfolioHealthScoringService.calculateDiversificationScore(0.9));
        assertEquals(0.0, PortfolioHealthScoringService.calculateDiversificationScore(1.0));
        assertEquals(5.0, PortfolioHealthScoringService.calculateDiversificationScore(1.1));
        assertEquals(10.0, PortfolioHealthScoringService.calculateDiversificationScore(1.2));
        assertEquals(15.0, PortfolioHealthScoringService.calculateDiversificationScore(1.3));
        assertEquals(20.0, PortfolioHealthScoringService.calculateDiversificationScore(1.4));
        assertEquals(25.0, PortfolioHealthScoringService.calculateDiversificationScore(1.5));
    }

    // ==========================================
    // News Score Tests
    // ==========================================

    @Test
    void calculateNewsScore() {
        assertEquals(0.0, PortfolioHealthScoringService.calculateNewsScore());
    }

    // ==========================================
    // Total Health Score Tests
    // ==========================================

    @Test
    void calculatePortfolioHealthScore() {
        // Null portfolio edge case
        assertEquals(0.0, PortfolioHealthScoringService.calculatePortfolioHealthScore(null, mockRiskProfile));
    }
}