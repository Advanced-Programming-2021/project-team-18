package data.api.deckmenu;

import game.GameDeck;
import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.util.ArrayList;

@RestController
public class DeckSelectionController {

    @RequestMapping(path = "api/deckmenu/deck_selection/get_deck_names" , method = RequestMethod.GET)
    @GetMapping
    public String getDeckNames(@RequestHeader(value = "token") String token) {
        User user = User.getUserByToken(token);
        ArrayList<String> deckNames = new ArrayList<>();
        for(GameDeck gameDeck : user.getDecks())
            deckNames.add(gameDeck.getName());
        return Utility.getJson(deckNames);
    }
    @RequestMapping(path = "api/deckmenu/deck_selection/create_new_deck" , method = RequestMethod.POST)
    @PostMapping
    public void addDeck(@RequestHeader(value = "token") String token , @RequestHeader(value = "name") String deckName) {
        User user = User.getUserByToken(token);
        user.addGameDeck(new GameDeck(deckName));
    }
    @RequestMapping(path = "api/deckmenu/deck_selection/remove_deck" , method = RequestMethod.POST)
    @PostMapping
    public void removeDeck(@RequestHeader(value = "token") String token , @RequestHeader(value = "name") String deckName) {
        User user = User.getUserByToken(token);
        user.removeGameDeck(user.getGameDeckByName(deckName));
    }
}
