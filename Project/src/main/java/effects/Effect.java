package effects;

import card.Card;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

public abstract class Effect {
    protected Card selfCard;
    protected Player selfPlayer;

    public Effect() {
        selfCard = null;
        selfPlayer = null;
    }

    protected void initializeSelfCardWithEvent(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if (card.hasEffect(this)) {
                selfCard = card;
                selfPlayer = card.getPlayer();
            }
        }
    }
    public abstract boolean permit(Event event);
}
