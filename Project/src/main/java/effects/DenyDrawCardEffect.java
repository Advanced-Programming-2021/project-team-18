package effects;

import card.Card;
import events.*;
import game.Player;

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
            if (isActive && card.getPlayer() == selfPlayer.getOpponent() && turnsRemaining > 0) {
                return false;
            }
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
        if (event instanceof PhaseChangeEvent) {
            PhaseChangeEvent phaseChangeEvent = (PhaseChangeEvent) event;
            if (phaseChangeEvent.getPhase() == Phase.DRAW && isActive && phaseChangeEvent.getPlayer() == selfPlayer)
                --turnsRemaining;
        }
        isInConsideration = false;
    }
}
