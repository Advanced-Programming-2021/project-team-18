package effects;

import game.Game;
import game.Player;
import events.Event;
import card.Card;

public class Effect {
    private Game game;
    private Player owner;
    private Player opponent;
    private Card causedByCard;
    private int speed;


    public void runEffect() {

    }

    public boolean permit(Event event) {

        return false;
    }
}
