package effects;

import card.Card;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;

//Change of Heart
public class StealMonsterEffect extends Effect {

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
        }
        return true;
    }

    public void consider(Event event) {

    }

}
