package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SidebarHelper {

    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color SIDEBAR_BG = new Color(7, 10, 17);
    private static final Color CARD_BG = new Color(17, 24, 39);
    private static final Color BORDER_COLOR = new Color(31, 41, 55);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color SIDEBAR_ACTIVE = new Color(17, 24, 39);

    public static JPanel createSidebar(String activeViewName,
                                       JComponent parentComponent,
                                       ViewManagerModel viewManagerModel,
                                       LoggedInViewModel loggedInViewModel,
                                       BlackLittermanController blackLittermanController,
                                       PortfolioHealthController portfolioHealthController) {

        final JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(240, 0));
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        final JPanel brandPanel = new JPanel();
        brandPanel.setBackground(SIDEBAR_BG);
        brandPanel.setPreferredSize(new Dimension(240, 70));
        brandPanel.setLayout(null);

        final JLabel logoBadge = new JLabel("P", SwingConstants.CENTER);
        logoBadge.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoBadge.setForeground(TEXT_MAIN);
        logoBadge.setBackground(ACCENT_GREEN);
        logoBadge.setOpaque(true);
        logoBadge.setBounds(20, 20, 28, 28);

        final JLabel brandLabel = new JLabel("PortfolioPilot");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        brandLabel.setForeground(TEXT_MAIN);
        brandLabel.setBounds(58, 20, 150, 28);

        brandPanel.add(logoBadge);
        brandPanel.add(brandLabel);

        final JPanel navLinksPanel = new JPanel();
        navLinksPanel.setBackground(SIDEBAR_BG);
        navLinksPanel.setLayout(new GridLayout(10, 1, 0, 2));
        navLinksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        navLinksPanel.add(createSidebarNavLink("Overview", "Overview".equals(activeViewName), e -> {
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Holdings", "Holdings".equals(activeViewName), e -> {
            viewManagerModel.setState("holdings");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Watchlist", "Watchlist".equals(activeViewName), e -> {
            viewManagerModel.setState("watchlist");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("News & Sentiment", "News & Sentiment".equals(activeViewName), e -> {
            viewManagerModel.setState("news");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Portfolio Health", "Portfolio Health".equals(activeViewName), e -> {
            LoggedInState state = loggedInViewModel != null ? loggedInViewModel.getState() : null;
            if (state != null && state.getUser() != null) {
                if (portfolioHealthController != null) {
                    portfolioHealthController.execute(state.getUser());
                }
            } else {
                JOptionPane.showMessageDialog(parentComponent, "No active user session found.");
            }
            viewManagerModel.setState("portfolio health");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Risk Preference", "Risk Preference".equals(activeViewName), e -> {
            viewManagerModel.setState("risk preference");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Currency", "Currency".equals(activeViewName), e -> {
            viewManagerModel.setState("currency conversion");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Search Stocks", "Search Stocks".equals(activeViewName), e -> {
            viewManagerModel.setState("search");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Black-Litterman", "Black-Litterman".equals(activeViewName), e -> {
            LoggedInState state = loggedInViewModel != null ? loggedInViewModel.getState() : null;
            if (state != null && state.getUser() != null) {
                if (blackLittermanController != null) {
                    blackLittermanController.loadMarketData(state.getUser());
                }
            } else {
                JOptionPane.showMessageDialog(parentComponent, "No active user session found.");
            }
            viewManagerModel.setState("Black-Litterman view");
            viewManagerModel.firePropertyChanged();
        }));
        navLinksPanel.add(createSidebarNavLink("Stress Test", "Stress Test".equals(activeViewName), e -> {
            viewManagerModel.setState("stress test");
            viewManagerModel.firePropertyChanged();
        }));

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(SIDEBAR_BG);
        bottomPanel.setPreferredSize(new Dimension(240, 95));
        bottomPanel.setLayout(null);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        // Helper method to fetch the current username dynamically
        final JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        welcomeLabel.setForeground(TEXT_MUTED);
        welcomeLabel.setBounds(20, 12, 180, 15);

        // Update username text logic
        Runnable updateUsernameText = () -> {
            LoggedInState currentState = loggedInViewModel != null ? loggedInViewModel.getState() : null;
            String username = (currentState != null && currentState.getUsername() != null && !currentState.getUsername().isBlank())
                    ? currentState.getUsername().toUpperCase()
                    : "USER";
            welcomeLabel.setText("WELCOME, " + username);
        };

        // Initialize label text right away
        updateUsernameText.run();

        // Listen for future state changes so it updates automatically when logging in/switching users
        if (loggedInViewModel != null) {
            loggedInViewModel.addPropertyChangeListener(evt -> updateUsernameText.run());
        }

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        final JLabel dateLabel = new JLabel(currentDate);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        dateLabel.setForeground(TEXT_MUTED);
        dateLabel.setBounds(20, 30, 180, 15);

        final JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 11));
        logoutButton.setForeground(TEXT_MAIN);
        logoutButton.setBackground(CARD_BG);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBounds(20, 52, 200, 30);
        logoutButton.addActionListener(e -> {
            viewManagerModel.setState("log in");
            viewManagerModel.firePropertyChanged();
        });

        bottomPanel.add(welcomeLabel);
        bottomPanel.add(dateLabel);
        bottomPanel.add(logoutButton);

        sidebarPanel.add(brandPanel, BorderLayout.NORTH);
        sidebarPanel.add(navLinksPanel, BorderLayout.CENTER);
        sidebarPanel.add(bottomPanel, BorderLayout.SOUTH);

        return sidebarPanel;
    }

    private static JButton createSidebarNavLink(String text, boolean isActive, ActionListener action) {
        final JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 13));
        button.setForeground(isActive ? TEXT_MAIN : TEXT_MUTED);
        button.setBackground(isActive ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        button.addActionListener(action);
        return button;
    }
}