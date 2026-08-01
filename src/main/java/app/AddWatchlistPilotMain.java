package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.add_watchlist.AddWatchlistController;
import interface_adapter.add_watchlist.AddWatchlistPresenter;
import interface_adapter.add_watchlist.AddWatchlistViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.StockDailyDataAccessInterface;
import use_case.add_watchlist.AddWatchlistInputBoundary;
import use_case.add_watchlist.AddWatchlistInteractor;
import use_case.add_watchlist.AddWatchlistOutputBoundary;
import view.AddWatchlistView;

import javax.swing.*;
import java.awt.*;

public class AddWatchlistPilotMain {

    public static AddWatchlistView create(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            AddWatchlistViewModel addWatchlistViewModel,
            StockDailyDataAccessInterface dataAccessObject) {

        // 1. Create the Presenter
        AddWatchlistOutputBoundary addWatchlistOutputBoundary = new AddWatchlistPresenter(
                addWatchlistViewModel,
                loggedInViewModel,
                viewManagerModel
        );

        // 2. Create the Interactor
        AddWatchlistInputBoundary addWatchlistInteractor = new AddWatchlistInteractor(
                dataAccessObject,
                addWatchlistOutputBoundary,
                loggedInViewModel
        );

        // 3. Create the Controller
        AddWatchlistController addWatchlistController = new AddWatchlistController(
                addWatchlistInteractor
        );

        // 4. Create and return the View (injecting ViewManagerModel so the Cancel button works)
        return new AddWatchlistView(addWatchlistViewModel, addWatchlistController, viewManagerModel);
    }
}