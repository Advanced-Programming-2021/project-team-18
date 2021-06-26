package effects;

import events.AttackEvent;
import events.Event;

// By Sina
public class  MagicCylinderEffect extends Effect {

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
                    if (!selfCard.isFaceUp()) selfCard.setFaceUp(true);
                    selfPlayer.getOpponent().decreaseLifePoint(partEvent.getAttacker().getCardAttack(), null);
                    return false;
                }
            }
        }
        return true;
    }

    public void consider(Event event) {

    }
}
