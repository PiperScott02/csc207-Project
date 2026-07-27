package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Represents a financial transaction for buying or selling a stock. */
public class Transaction {
    private String   id;

    private String userId;

    private String ticker;

    private LocalDate date;

    private Double numberOfShares;

    private BigDecimal pricePerShare;

    private TransactionType type;

    /** Returns the user ID associated with this transaction.
     * @return the user ID string.
     */
    public String getUserId() {
        return this.userId;
    }

    /** Returns the number of shares involved in this transaction.
     * @return the number of shares as a double.
     */
    public double getNumberOfShares() {
        return this.numberOfShares;
    }

    /** Returns the price per share for this transaction.
     * @return the price per share as a BigDecimal.
     */
    public BigDecimal getPricePerShare() {
        return this.pricePerShare;
    }

    /** Returns the date when this transaction occurred.
     * @return the transaction LocalDate.
     */
    public LocalDate getDate() {
        return this.date;
    }
}