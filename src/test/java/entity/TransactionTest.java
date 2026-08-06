package entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testGettersAndSetters() {
        Transaction transaction = new Transaction();
        LocalDate date = LocalDate.of(2026, 8, 5);
        BigDecimal price = new BigDecimal("150.50");

        transaction.setType(TransactionType.BUY);
        transaction.setDate(date);
        transaction.setNumberOfShares(10.5);
        transaction.setPricePerShare(price);

        assertEquals(TransactionType.BUY, transaction.getType());
        assertEquals(date, transaction.getDate());
        assertEquals(10.5, transaction.getNumberOfShares());
        assertEquals(price, transaction.getPricePerShare());
    }

    @Test
    void testNullValues() {
        Transaction transaction = new Transaction();

        transaction.setType(null);
        transaction.setDate(null);
        transaction.setNumberOfShares(null);
        transaction.setPricePerShare(null);

        assertNull(transaction.getType());
        assertNull(transaction.getDate());
        assertNull(transaction.getPricePerShare());
        assertNull(transaction.getUserId());
    }
}