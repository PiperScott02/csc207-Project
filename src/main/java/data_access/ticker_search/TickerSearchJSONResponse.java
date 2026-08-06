package data_access.ticker_search;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TickerSearchJSONResponse {
    @SerializedName("Symbol")
    private String tickerSymbol;

    @SerializedName("Name")
    private String companyName;

    @SerializedName("Country")
    private String country;

    @SerializedName("Industry")
    private String industry;

    @SerializedName("PERatio")
    private String PERatio;

    @SerializedName("EPS")
    private String EPS;

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCountry() {
        return country;
    }

    public String getIndustry() {
        return industry;
    }

    public BigDecimal getStockPrice() {
        if (this.PERatio.equals("None")
                || this.PERatio.equals("NONE")
                || this.EPS.equals("None")
                || this.EPS.equals("NONE")) {
            return null;
        }
        final BigDecimal PERationAsBigDecimal = new BigDecimal(this.PERatio);
        final BigDecimal EPSAsBigDecimal = new BigDecimal(this.EPS);
        return PERationAsBigDecimal.multiply(EPSAsBigDecimal);
    }
}
