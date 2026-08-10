package use_case.black_litterman;

import entity.Stock;

public interface BlackLittermanDataAccessInterface {
    Stock get(String ticker);
}
