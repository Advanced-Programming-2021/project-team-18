package effects;

import data.Printer;
import events.AttackEvent;
import events.Event;
import utility.Utility;

public class MagicCylinderEffect extends Effect {

    private boolean obtainConfirmation() {
        return selfPlayer.obtainConfirmation(
                "Do you want to activate Magic Cylinder? (yes/no)");
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof AttackEvent) {
            AttackEvent partEvent = (AttackEvent) event;
            if (partEvent.getDefender() == selfCard) {
                if (obtainConfirmation()) {
                    if (!selfCard.isFaceUp())  selfCard.setFaceUp(true);
                    selfPlayer.getOpponent().decreaseLifePoint(
                            partEvent.getAttacker().getCardAttack()
                    );
                    return false;
                }
            }
        }
        return true;
    }
}
