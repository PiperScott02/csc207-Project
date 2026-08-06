    package use_case.analysis;

    import entity.*;
    import interface_adapter.news.NewsController;
    import use_case.news.NewsDataAccessInterface;
    import use_case.news.NewsInputBoundary;
    import use_case.news.NewsInputData;
    import use_case.news.NewsInteractor;

    import java.math.BigDecimal;
    import java.util.List;
    import java.util.concurrent.TimeUnit;

    /**
     * Service class responsible for calculating individual sub-scores and evaluating
     * the overall health of an investment portfolio based on quantitative metrics
     * and user risk preferences.
     */
    public class PortfolioHealthScoringService {

        private static final double NEUTRAL_NEWS_SCORE = 12.5;

        /**
         * Calculates the total portfolio health score out of 100.
         *
         * @param portfolio the Portfolio to evaluate.
         * @param riskProfile the user's RiskProfile entity.
         * @param newsDataAccess the data access interface for fetching stock news.
         * @return the total portfolio health score as a Double.
         */
        public static Double calculatePortfolioHealthScore(
                Portfolio portfolio,
                RiskProfile riskProfile,
                NewsDataAccessInterface newsDataAccess) {

            if (portfolio == null) return 0.0;

            Double beta = portfolio.getTrueBeta();
            Double sharpeRatio = portfolio.getSharpeRatio();
            Double cdr = PortfolioFinancialService.calculateCdr(portfolio);

            double sharpeScore = calculateSharpeScore(sharpeRatio);
            double riskScore = calculateRiskAlignmentScore(beta, riskProfile);
            double divScore = calculateDiversificationScore(cdr);
            double newsScore = calculateNewsScore(portfolio, newsDataAccess);

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
                } else {
                    difference = 0.0;
                }
            } else if (level == RiskLevel.AGGRESSIVE) {
                comparisonScore = 1.2;
                if (beta < comparisonScore) {
                    difference = comparisonScore - beta;
                }
            }


            final double EPSILON = 1e-8;

            // Scoring tiers based on the calculated penalty difference
            if (difference <= 0.0) return 25.0;
            if (difference <= 0.15 + EPSILON) return 20.0;
            if (difference <= 0.3 + EPSILON) return 15.0;
            if (difference <= 0.45 + EPSILON) return 10.0;
            if (difference <= 0.6 + EPSILON) return 5.0;
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
         * Calculates the overall portfolio news sub-score (out of 25.0) weighted by holding value.
         *
         * @param portfolio the investment portfolio
         * @param newsDataAccess the data access interface for fetching stock news
         * @return the weighted news score between 0.0 and 25.0
         */
        public static double calculateNewsScore(Portfolio portfolio, NewsDataAccessInterface newsDataAccess) {
            System.out.println("Calculating news score.");
            if (portfolio == null || newsDataAccess == null) return NEUTRAL_NEWS_SCORE;
            System.out.println("Portfolio not null and newsDataAccess not null");

            List<StockHolding> holdings = portfolio.getHoldings();
            if (holdings == null || holdings.isEmpty()) return NEUTRAL_NEWS_SCORE;

            System.out.println("Holdings not null or empty.");

            BigDecimal totalPortfolioValue = portfolio.calculateTotalPortfolioValue();
            if (totalPortfolioValue.compareTo(BigDecimal.ZERO) <= 0) return NEUTRAL_NEWS_SCORE;

            System.out.println("Total portfolio value is " + totalPortfolioValue);

            double weightedPortfolioNewsScore = 0.0;

            for (StockHolding holding : holdings) {
                Stock stock = holding.getStock();
                String ticker = stock.getTickerSymbol();

                double weight = portfolio.getHoldingShare(holding);

                // Fetch articles and calculate individual stock news points (0.0 to 25.0)
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
                double stockNewsPoints = computeStockNewsPoints(ticker, newsDataAccess);

                System.out.println(ticker + "'s score is " + stockNewsPoints);

                weightedPortfolioNewsScore += weight * stockNewsPoints;

                System.out.println("The weighted portfolio score is " + weightedPortfolioNewsScore);
            }

            return weightedPortfolioNewsScore;
        }

        private static double computeStockNewsPoints(String ticker, NewsDataAccessInterface newsDataAccess) {
            try {
                List<NewsArticle> articles = newsDataAccess.getNews(ticker);
                System.out.println ("Number of articles = " + articles.toArray().length);

                // 1. Get raw score (-1.0 to +1.0) from shared calculator
                double rawSentiment = NewsSentimentCalculator.calculateRawSentiment(articles);

                System.out.println("Sentiment of articles is " + rawSentiment);

                // 2. Map raw sentiment [-1.0, +1.0] to points [0.0, 25.0]
                System.out.println("Individual News Score: " + (NEUTRAL_NEWS_SCORE + (NEUTRAL_NEWS_SCORE * rawSentiment)));
                return NEUTRAL_NEWS_SCORE + (NEUTRAL_NEWS_SCORE * rawSentiment);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return NEUTRAL_NEWS_SCORE; // Fallback on network error
            }
        }
    }