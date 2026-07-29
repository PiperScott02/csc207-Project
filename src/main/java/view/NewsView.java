package view;

import entity.NewsArticle;
import entity.NewsSentiment;
import interface_adapter.news.NewsController;
import interface_adapter.news.NewsState;
import interface_adapter.news.NewsViewModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import interface_adapter.ViewManagerModel;

/**
 * Displays stock news grouped by sentiment.
 */
public class NewsView extends JPanel
        implements PropertyChangeListener {

    public static final String VIEW_NAME = "news";

    private final NewsViewModel newsViewModel;
    private static final String LOGGED_IN_VIEW_NAME = "logged in";
    private final NewsController newsController;

    private final JTextField tickerField = new JTextField(10);

    private final JLabel overallSentimentLabel =
            new JLabel("Overall sentiment: ");

    private final JLabel errorLabel = new JLabel();

    private final JTextArea bearishArea = new JTextArea();
    private final JTextArea neutralArea = new JTextArea();
    private final JTextArea bullishArea = new JTextArea();

    public NewsView(
            NewsViewModel newsViewModel,
            NewsController newsController,
            ViewManagerModel viewManagerModel) {

        this.newsViewModel = newsViewModel;
        this.newsController = newsController;

        newsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        add(
                createSearchPanel(viewManagerModel),
                BorderLayout.NORTH
        );

        add(createArticlesPanel(), BorderLayout.CENTER);
    }

    private JPanel createSearchPanel(
            ViewManagerModel viewManagerModel) {
        final JPanel searchPanel = new JPanel();

        final JButton backButton = new JButton("Back");
        final JLabel tickerLabel = new JLabel("Ticker:");
        final JButton searchButton = new JButton("Search News");

        backButton.addActionListener(event -> {
            viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        searchButton.addActionListener(event -> {
            final String ticker = tickerField.getText();
            newsController.execute(ticker);
        });

        searchPanel.add(backButton);
        searchPanel.add(tickerLabel);
        searchPanel.add(tickerField);
        searchPanel.add(searchButton);
        searchPanel.add(overallSentimentLabel);
        searchPanel.add(errorLabel);

        return searchPanel;
    }

    private JPanel createArticlesPanel() {
        final JPanel articlesPanel = new JPanel(
                new GridLayout(1, 3, 10, 10)
        );

        configureTextArea(bearishArea);
        configureTextArea(neutralArea);
        configureTextArea(bullishArea);

        articlesPanel.add(
                createColumn("Bearish", bearishArea)
        );

        articlesPanel.add(
                createColumn("Neutral", neutralArea)
        );

        articlesPanel.add(
                createColumn("Bullish", bullishArea)
        );

        return articlesPanel;
    }

    private JPanel createColumn(
            String title,
            JTextArea textArea) {

        final JPanel column = new JPanel(
                new BorderLayout()
        );

        final JLabel titleLabel = new JLabel(
                title,
                JLabel.CENTER
        );

        final JScrollPane scrollPane =
                new JScrollPane(textArea);

        scrollPane.setPreferredSize(
                new Dimension(300, 500)
        );

        column.setBorder(
                BorderFactory.createEtchedBorder()
        );

        column.add(titleLabel, BorderLayout.NORTH);
        column.add(scrollPane, BorderLayout.CENTER);

        return column;
    }

    private void configureTextArea(JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        final NewsState state = newsViewModel.getState();

        errorLabel.setText(state.getErrorMessage());

        overallSentimentLabel.setText(
                "Overall sentiment for "
                        + state.getTicker()
                        + ": "
                        + state.getOverallSentiment()
        );

        bearishArea.setText("");
        neutralArea.setText("");
        bullishArea.setText("");

        for (NewsArticle article : state.getArticles()) {
            displayArticle(article);
        }
    }

    private void displayArticle(NewsArticle article) {
        final String articleText =
                article.getTitle()
                        + "\n"
                        + article.getSource()
                        + "\nScore: "
                        + article.getSentimentScore()
                        + "\n"
                        + article.getSummary()
                        + "\n"
                        + article.getUrl()
                        + "\n\n";

        final NewsSentiment sentiment =
                article.getSentiment();

        if (sentiment == NewsSentiment.BEARISH) {
            bearishArea.append(articleText);
        }
        else if (sentiment == NewsSentiment.BULLISH) {
            bullishArea.append(articleText);
        }
        else {
            neutralArea.append(articleText);
        }
    }
    /**
     * Returns the name used by CardLayout.
     *
     * @return the name of this view
     */
    public String getViewName() {
        return VIEW_NAME;
    }
}