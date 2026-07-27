package interface_adapter.stock;
import interface_adapter.ViewManagerModel;
import use_case.stock.StockOutputBoundary;
import use_case.stock.StockOutputData;

public class StockPresenter implements StockOutputBoundary {

    private final StockViewModel stockViewModel;

    private final ViewManagerModel viewManagerModel;
    public StockPresenter(ViewManagerModel viewManagerModel, StockViewModel stockViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.stockViewModel = stockViewModel;
    }

    @Override
    public void prepareSuccessView(StockOutputData outputData) {
        StockState currentState = stockViewModel.getState();

        currentState.setTicker(outputData.getTickerSymbol());
        currentState.setCompanyName(outputData.getCompanyName());
        currentState.setClose(outputData.getClose());
        currentState.setBeta(outputData.getBeta());
        currentState.setAlpha(outputData.getAlpha());
        currentState.setSharpeRatio(outputData.getSharpeRatio());

        stockViewModel.setState(currentState);

        stockViewModel.firePropertyChanged();

        viewManagerModel.setState(stockViewModel.getViewName());
        viewManagerModel.firePropertyChanged();

    }

    @Override
    public void prepareFailView(String errorMessage) {
        StockState currentState = stockViewModel.getState();
        currentState.setErrorMessage(errorMessage);

        stockViewModel.setState(currentState);
        stockViewModel.firePropertyChanged();
    }
}