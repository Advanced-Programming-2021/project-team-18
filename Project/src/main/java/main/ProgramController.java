package main;

import data.DataManager;
import menus.LoginMenu;
import view.App;
import view.Starter;

public class ProgramController {

    public void runConsole() {
        DataManager.loadCardsIntoAllCards();
        DataManager.loadUsersData();
        new LoginMenu().runMenu();
        DataManager.saveUsersData();
    }
    public void runGraphic(String[] args) {
        DataManager.loadCardsIntoAllCards();
        DataManager.loadUsersData();
        App.main(args);
        DataManager.saveUsersData();
    }
}
