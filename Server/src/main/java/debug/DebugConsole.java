package debug;

import data.DataManager;
import data.api.APIServer;
import game.User;

public class DebugConsole {
    public static void main(String[] args) {
        DataManager.loadCardsIntoAllCards();
        DataManager.initializeAIDeck();
        DataManager.loadUsersData();
        APIServer.main(args);
    }
}
