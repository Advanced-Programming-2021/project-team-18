package effects;

import card.Card;
import card.TrapCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;

public class TrapActivationDenialEffect extends Effect {

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            Card sourceCard = cardEvent.getCard();
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            if (sourceCard instanceof TrapCard) {
                if (cardEventInfo == CardEventInfo.ACTIVATE_EFFECT
                        || cardEventInfo == CardEventInfo.FLIP) return false;
            }
        }
        return true;
    }
}
