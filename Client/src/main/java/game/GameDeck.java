package game;
// TODO : KAMYAR

import card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter

public class GameDeck {
    private Deck mainDeck;
    private Deck sideDeck;
    private String name;

    public GameDeck(String name) {
        this.name = name;

        mainDeck = new Deck(60);
        sideDeck = new Deck(15);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameDeck)) return false;
        GameDeck gameDeck = (GameDeck) o;
        return Objects.equals(mainDeck, gameDeck.mainDeck) && Objects.equals(sideDeck, gameDeck.sideDeck) && name.equals(gameDeck.name);
    }
}
