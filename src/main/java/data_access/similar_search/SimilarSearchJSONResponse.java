package data_access.similar_search;

import com.google.gson.annotations.SerializedName;

public class SimilarSearchJSONResponse {
    @SerializedName("bestMatches")
    private SpecificStockResponse[] bestMatches;

    public int getLength() {
        return this.bestMatches.length;
    }

    public String getTickerSymbol(int i) {
        return this.bestMatches[i].getTickerSymbol();
    }

    public String getCompanyName(int i) {
        return this.bestMatches[i].getCompanyName();
    }

    public String getRegion(int i) {
        return this.bestMatches[i].getRegion();
    }

    private static class SpecificStockResponse {
        @SerializedName("1. symbol")
        private String tickerSymbol;

        @SerializedName("2. name")
        private String companyName;

        @SerializedName("4. region")
        private String region;

        private String getTickerSymbol() {
            return tickerSymbol;
        }

        private String getCompanyName() {
            return companyName;
        }

        private String getRegion() {
            return region;
        }
    }
}
