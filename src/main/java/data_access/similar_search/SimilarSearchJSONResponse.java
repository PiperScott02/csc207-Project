package data_access.similar_search;

import com.google.gson.annotations.SerializedName;

public class SimilarSearchJSONResponse {
    public SpecificStockResponse[] bestMatches;

    public static class SpecificStockResponse {
        @SerializedName("1. symbol")
        public String tickerSymbol;

        @SerializedName("2. name")
        public String companyName;

        @SerializedName("3. type")
        public String type;

        @SerializedName("4. region")
        public String region;

        @SerializedName("5. marketOpen")
        public String marketOpen;

        @SerializedName("6. marketClose")
        public String marketClose;

        @SerializedName("7. timezone")
        public String timezone;

        @SerializedName("8. currency")
        public String currency;

        @SerializedName("9. matchScore")
        public String matchScore;
    }
}
