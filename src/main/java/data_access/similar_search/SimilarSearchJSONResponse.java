package data_access.similar_search;

import com.google.gson.annotations.SerializedName;

public class SimilarSearchJSONResponse {
    SpecificStockResponse[] bestMatches;

    public static class SpecificStockResponse {
        @SerializedName("1. symbol")
        String tickerSymbol;

        @SerializedName("2. name")
        String companyName;

        @SerializedName("3. type")
        String type;

        @SerializedName("4. region")
        String region;

        @SerializedName("5. marketOpen")
        String marketOpen;

        @SerializedName("6. marketClose")
        String marketClose;

        @SerializedName("7. timeZone")
        String timeZone;

        @SerializedName("8. currency")
        String currency;

        @SerializedName("9. matchScore")
        String matchScore;
    }
}
