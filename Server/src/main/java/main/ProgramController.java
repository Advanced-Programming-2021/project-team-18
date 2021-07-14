package main;

import data.DataManager;
import data.Printer;
import data.api.APIServer;
import menus.LoginMenu;
import view.App;

public class ProgramController {

    public void runConsole() {
        Printer.greetings();
        DataManager.loadCardsIntoAllCards();
        DataManager.initializeAIDeck();
        DataManager.loadUsersData();
        DataManager.initializeAIDeck();
        new LoginMenu().runMenu();
        DataManager.saveUsersData();
    }

    public void runGraphic(String[] args) {
        DataManager.loadCardsIntoAllCards();
        DataManager.initializeAIDeck();
        DataManager.loadUsersData();
        DataManager.initializeAIDeck();
        APIServer.main(args);
        App.main(args);
        DataManager.saveUsersData();
    }
}
