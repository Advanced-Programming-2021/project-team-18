package effects;

import card.MonsterCardType;
import events.Event;

public class SummonSpecialTypeEffect extends Effect{
    private MonsterCardType monsterType;
    private boolean handAllowed;
    private boolean deckAllowed;
    private boolean graveyardAllowed;
    private boolean opponentGraveyardAllowed;

    public boolean permit(Event event) {

        return true;
    }

    public void consider(Event event) {

    }
}
