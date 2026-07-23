package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Represents a holding of a specific stock along with its transaction history. */
public class StockHolding {
    private Stock stock;

    private List<Transaction> transactions;

    private BigDecimal purchaseDate;

    private LocalDate date;


    /** Calculates the total current value of this holding.
     * @return the total value as a BigDecimal.
     */
    public BigDecimal calculateTotalValue() {
        BigDecimal price = stock.getClose();
        BigDecimal shares = BigDecimal.valueOf(getNumberOfShares());
        return price.multiply(shares);
    }

    /** Calculates the quantity of shares held on a specific date.
     * @param date the date to check the quantity for.
     * @return the number of shares as a double.
     */
    public double getQuantityOnDate(LocalDate date) {
        double quantity = 0;
        for (Transaction transaction: transactions) {
            if (!transaction.getDate().isAfter(date)) {
                quantity += transaction.getNumberOfShares();
            }
        }
        return quantity;
    }

    /** Calculates the total value of this holding on a specific date.
     * @param date the date for the value calculation.
     * @return the total value on that date as a BigDecimal, or null if price data is missing.
     */
    public BigDecimal calculateTotalValueOnDate(LocalDate date) {
        BigDecimal price = stock.getCloseOnDate(date);
        if (price == null) {
            return null;
        }
        double shares = getQuantityOnDate(date);
        return price.multiply(BigDecimal.valueOf(shares));
    }

    /** Returns the Stock entity associated with this holding.
     * @return the Stock object.
     */
    public Stock getStock() {
        return this.stock;
    }

    /** Returns the current number of shares held.
     * @return the number of shares as a double.
     */
    public double getNumberOfShares() {
        return getQuantityOnDate(LocalDate.now());
    }


}