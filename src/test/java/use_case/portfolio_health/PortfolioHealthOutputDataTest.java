package use_case.portfolio_health;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioHealthOutputDataTest {

    @Test
    void testGettersReturnCorrectValuesOnSuccess() {
        PortfolioHealthOutputData outputData = new PortfolioHealthOutputData(
                "Moderate",
                "85/100",
                "1.10",
                "0.03",
                "1.50",
                "Good risk-adjusted return.",
                "Portfolio risk aligns well with preference.",
                "Well diversified across sectors.",
                "Positive market sentiment detected.",
                false
        );

        assertEquals("Moderate", outputData.getRiskPreference());
        assertEquals("85/100", outputData.getPortfolioHealthScore());
        assertEquals("1.10", outputData.getBeta());
        assertEquals("0.03", outputData.getAlpha());
        assertEquals("1.50", outputData.getSharpeRatio());
        assertEquals("Good risk-adjusted return.", outputData.getSharpeAdvice());
        assertEquals("Portfolio risk aligns well with preference.", outputData.getRiskAlignmentAdvice());
        assertEquals("Well diversified across sectors.", outputData.getDiversificationAdvice());
        assertEquals("Positive market sentiment detected.", outputData.getNewsAdvice());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void testUseCaseFailedFlagWhenTrue() {
        PortfolioHealthOutputData outputData = new PortfolioHealthOutputData(
                "High",
                "0/100",
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                true
        );

        assertTrue(outputData.isUseCaseFailed());
    }

    @Test
    void testNullValues() {
        PortfolioHealthOutputData outputData = new PortfolioHealthOutputData(
                null, null, null, null, null, null, null, null, null, false
        );

        assertNull(outputData.getRiskPreference());
        assertNull(outputData.getPortfolioHealthScore());
        assertNull(outputData.getBeta());
        assertNull(outputData.getAlpha());
        assertNull(outputData.getSharpeRatio());
        assertNull(outputData.getSharpeAdvice());
        assertNull(outputData.getRiskAlignmentAdvice());
        assertNull(outputData.getDiversificationAdvice());
        assertNull(outputData.getNewsAdvice());
        assertFalse(outputData.isUseCaseFailed());
    }
}