import java.math.BigDecimal;
import java.time.LocalDate;

public class StockHolding {
    private Stock stock;

    private double numberOfShares;

    private BigDecimal purchaseDate;

    private LocalDate date;

    public BigDecimal calculateTotalValue() {
        BigDecimal price = stock.getCurrentPrice();
        BigDecimal shares = BigDecimal.valueOf(numberOfShares);
        return price.multiply(shares);
    }

    public Stock getStock() {
        return this.stock;
    }
}
