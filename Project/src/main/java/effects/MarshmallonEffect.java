package effects;

import events.AttackEvent;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

// By Sina
// This effect has similarities with SolemnWarningEffect
public class MarshmallonEffect extends Effect{
    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof CardEvent) {
            CardEvent partEvent = (CardEvent) event;
            if (partEvent.getInfo() == CardEventInfo.DESTROYED) {
                return !partEvent.getCard().hasEffect(this);
            }
        }
        return true;
    }

    public void consider(Event event) {
        if (event instanceof AttackEvent) {
            AttackEvent partEvent = (AttackEvent) event;
            if (partEvent.getDefender().hasEffect(this)) {
                if (!partEvent.getDefender().isFaceUp()) {
                    Player opponent = partEvent.getAttacker().getPlayer();
                    opponent.decreaseLifePoint(1000, null);
                }
            }
        }
    }
}
