package effects;

import events.AttackEvent;
import events.Event;
import events.Phase;
import events.PhaseEndedEvent;


// By Sina
public class MessengerOfPeaceEffect extends Effect {
    private void consumeLP() {
        if (selfPlayer.getLifePoint() < 100) {
            selfPlayer.removeCardFromField(selfCard, null);
            return;
        }
        if (selfPlayer.obtainConfirmation("Would you like to spend 100 LPs," +
                " or destruct this card? (yes for LP, no for destruction)"))
            selfPlayer.decreaseLifePoint(100, null);
        else selfPlayer.removeCardFromField(selfCard, null);
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof AttackEvent) {
            AttackEvent partEvent = (AttackEvent) event;
            if (partEvent.getAttacker().getPlayer() != selfPlayer) {
                return partEvent.getDefender().getCardAttack() < 1500;
            }
        }

        return true;
    }

    public void consider(Event event) {
        if (event instanceof PhaseEndedEvent) {
            if (((PhaseEndedEvent) event).getPhase() == Phase.STANDBY) {
                consumeLP();
            }
        }
    }
}
