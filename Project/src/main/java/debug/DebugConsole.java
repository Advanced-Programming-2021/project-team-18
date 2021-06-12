package debug;

import card.Card;
import data.DataManager;
import game.Game;
import game.GameDeck;
import game.User;
import utility.Utility;

import java.util.HashMap;

public class DebugConsole {
    public static void main(String[] args) {
        DataManager.loadCardsIntoAllCards();
        DataManager.loadUsersData();

        User a = getSampleUser("a");
        User b = getSampleUser("b");
        new Game(a , b , 1).runGame();
    }
    private static User getSampleUser(String name) {
        User user = new User(name , name , name);
        GameDeck gameDeck = new GameDeck(name);
        Card sample = Card.getCardByName("Fireyarou");
        for(int i = 0;i < 40;++ i)
            gameDeck.getMainDeck().addCard(sample);
        user.addGameDeck(gameDeck);
        user.setActiveDeckName(gameDeck.getName());
        return user;
    }
}
