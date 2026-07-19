import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private String   id;

    private String userId;

    private String ticker;

    private LocalDate date;

    private Double numberOfShares;

    private BigDecimal pricePerShare;

    private TransactionType type;

    public String getUserId() {
        return this.userId;
    }

    public double getNumberOfShares() {
        return this.numberOfShares;
    }

    public BigDecimal getPricePerShare() {
        return this.pricePerShare;
    }

    public LocalDate getDate() {
        return this.date;
    }
}
