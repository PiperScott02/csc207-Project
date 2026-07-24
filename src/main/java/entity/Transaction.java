package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Represents a financial transaction for buying or selling a stock. */
public class Transaction {
    private String   id;

    private String userId;

    private LocalDate date;

    private Double numberOfShares;

    private BigDecimal pricePerShare;

    private TransactionType type;

    /** Returns the type of transaction that occurred.
     * @return the type of transaction, either BUY or SELL.
     */
    public TransactionType getType() {
        return type;
    }

    /** Sets the type of transaction that occurred.
     * @param type the type of transaction that occurred.
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

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


    /** Sets the number of shares involved in this transaction.
     * @param numberOfShares the number of shares as a double.
     */
     public void setNumberOfShares(Double numberOfShares) {
     this.numberOfShares = numberOfShares;
     }

    /** Sets the price per share of this transaction.
     * @param pricePerShare the number of shares as a double.
     */
    public void setPricePerShare(BigDecimal pricePerShare) {
        this.pricePerShare = pricePerShare;
    }

    /** Returns the date when this transaction occurred.
     * @return the transaction LocalDate.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /** Sets the date when this transaction occurred.
     * @param date the date when this transaction occurred.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }


}