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
}
