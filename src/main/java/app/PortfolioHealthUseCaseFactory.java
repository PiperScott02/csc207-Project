package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthPresenter;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import use_case.portfolio_health.PortfolioHealthInputBoundary;
import use_case.portfolio_health.PortfolioHealthInteractor;
import use_case.portfolio_health.PortfolioHealthOutputBoundary;
import use_case.stock.StockDataAccessInterface;
import view.PortfolioHealthView;

public final class PortfolioHealthUseCaseFactory {

    private PortfolioHealthUseCaseFactory() {
    }

    public static PortfolioHealthView create(
            ViewManagerModel viewManagerModel,
            PortfolioHealthViewModel portfolioHealthViewModel) {

        return new PortfolioHealthView(portfolioHealthViewModel);
    }

    public static PortfolioHealthController createPortfolioHealthUseCase(
            ViewManagerModel viewManagerModel,
            PortfolioHealthViewModel portfolioHealthViewModel,
            StockDataAccessInterface stockDataAccessObject) {

        final PortfolioHealthOutputBoundary presenter =
                new PortfolioHealthPresenter(viewManagerModel, portfolioHealthViewModel);

        final PortfolioHealthInputBoundary interactor =
                new PortfolioHealthInteractor(
                        stockDataAccessObject,
                        presenter
                );

        return new PortfolioHealthController(interactor);
    }
}