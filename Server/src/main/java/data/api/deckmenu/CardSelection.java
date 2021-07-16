package data.api.deckmenu;

import card.Card;
import game.Deck;
import game.GameDeck;
import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;
import view.UtilityView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CardSelection {

    @RequestMapping(path = "api/deckmenu/card_selection/add_card_to_main_deck", method = RequestMethod.GET)
    @GetMapping
    public String addCardToMainDeck(@RequestHeader(value = "token") String token , @RequestHeader(value = "deck_name") String deckName , @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        Deck currentDeck = gameDeck.getMainDeck();
        return addCardToDeck(user , gameDeck , currentDeck , cardName);
    }
    @RequestMapping(path = "api/deckmenu/card_selection/add_card_to_side_deck", method = RequestMethod.GET)
    @GetMapping
    public String addCardToSideDeck(@RequestHeader(value = "token") String token , @RequestHeader(value = "deck_name") String deckName , @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        Deck currentDeck = gameDeck.getSideDeck();
        return addCardToDeck(user , gameDeck , currentDeck , cardName);
    }
    private String addCardToDeck(User user , GameDeck gameDeck , Deck currentDeck , String cardName) {
        if (currentDeck.getCardCount(cardName) == 3) {
            return "you can't add more than 3 cards of a type";
        } else if (user.getCardBalance(cardName) == gameDeck.getMainDeck().getCardCount(cardName) + gameDeck.getSideDeck().getCardCount(cardName)) {
            return "you already have all owned cards of this type in your deck";
        } else {
            currentDeck.addCard(Card.getCardByName(cardName));
            return "card added successfully";
        }
    }
    @RequestMapping(path = "api/deckmenu/card_selection/get_card_balance", method = RequestMethod.GET)
    @GetMapping
    public String getCardBalance(@RequestHeader(value = "token") String token , @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("balance" , user.getCardBalance(cardName) + "");
        return Utility.getJson(hashMap);
    }

}
