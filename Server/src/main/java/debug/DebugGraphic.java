package debug;

import card.Card;
import card.CardSerializer;
import card.MonsterCard;
import card.MonsterCardType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.DataManager;
import game.Deck;
import game.Game;
import game.GameDeck;
import game.User;
import view.DebugStarter;
import view.menu.mainmenu.MainMenuView;

public class DebugGraphic {
    public static void main(String[] args) {
        DataManager.loadCardsIntoAllCards();
        Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Card.class , new CardSerializer()).create();
        User a = getSampleUser("a");
        User b = getSampleUser("b");
        Game game = new Game(a , b , 1);
        game.runGame();
        String serialized = gson.toJson(game);
        System.out.println(serialized);
        System.out.println(serialized.length());
        Game game2 = gson.fromJson(serialized , Game.class);
    }
    private static User getSampleUser(String name) {
        User sample = new User(name , name  ,name);
        GameDeck gameDeck = new GameDeck(name);
        Card card = Card.getCardByName("Fireyarou");
        for(int i = 0;i < 35;++ i)
            gameDeck.getMainDeck().addCard(card);
        sample.addGameDeck(gameDeck);
        sample.setActiveDeckName(name);
        return sample;
    }
}
