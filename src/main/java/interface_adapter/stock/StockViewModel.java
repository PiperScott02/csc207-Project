package interface_adapter.stock;

import interface_adapter.ViewModel;

/** ViewModel for managing and observing the state of stock information in the user interface. */
public class StockViewModel extends ViewModel<StockState> {

    public static final String TITLE_LABEL = "Stock Information";

    /** Constructs a new StockViewModel with the view name "stock" and initializes its default state. */
    public StockViewModel() {
        super("stock");
        setState(new StockState());
    }
}