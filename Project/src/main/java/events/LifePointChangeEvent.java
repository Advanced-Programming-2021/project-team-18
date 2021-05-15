package events;

import card.Card;
import game.Player;
import lombok.Getter;

@Getter
public class LifePointChangeEvent extends Event {
    private Player player;
    private Card attackedTo;// null if lifepoint change wasnt due to an attack
    private int amountAdded;
}
