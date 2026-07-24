package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Represents a holding of a specific stock along with its transaction history. */
public class StockHolding {
    private Stock stock;

    private List<Transaction> transactions = new ArrayList<>();

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
                if (transaction.getType() == TransactionType.BUY)
                    quantity += transaction.getNumberOfShares();
                if (transaction.getType() == TransactionType.SELL)
                    quantity -= transaction.getNumberOfShares();
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

    /** Sets the stock of this StockHolding.
     * @param stock  the stock to be set.
     */
    public void setStock(Stock stock) {this.stock = stock;
    }

    /** Returns the current number of shares held.
     * @return the number of shares as a double.
     */
    public double getNumberOfShares() {
        return getQuantityOnDate(LocalDate.now());
    }

    public void makeTransaction(Stock stock, Double quantity, LocalDate date, TransactionType transactionType) {
        Transaction transaction = new Transaction();

        transaction.setDate(date);
        transaction.setPricePerShare(stock.getCloseOnDate(date));
        transaction.setNumberOfShares(quantity);
        transaction.setType(transactionType);

        this.transactions.add(transaction);
    }
    public void makeTransaction(Stock stock, Double quantity, TransactionType type) {
        Transaction transaction = new Transaction();
        LocalDate lastDay = stock.getLastTradingDay();
        transaction.setPricePerShare(stock.getCloseOnDate(lastDay));
        transaction.setNumberOfShares(quantity);
        transaction.setType(type);
        this.transactions.add(transaction);
    }


}