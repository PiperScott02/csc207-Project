package use_case.analysis;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {

    @Test
    void testCalculateMean() {
        List<Double> values = Arrays.asList(2.0, 4.0, 6.0);
        double mean = StatisticsService.calculateMean(values);
        assertEquals(4.0, mean, 0.0001);
    }

    @Test
    void testCalculateVariance() {
        List<Double> values = Arrays.asList(2.0, 4.0, 6.0);
        // mean = 4.0. sq diffs: (-2)^2 + 0 + 2^2 = 8. sample variance = 8 / (3-1) = 4.0
        double variance = StatisticsService.calculateVariance(values);
        assertEquals(4.0, variance, 0.0001);
    }

    @Test
    void testCalculateCovariance() {
        List<Double> list1 = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> list2 = Arrays.asList(2.0, 4.0, 6.0);
        double covariance = StatisticsService.calculateCovariance(list1, list2);
        assertEquals(2.0, covariance, 0.0001);
    }

    @Test
    void testCalculateAlpha() {
        double assetMean = 0.10;
        double marketMean = 0.08;
        double beta = 1.1;
        double riskFreeRate = 0.01;

        // Formula: assetMean - (riskFreeRate + (beta * (marketMean - riskFreeRate)))
        // 0.10 - (0.01 + 1.1 * (0.08 - 0.01)) = 0.10 - (0.01 + 0.077) = 0.013
        double alpha = StatisticsService.calculateAlpha(assetMean, marketMean, beta, riskFreeRate);
        assertEquals(0.013, alpha, 0.0001);
    }
}