package effects;

import data.Printer;
import events.AttackEvent;
import events.Event;
import events.Phase;
import events.PhaseEndedEvent;
import utility.Utility;


// By Sina
public class MessengerOfPeaceEffect extends Effect {
    private void consumeLP() {
        if (selfPlayer.getLifePoint() < 100) {
            selfPlayer.removeCardFromField(selfCard , null);
            return;
        }
        String response;
        while (true) {
            Printer.prompt("Would you like to spend 100 LPs, or destruct this card? (1 for LP, 2 for destruction)");
            response = Utility.getNextLine();
            if (response.equals("1")) {
                selfPlayer.decreaseLifePoint(100 , null);
                return;
            }
            if (response.equals("2")) {
                selfPlayer.removeCardFromField(selfCard , null);
                return;
            }
            Printer.prompt("invalid input");
        }
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
