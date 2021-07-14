package debug;

import card.Card;
import data.DataManager;
import game.GameDeck;
import game.User;
import view.DebugStarter;
import view.menu.mainmenu.MainMenuView;

public class DebugGraphic {
    public static void main(String[] args) {
        DataManager.loadCardsIntoAllCards();
        DataManager.initializeAIDeck();
        DataManager.loadUsersData();

        User a = getSampleUser("asdffdsa");
        MainMenuView.setCurrentUser(a);
        DebugStarter.main(args);
    }

    private static User getSampleUser(String name) {
        User user = new User(name, name, name);
        GameDeck gameDeck1 = new GameDeck("yohoho");
        GameDeck gameDeck2 = new GameDeck("damn son");
        Card sample = Card.getCardByName("Fireyarou");
        for (int i = 0; i < 40; ++i) {
            gameDeck1.getMainDeck().addCard(sample);
        }
        gameDeck2.getMainDeck().addCard(sample);
        sample = Card.getCardByName("Axe Raider");
        gameDeck2.getMainDeck().addCard(sample);
        user.addGameDeck(gameDeck1);
        user.addGameDeck(gameDeck2);
        user.setActiveDeckName(gameDeck1.getName());
        user.setActiveDeckName(gameDeck2.getName());
        return user;
    }
}
