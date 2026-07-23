package data_access.stock_daily;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageOverviewResponse {

    @JsonProperty("Name")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}