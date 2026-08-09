package use_case.similar_search;

import java.math.BigDecimal;

/**
 * Output Data for the Similar Search Use Case.
 */
public class SimilarSearchOutputData {

    private final String tickerSymbol;
    private final String companyName;
    private final String country;
    private final String industry;
    private final BigDecimal previousClose;

    public SimilarSearchOutputData(String tickerSymbol,
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
