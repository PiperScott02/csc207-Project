package interface_adapter.news;

import use_case.news.NewsOutputBoundary;
import use_case.news.NewsOutputData;

/**
 * Converts news use-case results into view state.
 */
public class NewsPresenter implements NewsOutputBoundary {

    private final NewsViewModel newsViewModel;

    public NewsPresenter(NewsViewModel newsViewModel) {
        this.newsViewModel = newsViewModel;
    }

    @Override
    public void prepareSuccessView(NewsOutputData outputData) {
        NewsState state = newsViewModel.getState();

        state.setTicker(outputData.getTicker());
        state.setArticles(outputData.getArticles());
        state.setOverallSentiment(
                outputData.getOverallSentiment()
        );
        state.setErrorMessage("");

        newsViewModel.setState(state);
        newsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        NewsState state = newsViewModel.getState();

        state.setErrorMessage(errorMessage);
        state.setArticles(new java.util.ArrayList<>());

        newsViewModel.setState(state);
        newsViewModel.firePropertyChanged();
    }
}