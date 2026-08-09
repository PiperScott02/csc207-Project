package data_access;

import interface_adapter.watchlist.WatchlistState;
import use_case.watchlist.WatchlistDataAccessInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File-based Data Access Object for persisting user watchlist items across sessions.
 */
public class FileWatchlistDataAccessObject implements WatchlistDataAccessInterface {

    private final File csvFile;
    private final Map<String, WatchlistState.WatchlistStockItem> watchlist = new HashMap<>();

    public FileWatchlistDataAccessObject(String csvPath) {
        csvFile = new File(csvPath);

        // Load existing watchlist items from disk on startup if the file exists
        if (csvFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                String line;
                reader.readLine(); // Skip header row: Ticker,CompanyName,Close,DailyPriceChange
                while ((line = reader.readLine()) != null) {
                    final String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        final String ticker = parts[0];
                        final String companyName = parts[1];
                        final String close = (parts.length > 2 && !parts[2].equals("null")) ? parts[2] : "—";
                        final String change = (parts.length > 3 && !parts[3].equals("null")) ? parts[3] : "—";

                        final WatchlistState.WatchlistStockItem item =
                                new WatchlistState.WatchlistStockItem(ticker, companyName, close, change);
                        watchlist.put(ticker, item);
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Could not read watchlist file.", e);
            }
        }
    }

    @Override
    public void addWatchlistStock(String ticker) {
        // Initialize with default placeholders when adding via ticker alone
        final WatchlistState.WatchlistStockItem item =
                new WatchlistState.WatchlistStockItem(ticker, ticker, "—", "—");
        watchlist.put(ticker, item);
        saveToFile();
    }

    @Override
    public void removeWatchlistStock(String ticker) {
        watchlist.remove(ticker);
        saveToFile();
    }

    @Override
    public List<WatchlistState.WatchlistStockItem> getWatchlistItems() {
        return new ArrayList<>(watchlist.values());
    }

    @Override
    public boolean exists(String ticker) {
        return watchlist.containsKey(ticker);
    }

    /**
     * Helper method to write the current map state back to the CSV file.
     */
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write("Ticker,CompanyName,Close,DailyPriceChange\n");
            for (WatchlistState.WatchlistStockItem item : watchlist.values()) {
                final String close = item.getClose() != null ? item.getClose() : "";
                final String change = item.getDailyPriceChange() != null ? item.getDailyPriceChange() : "";
                writer.write(item.getTicker() + "," + item.getCompanyName() + "," + close + "," + change + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not save watchlist to file.", e);
        }
    }
}