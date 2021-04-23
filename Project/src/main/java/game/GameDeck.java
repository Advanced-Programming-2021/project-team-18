package game;
// TODO : KAMYAR

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GameDeck {
    private Deck mainDeck;
    private Deck sideDeck;
    private String name;
    private User owner;

    public boolean isDeckValid() {
        return false;
    }

}
