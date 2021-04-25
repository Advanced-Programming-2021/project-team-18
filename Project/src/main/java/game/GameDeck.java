package game;
// TODO : KAMYAR

import card.Card;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GameDeck {
    private Deck mainDeck;
    private Deck sideDeck;
    private String name;
    private User user;

    public GameDeck(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public boolean isDeckValid() {
        if (mainDeck.getCardsList().size() < 40 || mainDeck.getCardsList().size() > 60) return false;
        if (sideDeck.getCardsList().size() > 15) return false;
        for (Card i : mainDeck.getCardsList()) {
            if (mainDeck.getCardCount(i.getCardName()) + sideDeck.getCardCount(i.getCardName()) > 3) return false;
        }
        for (Card i : sideDeck.getCardsList()) {
            if (sideDeck.getCardCount(i.getCardName()) > 3) return false;
        }
        return true;
    }

}
