package use_case.stock;

/** Input boundary for stock use cases. */
public interface StockInputBoundary {

    /** Executes the stock use case with the provided input data.
     * @param inputData the input data containing the stock ticker and/or parameters.
     */
    void execute(StockInputData inputData);
}

