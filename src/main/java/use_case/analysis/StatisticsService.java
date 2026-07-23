package use_case.analysis;

import java.util.List;

/** Utility service class for performing statistical calculations such as mean, variance, covariance, and alpha. */
public class StatisticsService {
    private StatisticsService() {
    }

    /** Calculates the arithmetic mean of a list of return ratios.
     * @param returnRatios the list of double values.
     * @return the mean as a double.
     */
    public static double calculateMean(List<Double> returnRatios) {
        Double sum = 0.0;
        for (Double r: returnRatios) {
            sum += r;
        }
        return sum/returnRatios.size();
    }

    /** Calculates the sample variance of a list of ratios.
     * @param ratios the list of double values.
     * @return the sample variance as a double.
     */
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

    /** Calculates the sample covariance between two lists of ratios.
     * @param ratios1 the first list of double values.
     * @param ratios2 the second list of double values.
     * @return the sample covariance as a double.
     */
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

    /** Calculates the alpha metric (Jensen's alpha) given asset and market means, beta, and the risk-free rate.
     * @param assetMean the mean return of the asset.
     * @param marketMean the mean return of the market.
     * @param beta the beta value of the asset.
     * @param riskFreeRate the risk-free rate.
     * @return the calculated alpha as a double.
     */
    public static double calculateAlpha(double assetMean, double marketMean, double beta, double riskFreeRate) {
        return assetMean - (riskFreeRate + (beta*(marketMean - riskFreeRate)));
    }


}