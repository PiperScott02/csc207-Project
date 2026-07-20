package use_case.analysis;

import java.util.List;

public class StatisticsService {
    private StatisticsService() {
    }

    public static double calculateMean(List<Double> returnRatios) {
        Double sum = 0.0;
        for (Double r: returnRatios) {
            sum += r;
        }
        return sum/returnRatios.size();
    }

    public static double calculateVariance(List<Double> ratios) {
        double mean = calculateMean(ratios);
        double sumofdifferencessquared = 0.0;
        for (int i = 0; i < ratios.size(); i++) {
            Double differencesquared = Math.pow(ratios.get(i)-mean,2);
            sumofdifferencessquared = sumofdifferencessquared + differencesquared;
        }
        Double variance = sumofdifferencessquared/(ratios.size() - 1);
        return variance;

    }

    public static double calculateCovariance(List<Double> ratios1, List<Double> ratios2) {
        Double mean1 = calculateMean(ratios1);
        Double mean2 = calculateMean(ratios2);
        Double sumofdifferencesmultiplied = 0.0;
        for (int i = 0; i < ratios1.size(); i++) {
            Double differences1 = ratios1.get(i) - mean1;
            Double differences2 = ratios2.get(i) - mean2;
            Double productofdifferences = differences1*differences2;
            sumofdifferencesmultiplied = sumofdifferencesmultiplied + productofdifferences;
        }
        Double covariance = sumofdifferencesmultiplied/(ratios1.size() - 1);
        return covariance;
    }
    public static double calculateAlpha(double assetMean, double marketMean, double beta, double riskFreeRate) {
        return assetMean - (riskFreeRate + (beta*(marketMean - riskFreeRate)));
    }


}
