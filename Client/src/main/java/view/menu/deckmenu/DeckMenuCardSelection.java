package view.menu.deckmenu;

import card.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.Deck;
import game.GameDeck;
import game.User;
import javafx.fxml.Initializable;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;
import view.components.CardComponent;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class DeckMenuCardSelection extends View implements Initializable {
    private static final String GET_CARD_BALANCE = "/api/deckmenu/card_selection/get_card_balance";
    private static final String ADD_CARD_TO_MAIN_DECK = "/api/deckmenu/card_selection/add_card_to_main_deck";
    private static final String ADD_CARD_TO_SIDE_DECK = "/api/deckmenu/card_selection/add_card_to_side_deck";
    @Setter
    private static String currentGameDeckName;
    @Setter
    private static boolean isMainDeck;
    @Getter
    private static Card lastPickedCard;
    public CardComponent cardComponent;
    private int getCardBalance(String cardName) {
        HashMap<String,String> headers = new HashMap<>(){{put("token" , MenuController.getInstance().getToken());put("card_name" , cardName);}};
        String response = Utility.getRequest(Utility.getSERVER_LOCATION() + GET_CARD_BALANCE , null , headers);
        System.out.println(response);
        HashMap<String,String> jsonMap = (new Gson()).fromJson(response , new TypeToken<HashMap<String,String >>() {
        }.getType());
        int balance = Integer.valueOf(jsonMap.get("balance"));
        return balance;
    }
    @SneakyThrows
    public void selectCard() {
        if (cardComponent.getSelectedCardName() == null) {
            UtilityView.showError("no card was selected");
            loadView("deck_view");
            return;
        }
        lastPickedCard = Card.getCardByName(cardComponent.getSelectedCardName());
        assert lastPickedCard != null;
        HashMap<String , String> headers = new HashMap<>() {{put("token" , MenuController.getInstance().getToken()); put("deck_name" , currentGameDeckName); put("card_name" , lastPickedCard.getCardName());}};
        String location = Utility.getSERVER_LOCATION() + (isMainDeck ? ADD_CARD_TO_MAIN_DECK : ADD_CARD_TO_SIDE_DECK);
        String response = Utility.getRequest(location , null , headers);
        UtilityView.displayMessage(response);
        loadView("deck_view");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Card card : Card.getAllCards()) {
            int count = getCardBalance(card.getCardName());
            for (int i = 0; i < count; ++i)
                cardComponent.addCard(card);
        }
    }
}
