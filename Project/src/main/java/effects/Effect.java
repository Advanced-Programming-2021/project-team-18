package effects;

import card.Card;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;
import lombok.Getter;
import lombok.Setter;

public abstract class Effect {
    protected Card selfCard;
    protected Player selfPlayer;
    @Getter @Setter
    protected boolean isInConsideration;
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
    public boolean getPermissionFromAllEffects(Event event) {
        return selfPlayer.getPermissionFromAllEffects(event);
    }
    public void notifyAllEffects(Event event) {
        selfPlayer.notifyAllEffectsForConsideration(event);
    }
    public abstract boolean permit(Event event);
    public abstract void consider(Event event);
}
