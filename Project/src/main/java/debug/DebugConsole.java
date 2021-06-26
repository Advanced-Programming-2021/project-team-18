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

        for(Card card : Card.getAllCards())
            System.out.println("#" + card.getCardName() + "#");
        User a = getSampleUser1("a");
        User b = getSampleUser2("b");
        new Game(a , b , 1).runGame();
    }
    private static User getSampleUser1(String name) {
        User user = new User(name , name , name);
        GameDeck gameDeck = new GameDeck(name);
        Card sample = Card.getCardByName("Man-Eater Bug");
        for(int i = 0;i < 40;++ i)
            gameDeck.getMainDeck().addCard(sample);
        user.addGameDeck(gameDeck);
        user.setActiveDeckName(gameDeck.getName());
        return user;
    }
    private static User getSampleUser2(String name) {
        User user = new User(name , name , name);
        GameDeck gameDeck = new GameDeck(name);
        Card sample = Card.getCardByName("Horn Imp");
        for(int i = 0;i < 40;++ i)
            gameDeck.getMainDeck().addCard(sample);
        user.addGameDeck(gameDeck);
        user.setActiveDeckName(gameDeck.getName());
        return user;
    }
}
