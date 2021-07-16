package data.api.deckmenu;

import card.Card;
import game.GameDeck;
import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.util.ArrayList;

@RestController
public class SpecificDeckController {
    @RequestMapping(path = "api/deckmenu/specific_deck/get_main_deck_cards", method = RequestMethod.GET)
    @GetMapping
    public String getMainDeckNames(@RequestHeader(value = "token") String token , @RequestHeader(value = "name") String name) {
        User user = User.getUserByToken(token);
        GameDeck gameDeck = user.getGameDeckByName(name);
        ArrayList<String> cardNames = new ArrayList<>();
        for(Card card : gameDeck.getMainDeck().getCardsList())
            cardNames.add(card.getCardName());
        return Utility.getJson(cardNames);
    }
    @RequestMapping(path = "api/deckmenu/specific_deck/get_side_deck_cards", method = RequestMethod.GET)
    @GetMapping
    public String getSideDeckNames(@RequestHeader(value = "token") String token , @RequestHeader(value = "name") String name) {
        User user = User.getUserByToken(token);
        GameDeck gameDeck = user.getGameDeckByName(name);
        ArrayList<String> cardNames = new ArrayList<>();
        for(Card card : gameDeck.getSideDeck().getCardsList())
            cardNames.add(card.getCardName());
        return Utility.getJson(cardNames);
    }

    @RequestMapping(path = "api/deckmenu/specific_deck/remove_card_from_main_deck", method = RequestMethod.POST)
    @PostMapping
    public void removeCardFromMainDeck(@RequestHeader(value = "token") String token , @RequestHeader(value = "deck_name") String deckName , @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        gameDeck.getMainDeck().removeCard(Card.getCardByName(cardName));
    }
    @RequestMapping(path = "api/deckmenu/specific_deck/remove_card_from_side_deck", method = RequestMethod.POST)
    @PostMapping
    public void removeCardFromSideDeck(@RequestHeader(value = "token") String token , @RequestHeader(value = "deck_name") String deckName , @RequestHeader(value = "card_name") String cardName) {
        User user = User.getUserByToken(token);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        gameDeck.getSideDeck().removeCard(Card.getCardByName(cardName));
    }
    @RequestMapping(path = "api/deckmenu/specific_deck/set_as_active", method = RequestMethod.POST)
    @PostMapping
    public void setAsActive(@RequestHeader(value = "token") String token , @RequestHeader(value = "deck_name") String deckName) {
        User user = User.getUserByToken(token);
        user.setActiveDeckName(deckName);
    }

}
