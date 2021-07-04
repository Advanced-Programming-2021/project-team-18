package effects;

import card.Card;
import events.*;

// by Pasha
// cards with this effect : [time seal]
// tested
public class DenyDrawCardEffect extends Effect {
    int turnsRemaining = 1;// should be initialized
    boolean isActive = false;

    public boolean permit(Event event) {
        if (event instanceof DrawCardEvent) {
            DrawCardEvent cardEvent = (DrawCardEvent) event;
            Card card = cardEvent.getCard();
            return !isActive || card.getPlayer() != selfPlayer.getOpponent() || turnsRemaining <= 0;
        }
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if (card.hasEffect(this) && (cardEventInfo == CardEventInfo.ACTIVATE_EFFECT)) {
                isActive = true;
            }
        }
        if (event instanceof PhaseEndedEvent) {
            PhaseEndedEvent phaseEndedEvent = (PhaseEndedEvent) event;
            if (phaseEndedEvent.getPhase() == Phase.DRAW && isActive && phaseEndedEvent.getPlayer() == selfPlayer)
                --turnsRemaining;
        }
        if (turnsRemaining == 0)
            selfPlayer.removeCardFromField(selfCard, null);
        isInConsideration = false;
    }
}
