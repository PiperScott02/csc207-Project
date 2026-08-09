package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.black_litterman.BlackLittermanController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.portfolio_health.PortfolioHealthController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SidebarHelper {

    private static final Color BG_DARK = new Color(11, 15, 25);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);

    public static JPanel createSidebar(String activeViewName,
                                       JComponent parentComponent,
                                       ViewManagerModel viewManagerModel,
                                       LoggedInViewModel loggedInViewModel,
                                       BlackLittermanController blackLittermanController,
                                       PortfolioHealthController portfolioHealthController) {

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_DARK);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        // 1. App Logo / Title Header
        JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoWrapper.setBackground(BG_DARK);
        logoWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel iconBadge = new JLabel("P");
        iconBadge.setOpaque(true);
        iconBadge.setBackground(ACCENT_GREEN);
        iconBadge.setForeground(Color.WHITE);
        iconBadge.setFont(new Font("SansSerif", Font.BOLD, 12));
        iconBadge.setHorizontalAlignment(SwingConstants.CENTER);
        iconBadge.setPreferredSize(new Dimension(24, 24));

        JLabel logoLabel = new JLabel("PortfolioPilot");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoLabel.setForeground(TEXT_MAIN);

        logoWrapper.add(iconBadge);
        logoWrapper.add(logoLabel);

        sidebar.add(logoWrapper);
        sidebar.add(Box.createRigidArea(new Dimension(0, 35)));

        // 2. Navigation Links List
        String[] navItems = {
                "Overview", "Holdings", "Watchlist",
                "News & Sentiment", "Portfolio Health",
                "Risk Preference", "Currency", "Search Stocks", "Black-Litterman"
        };

        for (String item : navItems) {
            boolean isActive = item.equals(activeViewName);
            sidebar.add(createNavLinkButton(item, isActive, parentComponent, viewManagerModel, loggedInViewModel, blackLittermanController, portfolioHealthController));
            sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        // 3. Bottom User Profile & Logout Section
        sidebar.add(Box.createVerticalGlue());

        LoggedInState currentState = loggedInViewModel != null ? loggedInViewModel.getState() : null;
        String username = (currentState != null && currentState.getUsername() != null && !currentState.getUsername().isBlank())
                ? currentState.getUsername().toUpperCase()
                : "";

        JLabel userLabel = new JLabel(username.isEmpty() ? "WELCOME" : "WELCOME, " + username);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        userLabel.setForeground(TEXT_MUTED);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(userLabel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));

        // Dynamically formatted current date
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        JLabel dateLabel = new JLabel(now.format(dateFormatter));
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dateLabel.setForeground(TEXT_MUTED);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(dateLabel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        // Logout Button
        JLabel logoutBtn = new JLabel("↳ Log Out");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setForeground(new Color(239, 68, 68));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewManagerModel.setState("log in");
                viewManagerModel.firePropertyChanged();
            }
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private static JComponent createNavLinkButton(String title, boolean isActive,
                                                  JComponent parentComponent,
                                                  ViewManagerModel viewManagerModel,
                                                  LoggedInViewModel loggedInViewModel,
                                                  BlackLittermanController blackLittermanController,
                                                  PortfolioHealthController portfolioHealthController) {
        JLabel link = new JLabel(title);
        link.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 13));
        link.setForeground(isActive ? TEXT_MAIN : TEXT_MUTED);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.setAlignmentX(Component.LEFT_ALIGNMENT);

        link.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LoggedInState state = loggedInViewModel != null ? loggedInViewModel.getState() : null;

                if (title.equals("Overview")) {
                    viewManagerModel.setState("logged in");
                    viewManagerModel.firePropertyChanged();
                }
                else if (title.equals("Holdings")) {
                    viewManagerModel.setState("holdings");
                    viewManagerModel.firePropertyChanged();
                }
                else if (title.equals("Watchlist")) {
                    viewManagerModel.setState("watchlist");
                    viewManagerModel.firePropertyChanged();
                }
                else if (title.equals("News & Sentiment")) {
                    viewManagerModel.setState("news");
                    viewManagerModel.firePropertyChanged();
                }
                else if (title.equals("Portfolio Health")) {
                    if (state != null && state.getUser() != null) {
                        if (portfolioHealthController != null) {
                            portfolioHealthController.execute(state.getUser());
                        }
                    } else {
                        JOptionPane.showMessageDialog(parentComponent, "No active user session found.");
                    }
                }
                else if (title.equals("Risk Preference")) {
                    viewManagerModel.setState("risk preference");
                    viewManagerModel.firePropertyChanged();
                }
                else if (title.equals("Currency")) {
                    viewManagerModel.setState("currency conversion");
                    viewManagerModel.firePropertyChanged();
                }
                else if (title.equals("Search Stocks")) {
                    viewManagerModel.setState("search");
                    viewManagerModel.firePropertyChanged();
                }
                else if (title.equals("Black-Litterman")) {
                    if (state != null && state.getUser() != null) {
                        if (blackLittermanController != null) {
                            blackLittermanController.loadMarketData(state.getUser());
                        }
                    } else {
                        JOptionPane.showMessageDialog(parentComponent, "No active user session found.");
                    }
                    viewManagerModel.setState("Black-Litterman view");
                    viewManagerModel.firePropertyChanged();
                }
            }
        });

        return link;
    }
}