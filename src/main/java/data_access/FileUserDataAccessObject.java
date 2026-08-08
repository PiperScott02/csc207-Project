package data_access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import entity.*;
import interface_adapter.watchlist.WatchlistState;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.risk_preference.RiskPreferenceUserDataAccessInterface;
import use_case.delete_holding.DeleteHoldingUserDataAccessInterface;
import use_case.watchlist.WatchlistDataAccessInterface;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject
        implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        RiskPreferenceUserDataAccessInterface,
        DeleteHoldingUserDataAccessInterface,
        WatchlistDataAccessInterface {

    private String currentUser;
    private static final String HEADER = "username,password,holdings";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) throws IOException {

        csvFile = new File(csvPath);
        final File parentFolder = csvFile.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        if (!csvFile.exists()) {
            csvFile.createNewFile();
        }

        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("holdings", 2);

        if (csvFile.length() == 0) {
            save();
        } else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(
                            String.format(
                                    "Header should be:%n%s%nBut was:%n%s",
                                    HEADER,
                                    header
                            )
                    );
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    if (row.isBlank()) {
                        continue;
                    }

                    final String[] col = row.split(",", -1);

                    if (col.length < headers.size()) {
                        continue;
                    }

                    final String username = col[headers.get("username")].trim();
                    final String password = col[headers.get("password")];
                    final String holdingsString = col.length > headers.get("holdings") ? col[headers.get("holdings")] : "";

                    if (username.isEmpty()) {
                        continue;
                    }

                    final User user = userFactory.create(username, password);
                    parseAndRestoreHoldings(user, holdingsString);
                    accounts.put(username, user);
                }
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                List<StockHolding> holdings = (user.getPortfolio() != null) ? user.getPortfolio().getHoldings() : null;
                final String holdingsString = formatHoldingsToString(holdings);
                final String line = String.format("%s,%s,%s",
                        user.getName(), user.getPassword(), holdingsString);
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String formatHoldingsToString(List<StockHolding> holdings) {
        if (holdings == null || holdings.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < holdings.size(); i++) {
            StockHolding h = holdings.get(i);
            if (h.getStock() != null) {
                Stock s = h.getStock();
                String ticker = s.getTickerSymbol() != null ? s.getTickerSymbol() : "";
                double shares = h.getNumberOfShares();
                double purchasePrice = h.getAveragePrice();
                double currentPrice = s.getClose() != null ? s.getClose().doubleValue() : purchasePrice;
                String company = s.getCompanyName() != null ? s.getCompanyName() : "";
                double dailyChange = s.getDailyPriceChange() != null ? s.getDailyPriceChange().doubleValue() : 0.0;

                // Format: Ticker : Shares : PurchasePrice : CurrentPrice : CompanyName : DailyChange
                sb.append(ticker)
                        .append(":")
                        .append(shares)
                        .append(":")
                        .append(purchasePrice)
                        .append(":")
                        .append(currentPrice)
                        .append(":")
                        .append(company)
                        .append(":")
                        .append(dailyChange);

                if (i < holdings.size() - 1) {
                    sb.append(";");
                }
            }
        }
        return sb.toString();
    }

    private void parseAndRestoreHoldings(User user, String holdingsString) {
        if (holdingsString == null || holdingsString.isBlank()) {
            return;
        }
        if (user.getPortfolio() == null) {
            return;
        }
        String[] items = holdingsString.split(";");
        for (String item : items) {
            String[] details = item.split(":");
            if (details.length >= 2) {
                String ticker = details[0];
                try {
                    double shares = Double.parseDouble(details[1]);
                    double purchasePrice = details.length >= 3 ? Double.parseDouble(details[2]) : 0.0;
                    double currentPrice = details.length >= 4 ? Double.parseDouble(details[3]) : purchasePrice;
                    String company = details.length >= 5 ? details[4] : "";
                    double dailyChange = details.length >= 6 ? Double.parseDouble(details[5]) : 0.0;

                    Stock stock = new Stock();
                    stock.setTickerSymbol(ticker);
                    stock.setClose(java.math.BigDecimal.valueOf(currentPrice));
                    stock.setCompanyName(company);
                    stock.setDailyChange(java.math.BigDecimal.valueOf(dailyChange));

                    StockHolding holding = new StockHolding();
                    holding.setStock(stock);

                    Transaction transaction = new Transaction();
                    transaction.setDate(LocalDate.now());
                    transaction.setPricePerShare(java.math.BigDecimal.valueOf(purchasePrice));
                    transaction.setNumberOfShares(shares);
                    transaction.setType(TransactionType.BUY);

                    holding.getTransactions().add(transaction);
                    user.getPortfolio().addHolding(holding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getName(), user);
        this.save();
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUser(String name) {
        currentUser = name;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getName(), user);
        save();
    }

    @Override
    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void addWatchlistStock(String ticker) {
        User user = get(currentUser);
        if (user != null && user.getPortfolio() != null) {
            if (!exists(ticker)) {
                WatchlistStockItem item = new WatchlistStockItem(ticker, ticker, null, null);
                user.getPortfolio().getWatchlist().add(item);
                save();
            }
        }
    }

    @Override
    public void removeWatchlistStock(String ticker) {
        User user = get(currentUser);
        if (user != null && user.getPortfolio() != null && user.getPortfolio().getWatchlist() != null) {
            user.getPortfolio().getWatchlist().removeIf(item -> item.ticker().equalsIgnoreCase(ticker));
            save();
        }
    }

    @Override
    public List<WatchlistState.WatchlistStockItem> getWatchlistItems() {
        User user = get(currentUser);
        if (user != null && user.getPortfolio() != null) {
            List<WatchlistStockItem> rawItems = user.getPortfolio().getWatchlist();
            List<WatchlistState.WatchlistStockItem> stateItems = new java.util.ArrayList<>();
            for (WatchlistStockItem item : rawItems) {
                stateItems.add(new WatchlistState.WatchlistStockItem(
                        item.ticker(),
                        item.companyName() != null ? item.companyName() : "",
                        item.closePrice() != null ? item.closePrice().toString() : "",
                        item.dailyPriceChange() != null ? item.dailyPriceChange().toString() : ""
                ));
            }
            return stateItems;
        }
        return new java.util.ArrayList<>();
    }

    @Override
    public boolean exists(String ticker) {
        User user = get(currentUser);
        if (user != null && user.getPortfolio() != null && user.getPortfolio().getWatchlist() != null) {
            for (WatchlistStockItem item : user.getPortfolio().getWatchlist()) {
                if (item.ticker().equalsIgnoreCase(ticker)) {
                    return true;
                }
            }
        }
        return false;
    }
}