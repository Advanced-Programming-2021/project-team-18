package view.menu.deckmenu;

import card.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.GameDeck;
import game.User;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;
import view.components.CardComponent;

import javax.lang.model.type.ArrayType;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DeckMenuSpecificDeck extends View implements Initializable {
    private static final String GET_MAIN_DECK = "/api/deckmenu/specific_deck/get_main_deck_cards";
    private static final String GET_SIDE_DECK = "/api/deckmenu/specific_deck/get_side_deck_cards";
    private static final String ADD_CARD_TO_MAIN_DECK = "/api/deckmenu/specific_deck/add_card_to_main_deck";
    private static final String ADD_CARD_TO_SIDE_DECK = "/api/deckmenu/specific_deck/add_card_to_side_deck";
    private static final String REMOVE_CARD_FROM_MAIN_DECK = "/api/deckmenu/specific_deck/remove_card_from_main_deck";
    private static final String REMOVE_CARD_FROM_SIDE_DECK = "/api/deckmenu/specific_deck/remove_card_from_side_deck";
    private static final String SET_DECK_AS_ACTIVE = "/api/deckmenu/specific_deck/set_as_active";
    @Setter
    private static String currentDeckName;
    private ArrayList<String> mainDeckCardNames;
    private ArrayList<String> sideDeckCardNames;
    public CardComponent sideDeckCardComponent;
    public CardComponent mainDeckCardComponent;

    @SneakyThrows
    public void addCardToMainDeck(MouseEvent actionEvent) {
        DeckMenuCardSelection.setMainDeck(true);
        DeckMenuCardSelection.setCurrentGameDeckName(currentDeckName);
        loadView("deck_menu_select_card");
    }

    @SneakyThrows
    public void addCardToSideDeck(MouseEvent actionEvent) {
        DeckMenuCardSelection.setMainDeck(false);
        DeckMenuCardSelection.setCurrentGameDeckName(currentDeckName);
        loadView("deck_menu_select_card");
    }

    public void onRemoveMainDeckButton(MouseEvent actionEvent) {
        if (mainDeckCardComponent.getSelectedCardName() == null)
            return;
        HashMap<String,String> headers = new HashMap<>(){{ put("token" , MenuController.getInstance().getToken()); put("deck_name" , currentDeckName); put("card_name" , mainDeckCardComponent.getSelectedCardName());}};
        Utility.postRequest(Utility.getSERVER_LOCATION() + REMOVE_CARD_FROM_MAIN_DECK , null , headers);
        mainDeckCardComponent.removeCard(Card.getCardByName(mainDeckCardComponent.getSelectedCardName()));
    }

    public void onRemoveSideDeckButton(MouseEvent actionEvent) {
        if (sideDeckCardComponent.getSelectedCardName() == null)
            return;
        HashMap<String,String> headers = new HashMap<>(){{ put("token" , MenuController.getInstance().getToken()); put("deck_name" , currentDeckName); put("card_name" , sideDeckCardComponent.getSelectedCardName());}};
        Utility.postRequest(Utility.getSERVER_LOCATION() + REMOVE_CARD_FROM_SIDE_DECK , null , headers);
        sideDeckCardComponent.removeCard(Card.getCardByName(sideDeckCardComponent.getSelectedCardName()));
    }

    @SneakyThrows
    public void onBackButton(MouseEvent actionEvent) {
        loadView("deck_menu_deck_selection");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HashMap<String,String> mainDeckHeaders = new HashMap<>(){{ put("token" , MenuController.getInstance().getToken()); put("name" , currentDeckName);}};
        mainDeckCardNames = (new Gson()).fromJson(Utility.getRequest(Utility.getSERVER_LOCATION() + GET_MAIN_DECK , null , mainDeckHeaders) , new TypeToken<ArrayList<String>>() {
        }.getType());
        HashMap<String,String> sideDeckHeaders = new HashMap<>(){{ put("token" , MenuController.getInstance().getToken()); put("name" , currentDeckName);}};
        sideDeckCardNames = (new Gson()).fromJson(Utility.getRequest(Utility.getSERVER_LOCATION() + GET_SIDE_DECK , null , sideDeckHeaders) , new TypeToken<ArrayList<String>>() {
        }.getType());
        for(String cardName : mainDeckCardNames)
            mainDeckCardComponent.addCard(Card.getCardByName(cardName));
        for(String cardName : sideDeckCardNames)
            sideDeckCardComponent.addCard(Card.getCardByName(cardName));
    }

    public void setAsActiveDeck(MouseEvent actionEvent) {
        HashMap<String ,String > headers = new HashMap<>(){{put("token" , MenuController.getInstance().getToken()); put("deck_name" , currentDeckName);}};
        Utility.postRequest(Utility.getSERVER_LOCATION() + SET_DECK_AS_ACTIVE , null , headers);
        UtilityView.displayMessage("this deck was set as your active deck");
    }
}
