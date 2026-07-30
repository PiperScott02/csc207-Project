package entity;

import java.math.BigDecimal;

public record WatchlistStockItem(
        String ticker,
        String companyName,
        BigDecimal closePrice,
        BigDecimal dailyPriceChange
) {}