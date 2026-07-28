    package use_case.analysis;

    import entity.Portfolio;
    import entity.RiskLevel;
    import entity.RiskProfile;

    /**
     * Service class responsible for calculating individual sub-scores and evaluating
     * the overall health of an investment portfolio based on quantitative metrics
     * and user risk preferences.
     */
    public class PortfolioHealthScoringService {


        /** Calculates the total portfolio health score out of 100.
         * @param portfolio the Portfolio to evaluate.
         * @param riskProfile the user's RiskProfile entity.
         * @return the total portfolio health score as a Double.
         */
        public static Double calculatePortfolioHealthScore(Portfolio portfolio, RiskProfile riskProfile) {
            if (portfolio == null) return 0.0;

            Double beta = portfolio.getTrueBeta();
            Double sharpeRatio = portfolio.getSharpeRatio();
            Double cdr = PortfolioFinancialService.calculateCdr(portfolio); // Or wherever your CDR is calculated

            double sharpeScore = calculateSharpeScore(sharpeRatio);
            double riskScore = calculateRiskAlignmentScore(beta, riskProfile);
            double divScore = calculateDiversificationScore(cdr);
            double newsScore = calculateNewsScore();

            return sharpeScore + riskScore + divScore + newsScore;
        }


        /**
         * Calculates the Sharpe Ratio sub-score (out of 25.0 points) based on risk-adjusted returns.
         *
         * @param sharpeRatio the portfolio's Sharpe ratio
         * @return the calculated score as a Double
         */
        public static Double calculateSharpeScore(Double sharpeRatio) {
            if (sharpeRatio == null) return 0.00;
            if (sharpeRatio >= 3.0) return 25.00;
            if (sharpeRatio >= 2.0) return 20.00;
            if (sharpeRatio >= 1.0) return 15.00;
            if (sharpeRatio >= 0) return 10.00;
            return 0.0;
        }

        /**
         * Calculates the Risk Alignment sub-score (out of 25.0 points) by comparing
         * the portfolio's true beta against the user's targeted risk profile.
         *
         * @param beta the portfolio's true beta relative to the market
         * @param riskProfile the user's selected investment risk profile
         * @return the calculated risk alignment score as a Double
         */
        public static Double calculateRiskAlignmentScore(Double beta, RiskProfile riskProfile) {
            if (beta == null || riskProfile == null) return 0.0;

            double comparisonScore = 1.0;
            double difference = 0.0;
            RiskLevel level = riskProfile.getRiskLevel();

            if (level == RiskLevel.CONSERVATIVE) {
                comparisonScore = 0.8;
                if (beta > comparisonScore) {
                    difference = beta - comparisonScore;
                }
            } else if (level == RiskLevel.MODERATE) {
                comparisonScore = 1.0;
                double deviation = Math.abs(beta - comparisonScore);
                if (deviation > 0.2) {
                    difference = deviation - 0.2;
                }
            } else if (level == RiskLevel.AGGRESSIVE) {
                comparisonScore = 1.2;
                if (beta < comparisonScore) {
                    difference = comparisonScore - beta;
                }
            }

            // Scoring tiers based on the calculated penalty difference
            if (difference <= 0.0) return 25.0;
            if (difference <= 0.15) return 20.0;
            if (difference <= 0.3) return 15.0;
            if (difference <= 0.45) return 10.0;
            if (difference <= 0.6) return 5.0;
            return 0.0;
        }

        /**
         * Calculates the Diversification sub-score (out of 25.0 points) using
         * Choueifat's Diversification Ratio (CDR).
         *
         * @param cdr the computed Choueifat Diversification ratio of the portfolio
         * @return the calculated diversification score as a Double
         */
        public static Double calculateDiversificationScore(Double cdr) {
            if (cdr == null || cdr < 1.0) {
                return 0.0;
            }
            if (cdr >= 1.5) {
                return 25.0;
            }
            if (cdr >= 1.4) {
                return 20.0;
            }
            if (cdr >= 1.3) {
                return 15.0;
            }
            if (cdr >= 1.2) {
                return 10.0;
            }
            if (cdr > 1.0) {
                return 5.0;
            }
            return 0.0;
        }

        /**
         * Calculates the news score (placeholder for now).
         *
         * @return the calculated news score as a double
         */
        public static double calculateNewsScore() {
            /* Pending News Data Processing */
            return 0.0;
        }
    }