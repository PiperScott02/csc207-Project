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


    /** Calculates the total current value of this holding based on current market price.
     * @return the total value as a BigDecimal.
     */
    public BigDecimal calculateTotalValue() {
        if (stock == null || stock.getClose() == null) {
            return BigDecimal.ZERO;
        }
        return stock.getClose().multiply(BigDecimal.valueOf(getNumberOfShares()));
    }

    /** Calculates the quantity of shares held on a specific date.
     * @param date the date to check the quantity for.
     * @return the number of shares as a double.
     */
    public double getQuantityOnDate(LocalDate date) {
        double quantity = 0;
        for (Transaction transaction: transactions) {
            System.out.println(getStock() + "getQuantityOnDate transaction date" +  transaction.getDate() );
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
        transaction.setPricePerShare(stock.getClosestPrice(date)); // Returns closest closing price even on non-trading days
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
        transaction.setDate(lastDay);
        this.transactions.add(transaction);
    }

    /**
     * Extracts and returns a list of unique Stock entities from a given list of StockHoldings.
     * @param holdings the list of stock holdings to extract stocks from
     * @return a list of Stock objects associated with the holdings
     */
    public static List<Stock> extractStocks(List<StockHolding> holdings) {
        List<Stock> stocks = new ArrayList<>();
        for (StockHolding holding : holdings) {
            if (holding.getStock() != null) {
                stocks.add(holding.getStock());
            }
        }
        return stocks;
    }

    /** Returns the list of transactions for this holding.
     * @return the list of Transaction objects.
     */
    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    /** Calculates the total cost of shares purchased minus sold.
     * @return the total cost as a BigDecimal.
     */
    public BigDecimal calculateTotalCost() {
        BigDecimal totalCost = BigDecimal.ZERO;
        if (transactions == null) {
            return totalCost;
        }
        for (Transaction t : transactions) {
            // Guard against null price per share from simplified CSV holdings
            if (t != null && t.getPricePerShare() != null) {
                BigDecimal txCost = t.getPricePerShare().multiply(BigDecimal.valueOf(t.getNumberOfShares()));
                if (t.getType() == TransactionType.BUY) {
                    totalCost = totalCost.add(txCost);
                } else if (t.getType() == TransactionType.SELL) {
                    totalCost = totalCost.subtract(txCost);
                }
            }
        }
        return totalCost;
    }

    /** Calculates the dollar gain or loss for this holding.
     * @return the gain or loss as a BigDecimal.
     */
    public BigDecimal calculateGainLoss() {
        return calculateTotalValue().subtract(calculateTotalCost());
    }

    /** Calculates the percentage gain or loss for this holding.
     * @return the gain or loss percentage as a BigDecimal.
     */
    public BigDecimal calculateGainLossPercentage() {
        BigDecimal cost = calculateTotalCost();
        if (cost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return calculateGainLoss()
                .divide(cost, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /** Calculates the average price per share.
     * @return the average price as a double.
     */
    public double getAveragePrice() {
        double shares = getNumberOfShares();
        if (shares == 0) return 0.0;
        BigDecimal cost = calculateTotalCost();
        return cost.divide(BigDecimal.valueOf(shares), 4, java.math.RoundingMode.HALF_UP).doubleValue();
    }
}