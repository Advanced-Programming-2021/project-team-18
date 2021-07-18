package debug;

import card.Card;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.DataManager;
import game.GameDeck;
import game.User;
import view.DebugStarter;
import view.menu.mainmenu.MainMenuView;

public class DebugGraphic {
    public static void main(String[] args) {
        DataManager.loadCardsIntoAllCards();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        User a = new User("a" , "a" , "a");
        a.addGameDeck(new GameDeck("a"));
        a.getGameDeckByName("a").getSideDeck().addCard(Card.getCardByName("Fireyarou"));
        String json = gson.toJson(a);
        System.out.println(json);
//        User b = gson.fromJson(json , User.class);
    }

}
