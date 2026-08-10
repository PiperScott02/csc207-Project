package view;

import entity.NewsArticle;
import entity.NewsSentiment;
import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.news.NewsController;
import interface_adapter.news.NewsState;
import interface_adapter.news.NewsViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.List;

/**
 * Displays stock news grouped by sentiment.
 */
public class NewsView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = "news";

    /**
     * Dark UI color palette.
     */
    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color ERROR_RED = new Color(248, 113, 113);

    /*
     * Project sentiment mapping: bearish = green, neutral = white,
     * and bullish = red.
     */
    private static final Color BEARISH_COLOR = new Color(198, 40, 40);
    private static final Color BULLISH_COLOR = new Color(28, 135, 64);
    private static final Color NEUTRAL_COLOR = Color.WHITE;
    private static final Color NEUTRAL_TEXT_COLOR = new Color(31, 41, 55);

    private final NewsViewModel newsViewModel;
    private final NewsController newsController;

    private final JTextField tickerField = new JTextField(14);
    private final JLabel errorLabel = new JLabel(" ");
    private final JLabel overallContextLabel =
            new JLabel("OVERALL NEWS SENTIMENT");
    private final JLabel overallSentimentLabel =
            new JLabel("SEARCH A TICKER");

    private final JLabel bearishTitleLabel =
            createColumnTitle("Bearish", BEARISH_COLOR, Color.WHITE);
    private final JLabel neutralTitleLabel =
            createColumnTitle("Neutral", NEUTRAL_COLOR, NEUTRAL_TEXT_COLOR);
    private final JLabel bullishTitleLabel =
            createColumnTitle("Bullish", BULLISH_COLOR, Color.WHITE);

    private final JPanel bearishArticlesPanel = createArticleListPanel();
    private final JPanel neutralArticlesPanel = createArticleListPanel();
    private final JPanel bullishArticlesPanel = createArticleListPanel();

    private final JPanel overallPanel = new JPanel(new BorderLayout());

    public NewsView(NewsViewModel newsViewModel,
                    NewsController newsController,
                    ViewManagerModel viewManagerModel,
                    LoggedInViewModel loggedInViewModel,
                    BlackLittermanController blackLittermanController,
                    PortfolioHealthController portfolioHealthController) {

        this.newsViewModel = newsViewModel;
        this.newsController = newsController;

        newsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(BG_DARK);

        add(SidebarHelper.createSidebar("News & Sentiment",
                this,
                viewManagerModel,
                loggedInViewModel,
                blackLittermanController,
                portfolioHealthController), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainContentPanel() {
        final JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        final JLabel pageTitle = new JLabel("News & Sentiment");
        pageTitle.setFont(new Font("Serif", Font.BOLD, 26));
        pageTitle.setForeground(TEXT_MAIN);
        pageTitle.setBorder(new EmptyBorder(0, 0, 5, 0));

        final JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(BG_DARK);

        final JPanel topCards = new JPanel();
        topCards.setLayout(new BoxLayout(topCards, BoxLayout.Y_AXIS));
        topCards.setBackground(BG_DARK);
        topCards.add(createSearchPanel());
        topCards.add(Box.createRigidArea(new Dimension(0, 12)));
        topCards.add(createOverallPanel());

        contentPanel.add(topCards, BorderLayout.NORTH);
        contentPanel.add(createArticlesPanel(), BorderLayout.CENTER);

        mainPanel.add(pageTitle, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createSearchPanel() {
        final JPanel searchPanel = new JPanel(new BorderLayout(0, 8));
        searchPanel.setBackground(CARD_BG);
        searchPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 100)
        );
        searchPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        new EmptyBorder(12, 20, 10, 20)
                )
        );

        final JLabel sectionLabel = new JLabel("SEARCH COMPANY NEWS");
        sectionLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        sectionLabel.setForeground(TEXT_MUTED);

        final JPanel searchRow = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 4)
        );
        searchRow.setBackground(CARD_BG);

        final JLabel tickerLabel = new JLabel("Ticker");
        tickerLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        tickerLabel.setForeground(TEXT_MAIN);

        tickerField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tickerField.setPreferredSize(new Dimension(260, 36));
        tickerField.setBackground(BG_DARK);
        tickerField.setForeground(TEXT_MAIN);
        tickerField.setCaretColor(TEXT_MAIN);
        tickerField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        new EmptyBorder(4, 10, 4, 10)
                )
        );

        final JButton searchButton = new JButton("Search News");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchButton.setForeground(Color.BLACK);
        searchButton.setBackground(ACCENT_GREEN);
        searchButton.setPreferredSize(new Dimension(145, 36));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setOpaque(true);

        searchButton.addActionListener(event -> searchForNews());
        tickerField.addActionListener(event -> searchForNews());

        searchRow.add(tickerLabel);
        searchRow.add(tickerField);
        searchRow.add(searchButton);

        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setForeground(ERROR_RED);

        searchPanel.add(sectionLabel, BorderLayout.NORTH);
        searchPanel.add(searchRow, BorderLayout.CENTER);
        searchPanel.add(errorLabel, BorderLayout.SOUTH);
        return searchPanel;
    }

    private JPanel createOverallPanel() {
        overallPanel.setBackground(CARD_BG);
        overallPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 100)
        );
        overallPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        new EmptyBorder(14, 20, 14, 20)
                )
        );

        overallContextLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        overallContextLabel.setForeground(TEXT_MUTED);

        overallSentimentLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        overallSentimentLabel.setForeground(TEXT_MAIN);

        overallPanel.add(overallContextLabel, BorderLayout.NORTH);
        overallPanel.add(overallSentimentLabel, BorderLayout.CENTER);
        return overallPanel;
    }

    private JPanel createArticlesPanel() {
        final JPanel articlesPanel = new JPanel(
                new GridLayout(1, 3, 15, 0)
        );
        articlesPanel.setBackground(BG_DARK);

        articlesPanel.add(createColumn(
                bearishTitleLabel,
                bearishArticlesPanel
        ));
        articlesPanel.add(createColumn(
                neutralTitleLabel,
                neutralArticlesPanel
        ));
        articlesPanel.add(createColumn(
                bullishTitleLabel,
                bullishArticlesPanel
        ));
        return articlesPanel;
    }

    private JPanel createColumn(
            JLabel titleLabel,
            JPanel articleListPanel) {

        final JPanel column = new JPanel(new BorderLayout());
        column.setBackground(CARD_BG);
        column.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        final JScrollPane scrollPane = new JScrollPane(articleListPanel);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setPreferredSize(new Dimension(250, 320));
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBackground(background);
        titleLabel.setForeground(foreground);
        titleLabel.setOpaque(true);
        titleLabel.setBorder(new EmptyBorder(9, 8, 9, 8));
        return titleLabel;
    }

    private static JPanel createArticleListPanel() {
        return new ArticleListPanel();
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

        bearishArticlesPanel.removeAll();
        neutralArticlesPanel.removeAll();
        bullishArticlesPanel.removeAll();

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

        refreshArticlePanels();

        if (errorMessage == null || errorMessage.isBlank()) {
            updateOverallPanel(
                    state.getTicker(),
                    state.getOverallSentiment()
            );
        }
        else {
            overallContextLabel.setText("OVERALL NEWS SENTIMENT");
            overallSentimentLabel.setText("NO RESULT");
            overallSentimentLabel.setForeground(TEXT_MUTED);
        }
    }

    private void updateOverallPanel(
            String ticker,
            NewsSentiment sentiment) {

        overallContextLabel.setText(
                "OVERALL SENTIMENT FOR " + ticker
        );
        overallSentimentLabel.setText(sentiment.toString());

        if (sentiment == NewsSentiment.BEARISH) {
            overallSentimentLabel.setForeground(BEARISH_COLOR);
        }
        else if (sentiment == NewsSentiment.BULLISH) {
            overallSentimentLabel.setForeground(BULLISH_COLOR);
        }
        else {
            overallSentimentLabel.setForeground(NEUTRAL_COLOR);
        }
    }

    private void displayArticle(NewsArticle article) {
        final JPanel articleCard = createArticleCard(article);
        if (article.getSentiment() == NewsSentiment.BEARISH) {
            addArticleCard(bearishArticlesPanel, articleCard);
        }
        else if (article.getSentiment() == NewsSentiment.BULLISH) {
            addArticleCard(bullishArticlesPanel, articleCard);
        }
        else {
            addArticleCard(neutralArticlesPanel, articleCard);
        }
    }

    private JPanel createArticleCard(NewsArticle article) {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(22, 30, 46));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        new EmptyBorder(10, 10, 9, 10)
                )
        );

        final JTextArea title = createWrappedText(
                shorten(article.getTitle(), 95),
                Font.BOLD,
                14,
                TEXT_MAIN,
                48
        );

        final String scoreText = String.format(
                "%s  •  Sentiment %.2f  •  Relevance %.0f%%",
                shorten(article.getSource(), 28),
                article.getSentimentScore(),
                article.getRelevanceScore() * 100.0
        );
        final JTextArea scores = createWrappedText(
                scoreText,
                Font.PLAIN,
                11,
                TEXT_MUTED,
                30
        );

        final JTextArea summary = createWrappedText(
                shorten(article.getSummary(), 155),
                Font.PLAIN,
                12,
                TEXT_MUTED,
                58
        );

        final JButton openButton = new JButton("Open article ↗");
        openButton.setFont(new Font("SansSerif", Font.BOLD, 11));
        openButton.setForeground(Color.WHITE);
        openButton.setContentAreaFilled(false);
        openButton.setBorderPainted(false);
        openButton.setFocusPainted(false);
        openButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        openButton.setBorder(new EmptyBorder(3, 0, 0, 0));
        openButton.addActionListener(
                event -> openArticle(article.getUrl())
        );

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(scores);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(summary);
        card.add(openButton);
        return card;
    }

    private JTextArea createWrappedText(
            String text,
            int style,
            int size,
            Color color,
            int height) {

        final JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setFont(new Font("SansSerif", style, size));
        textArea.setForeground(color);
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        textArea.setMinimumSize(new Dimension(0, height));
        textArea.setPreferredSize(new Dimension(0, height));
        textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        textArea.setBorder(null);
        return textArea;
    }

    /**
     * Keeps every article card the same width as the visible scroll area.
     * This makes wrapped text stop before the vertical scrollbar.
     */
    private static final class ArticleListPanel extends JPanel
            implements Scrollable {

        private static final int SCROLL_INCREMENT = 14;

        private ArticleListPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(CARD_BG);
            setBorder(new EmptyBorder(10, 10, 10, 16));
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(
                Rectangle visibleRectangle,
                int orientation,
                int direction) {

            return SCROLL_INCREMENT;
        }

        @Override
        public int getScrollableBlockIncrement(
                Rectangle visibleRectangle,
                int orientation,
                int direction) {

            return Math.max(
                    SCROLL_INCREMENT,
                    visibleRectangle.height - SCROLL_INCREMENT
            );
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private void addArticleCard(
            JPanel articleListPanel,
            JPanel articleCard) {

        articleListPanel.add(articleCard);
        articleListPanel.add(Box.createRigidArea(new Dimension(0, 9)));
    }

    private void refreshArticlePanels() {
        bearishArticlesPanel.revalidate();
        bearishArticlesPanel.repaint();
        neutralArticlesPanel.revalidate();
        neutralArticlesPanel.repaint();
        bullishArticlesPanel.revalidate();
        bullishArticlesPanel.repaint();
    }

    private String shorten(String text, int maximumLength) {
        if (text == null || text.isBlank()) {
            return "Not available";
        }
        if (text.length() <= maximumLength) {
            return text;
        }
        return text.substring(0, maximumLength - 1).trim() + "…";
    }

    private void openArticle(String url) {
        if (url == null || url.isBlank()) {
            errorLabel.setText("This article does not have a link.");
            return;
        }

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(url));
            }
            else {
                errorLabel.setText("Opening links is not supported.");
            }
        }
        catch (RuntimeException | java.io.IOException exception) {
            errorLabel.setText("Unable to open this article.");
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