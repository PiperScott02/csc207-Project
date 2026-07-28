package use_case.analysis;

import entity.Stock;
import entity.StockHolding;

import java.util.*;

public class AssetUniverseService {

    private List<Stock> stocks = new ArrayList<>();
    private Map<String, Integer> indexMap = new HashMap<>();

    public AssetUniverseService(List<StockHolding> holdings) {
        stocks = StockHolding.extractStocks(holdings);

        // choose ONE ordering
        stocks.sort(Comparator.comparing(Stock::getTickerSymbol));

        indexMap = new HashMap<>();

        for (int i = 0; i < stocks.size(); i++) {
            indexMap.put(stocks.get(i).getTickerSymbol(), i);
        }
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public int indexOf(String ticker) {
        Integer index = indexMap.get(ticker);
        if (index == null) {
            throw new IllegalArgumentException("Ticker not found in universe: " + ticker);
        }
        return index;
    }

    public int size() {
        return stocks.size();
    }
}