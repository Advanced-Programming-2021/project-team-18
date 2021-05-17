package events;

import card.Card;
import game.Player;
import lombok.Getter;

@Getter
public class LifePointChangeEvent extends Event {
    private Player player;
    private Card attackedTo;// null if lifepoint change wasnt due to an attack
    private int amountChanged;
    public LifePointChangeEvent(Player player , Card attackedTo , int amountChanged) {
        this.player = player;
        this.attackedTo = attackedTo;
        this.amountChanged = amountChanged;
    }
}
