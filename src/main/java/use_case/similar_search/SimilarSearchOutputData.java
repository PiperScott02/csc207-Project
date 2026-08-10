package use_case.similar_search;

import java.math.BigDecimal;

/**
 * Output Data for the Similar Search Use Case.
 */
public class SimilarSearchOutputData {

    private SimilarSearchStockInfo[] similarSearchOutputData;

    public SimilarSearchOutputData(int numResults) {
        this.similarSearchOutputData = new SimilarSearchStockInfo[numResults];
    }

    public void setSimilarSearchStockInfo(int i,
                                          String tickerSymbol,
                                          String companyName,
                                          String country,
                                          String industry,
                                          BigDecimal previousClose) {
        this.similarSearchOutputData[i] = new SimilarSearchStockInfo(tickerSymbol,
                companyName,
                country,
                industry,
                previousClose);
    }

    public String getTickerSymbol(int i) {
        return this.similarSearchOutputData[i].getTickerSymbol();
    }

    public String getCompanyName(int i) {
        return this.similarSearchOutputData[i].getCompanyName();
    }

    public String getCountry(int i) {
        return this.similarSearchOutputData[i].getCountry();
    }

    public String getIndustry(int i) {
        return this.similarSearchOutputData[i].getCountry();
    }

    public BigDecimal getPreviousClose(int i) {
        return this.similarSearchOutputData[i].getPreviousClose();
    }

    public int getLength() {
        return this.similarSearchOutputData.length;
    }

    static private class SimilarSearchStockInfo {

        private final String tickerSymbol;
        private final String companyName;
        private final String country;
        private final String industry;
        private final BigDecimal previousClose;

        public SimilarSearchStockInfo(String tickerSymbol,
                                       String companyName,
                                       String country,
                                       String industry,
                                       BigDecimal previousClose) {
            this.tickerSymbol = tickerSymbol;
            this.companyName = companyName;
            this.country = country;
            this.previousClose = previousClose;
            this.industry = industry;
        }

        public String getTickerSymbol() {
            return tickerSymbol;
        }

        public String getCompanyName() {
            return companyName;
        }

        public BigDecimal getPreviousClose() {
            return previousClose;
        }

        public String getCountry() {
            return country;
        }

        public String getIndustry() {
            return this.industry;
        }
    }
}
