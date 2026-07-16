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

    private BigDecimal calculateTotalPortfolioValue() {
        BigDecimal value = BigDecimal.ZERO;
        for (StockHolding holding: holdings) {
            value = value.add(holding.calculateTotalValue());
        }
        return value;
    }

    private BigDecimal calculateholdingShare(StockHolding holding) {
        BigDecimal holdingPrice = holding.calculateTotalValue();
        BigDecimal portfolioValue = this.calculateTotalPortfolioValue();
        if (portfolioValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return holdingPrice.divide(portfolioValue, 4, java.math.RoundingMode.HALF_UP);
    }

    private double calculatePortfolioBeta() {
        BigDecimal totalBeta = BigDecimal.ZERO;
        for (StockHolding holding: holdings) {
            BigDecimal holdingShare = this.calculateholdingShare(holding);
            BigDecimal holdingStockBeta = BigDecimal.valueOf(holding.getStock().getBeta());
            BigDecimal betaContribution = holdingShare.multiply(holdingStockBeta);
            totalBeta = totalBeta.add(betaContribution);
        }
        return totalBeta.doubleValue();
    }

}
