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
    private User user;
    public GameDeck(String name , User user) {
        this.name = name;
        this.user = user;
    }
    public boolean isDeckValid() {
        return false;
    }

}
