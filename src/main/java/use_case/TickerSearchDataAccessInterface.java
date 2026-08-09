package use_case;

import entity.Stock;

import java.io.IOException;

public interface TickerSearchDataAccessInterface {

    Stock createBasicStock(String tickerSymbol);

}
