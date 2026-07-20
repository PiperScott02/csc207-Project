package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StockHolding {
    private Stock stock;

    private List<Transaction> transactions;

    private BigDecimal purchaseDate;

    private LocalDate date;



    public BigDecimal calculateTotalValue() {
        BigDecimal price = stock.getClose();
        BigDecimal shares = BigDecimal.valueOf(getNumberOfShares());
        return price.multiply(shares);
    }

    public double getQuantityOnDate(LocalDate date) {
        double quantity = 0;
        for (Transaction transaction: transactions) {
            if (!transaction.getDate().isAfter(date)) {
                quantity += transaction.getNumberOfShares();
            }
        }
        return quantity;
    }

    public BigDecimal calculateTotalValueOnDate(LocalDate date) {
        BigDecimal price = stock.getCloseOnDate(date);
        if (price == null) {
            return null;
        }
        double shares = getQuantityOnDate(date);
        return price.multiply(BigDecimal.valueOf(shares));
    }

    public Stock getStock() {
        return this.stock;
    }

    public double getNumberOfShares() {
        return getQuantityOnDate(LocalDate.now());
    }


}
