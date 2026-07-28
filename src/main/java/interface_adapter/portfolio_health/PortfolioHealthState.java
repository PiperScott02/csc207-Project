    package interface_adapter.portfolio_health;

    public class PortfolioHealthState {
        private String portfolioHealthScore = "";
        private String riskPreference = "";
        private String beta = "";
        private String alpha = "";
        private String sharpeRatio = "";
        private String sharpeAdvice = "";
        private String riskAlignmentAdvice = "";
        private String diversificationAdvice = "";
        private String newsAdvice = "";
        private String errorMessage = "";

        /** Copy constructor to create a new PortfolioHealthState from an existing one.
         * @param copy the PortfolioHealthState to copy from.
         */
        public PortfolioHealthState(PortfolioHealthState copy) {
            this.portfolioHealthScore = copy.portfolioHealthScore;
            this.riskPreference = copy.riskPreference;
            this.beta = copy.beta;
            this.alpha = copy.alpha;
            this.sharpeRatio = copy.sharpeRatio;
            this.sharpeAdvice = copy.sharpeAdvice;
            this.riskAlignmentAdvice = copy.riskAlignmentAdvice;
            this.diversificationAdvice = copy.diversificationAdvice;
            this.newsAdvice = copy.newsAdvice;
            this.errorMessage = copy.errorMessage;
        }

        /** Default constructor to create an empty PortfolioHealthState. */
        public PortfolioHealthState() {
        }

        /** Returns the portfolio health score.
         * @return the portfolio health score string.
         */
        public String getPortfolioHealthScore() {
            return portfolioHealthScore;
        }

        /** Sets the portfolio health score.
         * @param portfolioHealthScore the portfolio health score to set.
         */
        public void setPortfolioHealthScore(String portfolioHealthScore) {
            this.portfolioHealthScore = portfolioHealthScore;
        }

        /** Returns the risk preference.
         * @return the risk preference string.
         */
        public String getRiskPreference() {
            return riskPreference;
        }

        /** Sets the risk preference.
         * @param riskPreference the risk preference to set.
         */
        public void setRiskPreference(String riskPreference) {
            this.riskPreference = riskPreference;
        }

        /** Returns the beta value as a string.
         * @return the beta string.
         */
        public String getBeta() {
            return beta;
        }

        /** Sets the beta value string.
         * @param beta the beta string to set.
         */
        public void setBeta(String beta) {
            this.beta = beta;
        }

        /** Returns the alpha value as a string.
         * @return the alpha string.
         */
        public String getAlpha() {
            return alpha;
        }

        /** Sets the alpha value string.
         * @param alpha the alpha string to set.
         */
        public void setAlpha(String alpha) {
            this.alpha = alpha;
        }

        /** Returns the Sharpe ratio value as a string.
         * @return the Sharpe ratio string.
         */
        public String getSharpeRatio() {
            return sharpeRatio;
        }

        /** Sets the Sharpe ratio value string.
         * @param sharpeRatio the Sharpe ratio string to set.
         */
        public void setSharpeRatio(String sharpeRatio) {
            this.sharpeRatio = sharpeRatio;
        }

        /** Returns the Sharpe advice string.
         * @return the Sharpe advice.
         */
        public String getSharpeAdvice() {
            return sharpeAdvice;
        }

        /** Sets the Sharpe advice string.
         * @param sharpeAdvice the Sharpe advice to set.
         */
        public void setSharpeAdvice(String sharpeAdvice) {
            this.sharpeAdvice = sharpeAdvice;
        }

        /** Returns the risk alignment advice string.
         * @return the risk alignment advice.
         */
        public String getRiskAlignmentAdvice() {
            return riskAlignmentAdvice;
        }

        /** Sets the risk alignment advice string.
         * @param riskAlignmentAdvice the risk alignment advice to set.
         */
        public void setRiskAlignmentAdvice(String riskAlignmentAdvice) {
            this.riskAlignmentAdvice = riskAlignmentAdvice;
        }

        /** Returns the diversification advice string.
         * @return the diversification advice.
         */
        public String getDiversificationAdvice() {
            return diversificationAdvice;
        }

        /** Sets the diversification advice string.
         * @param diversificationAdvice the diversification advice to set.
         */
        public void setDiversificationAdvice(String diversificationAdvice) {
            this.diversificationAdvice = diversificationAdvice;
        }

        /** Returns the news advice string.
         * @return the news advice.
         */
        public String getNewsAdvice() {
            return newsAdvice;
        }

        /** Sets the news advice string.
         * @param newsAdvice the news advice to set.
         */
        public void setNewsAdvice(String newsAdvice) {
            this.newsAdvice = newsAdvice;
        }

        /** Returns the error message.
         * @return the error message string.
         */
        public String getErrorMessage() {
            return errorMessage;
        }

        /** Sets the error message.
         * @param errorMessage the error message string to set.
         */
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }