package main;

import data.DataManager;
import menus.LoginMenu;

// TODO : Pasha
public class ProgramController {

    public void run() {
        DataManager.loadCardsIntoAllCards();
        DataManager.loadUsersData();
        new LoginMenu().runMenu();
        DataManager.saveUsersData();
    }
}
