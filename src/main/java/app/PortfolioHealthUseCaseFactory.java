package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;
import interface_adapter.portfolio_health.PortfolioHealthPresenter;
import interface_adapter.portfolio_health.PortfolioHealthViewModel;
import use_case.news.NewsDataAccessInterface;
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
            PortfolioHealthViewModel portfolioHealthViewModel, LoggedInViewModel loggedInViewModel) {

        return new PortfolioHealthView(portfolioHealthViewModel, viewManagerModel,
                loggedInViewModel
        );
    }

    public static PortfolioHealthController createPortfolioHealthUseCase(
            ViewManagerModel viewManagerModel,
            PortfolioHealthViewModel portfolioHealthViewModel,
            StockDataAccessInterface stockDataAccessObject,
            NewsDataAccessInterface newsDataAccessObject) {

        final PortfolioHealthOutputBoundary presenter =
                new PortfolioHealthPresenter(viewManagerModel, portfolioHealthViewModel);

        final PortfolioHealthInputBoundary interactor =
                new PortfolioHealthInteractor(
                        stockDataAccessObject,
                        newsDataAccessObject,
                        presenter
                );

        return new PortfolioHealthController(interactor);
    }
}