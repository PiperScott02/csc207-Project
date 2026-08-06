package view;

import entity.NewsArticle;
import entity.NewsSentiment;
import interface_adapter.ViewManagerModel;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Displays stock news grouped by sentiment.
 */
public class NewsView extends JPanel
        implements PropertyChangeListener {

    public static final String VIEW_NAME = "news";

    private static final String LOGGED_IN_VIEW_NAME = "logged in";

    private static final Color PAGE_BACKGROUND =
            new Color(245, 247, 250);
    private static final Color TEXT_COLOR =
            new Color(24, 38, 58);
    private static final Color SEARCH_BLUE =
            new Color(35, 101, 219);

    /*
     * Requested colour mapping:
     * bearish = green, neutral = white, bullish = red.
     */
    private static final Color BEARISH_COLOR =
            new Color(28, 135, 64);
    private static final Color BULLISH_COLOR =
            new Color(198, 40, 40);
    private static final Color NEUTRAL_COLOR = Color.WHITE;
    private static final Color NEUTRAL_TEXT_COLOR =
            new Color(65, 75, 90);
    private static final Color BORDER_COLOR =
            new Color(190, 198, 208);

    private final NewsViewModel newsViewModel;
    private final NewsController newsController;

    private final JTextField tickerField = new JTextField(14);
    private final JLabel errorLabel =
            new JLabel(" ", SwingConstants.CENTER);
    private final JLabel overallContextLabel =
            new JLabel("Overall news sentiment", SwingConstants.CENTER);
    private final JLabel overallSentimentLabel =
            new JLabel("SEARCH A TICKER", SwingConstants.CENTER);

    private final JLabel bearishTitleLabel =
            createColumnTitle("Bearish", BEARISH_COLOR, Color.WHITE);
    private final JLabel neutralTitleLabel =
            createColumnTitle("Neutral", NEUTRAL_COLOR, NEUTRAL_TEXT_COLOR);
    private final JLabel bullishTitleLabel =
            createColumnTitle("Bullish", BULLISH_COLOR, Color.WHITE);

    private final JTextArea bearishArea = new JTextArea();
    private final JTextArea neutralArea = new JTextArea();
    private final JTextArea bullishArea = new JTextArea();

    private final JPanel overallPanel = new JPanel(new BorderLayout());

    public NewsView(
            NewsViewModel newsViewModel,
            NewsController newsController,
            ViewManagerModel viewManagerModel) {

        this.newsViewModel = newsViewModel;
        this.newsController = newsController;

        newsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(0, 12));
        setBackground(PAGE_BACKGROUND);
        setBorder(new EmptyBorder(14, 18, 18, 18));

        add(createTopPanel(viewManagerModel), BorderLayout.NORTH);
        add(createArticlesPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel(ViewManagerModel viewManagerModel) {
        final JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);

        topPanel.add(createBackPanel(viewManagerModel), BorderLayout.NORTH);
        topPanel.add(createSearchPanel(), BorderLayout.CENTER);
        topPanel.add(createOverallPanel(), BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel createBackPanel(ViewManagerModel viewManagerModel) {
        final JPanel backPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );
        backPanel.setOpaque(false);

        final JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 17));
        backButton.setFocusPainted(false);

        backButton.addActionListener(event -> {
            viewManagerModel.setState(LOGGED_IN_VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        backPanel.add(backButton);
        return backPanel;
    }

    private JPanel createSearchPanel() {
        final JPanel searchPanel = new JPanel(new BorderLayout(0, 6));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        new EmptyBorder(10, 16, 8, 16)
                )
        );

        final JLabel titleLabel = new JLabel(
                "Search for a company or stock",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);

        final JPanel searchRow = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 16, 6)
        );
        searchRow.setBackground(Color.WHITE);

        final JLabel tickerLabel = new JLabel("Ticker:");
        tickerLabel.setFont(new Font("SansSerif", Font.BOLD, 21));
        tickerLabel.setForeground(TEXT_COLOR);

        tickerField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        tickerField.setPreferredSize(new Dimension(330, 42));

        final JButton searchButton = new JButton("Search News");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        searchButton.setForeground(Color.BLACK);
        searchButton.setBackground(SEARCH_BLUE);
        searchButton.setPreferredSize(new Dimension(180, 42));
        searchButton.setFocusPainted(false);
        searchButton.setOpaque(true);

        searchButton.addActionListener(event -> searchForNews());
        tickerField.addActionListener(event -> searchForNews());

        searchRow.add(tickerLabel);
        searchRow.add(tickerField);
        searchRow.add(searchButton);

        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        errorLabel.setForeground(BULLISH_COLOR);

        searchPanel.add(titleLabel, BorderLayout.NORTH);
        searchPanel.add(searchRow, BorderLayout.CENTER);
        searchPanel.add(errorLabel, BorderLayout.SOUTH);

        return searchPanel;
    }

    private JPanel createOverallPanel() {
        overallPanel.setBackground(NEUTRAL_COLOR);
        overallPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        new EmptyBorder(10, 16, 10, 16)
                )
        );

        overallContextLabel.setFont(
                new Font("SansSerif", Font.BOLD, 21)
        );
        overallContextLabel.setForeground(NEUTRAL_TEXT_COLOR);

        overallSentimentLabel.setFont(
                new Font("SansSerif", Font.BOLD, 34)
        );
        overallSentimentLabel.setForeground(NEUTRAL_TEXT_COLOR);

        overallPanel.add(overallContextLabel, BorderLayout.NORTH);
        overallPanel.add(overallSentimentLabel, BorderLayout.CENTER);

        return overallPanel;
    }

    private JPanel createArticlesPanel() {
        final JPanel articlesPanel = new JPanel(
                new GridLayout(1, 3, 12, 0)
        );
        articlesPanel.setOpaque(false);

        configureTextArea(bearishArea);
        configureTextArea(neutralArea);
        configureTextArea(bullishArea);

        articlesPanel.add(
                createColumn(bearishTitleLabel, bearishArea)
        );
        articlesPanel.add(
                createColumn(neutralTitleLabel, neutralArea)
        );
        articlesPanel.add(
                createColumn(bullishTitleLabel, bullishArea)
        );

        return articlesPanel;
    }

    private JPanel createColumn(
            JLabel titleLabel,
            JTextArea textArea) {

        final JPanel column = new JPanel(new BorderLayout());
        column.setBackground(Color.WHITE);
        column.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setBorder(null);

        column.add(titleLabel, BorderLayout.NORTH);
        column.add(scrollPane, BorderLayout.CENTER);

        return column;
    }

    private static JLabel createColumnTitle(
            String title,
            Color background,
            Color foreground) {

        final JLabel titleLabel = new JLabel(
                title + " (0)",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBackground(background);
        titleLabel.setForeground(foreground);
        titleLabel.setOpaque(true);
        titleLabel.setBorder(new EmptyBorder(10, 8, 10, 8));

        return titleLabel;
    }

    private void configureTextArea(JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setForeground(TEXT_COLOR);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(new EmptyBorder(10, 12, 10, 12));
    }

    private void searchForNews() {
        newsController.execute(tickerField.getText());
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        final NewsState state = newsViewModel.getState();
        final String errorMessage = state.getErrorMessage();

        errorLabel.setText(
                errorMessage == null || errorMessage.isBlank()
                        ? " "
                        : errorMessage
        );

        bearishArea.setText("");
        neutralArea.setText("");
        bullishArea.setText("");

        int bearishCount = 0;
        int neutralCount = 0;
        int bullishCount = 0;

        final List<NewsArticle> articles = state.getArticles();
        for (NewsArticle article : articles) {
            displayArticle(article);

            if (article.getSentiment() == NewsSentiment.BEARISH) {
                bearishCount++;
            }
            else if (article.getSentiment() == NewsSentiment.BULLISH) {
                bullishCount++;
            }
            else {
                neutralCount++;
            }
        }

        bearishTitleLabel.setText("Bearish (" + bearishCount + ")");
        neutralTitleLabel.setText("Neutral (" + neutralCount + ")");
        bullishTitleLabel.setText("Bullish (" + bullishCount + ")");

        if (errorMessage == null || errorMessage.isBlank()) {
            updateOverallPanel(
                    state.getTicker(),
                    state.getOverallSentiment()
            );
        }
        else {
            overallContextLabel.setText("Overall news sentiment");
            overallSentimentLabel.setText("NO RESULT");
            setOverallColors(NEUTRAL_COLOR, NEUTRAL_TEXT_COLOR);
        }
    }

    private void updateOverallPanel(
            String ticker,
            NewsSentiment sentiment) {

        overallContextLabel.setText(
                "Overall sentiment for " + ticker
        );
        overallSentimentLabel.setText(sentiment.toString());

        if (sentiment == NewsSentiment.BEARISH) {
            setOverallColors(BEARISH_COLOR, Color.WHITE);
        }
        else if (sentiment == NewsSentiment.BULLISH) {
            setOverallColors(BULLISH_COLOR, Color.WHITE);
        }
        else {
            setOverallColors(NEUTRAL_COLOR, NEUTRAL_TEXT_COLOR);
        }
    }

    private void setOverallColors(
            Color background,
            Color foreground) {

        overallPanel.setBackground(background);
        overallContextLabel.setForeground(foreground);
        overallSentimentLabel.setForeground(foreground);
    }

    private void displayArticle(NewsArticle article) {
        final String articleText =
                "• "
                        + article.getTitle()
                        + "\n"
                        + article.getSource()
                        + " | Sentiment: "
                        + String.format("%.2f", article.getSentimentScore())
                        + " | Relevance: "
                        + String.format("%.2f", article.getRelevanceScore())
                        + "\n"
                        + article.getSummary()
                        + "\n"
                        + article.getUrl()
                        + "\n\n";

        if (article.getSentiment() == NewsSentiment.BEARISH) {
            bearishArea.append(articleText);
        }
        else if (article.getSentiment() == NewsSentiment.BULLISH) {
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
