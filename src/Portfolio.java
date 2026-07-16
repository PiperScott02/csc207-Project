import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public class Portfolio {
    private String portfolioName;

    private List<StockHolding> holdings;

    private List<StockHolding> watchlisted;

    private double portfolioHealth;

}
