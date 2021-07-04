package effects;

import card.MonsterCard;
import events.*;

// By Sina

/*
ALGORITHM OF THIS EFFECT

Attack
    Confirmation?
        runEffect, faceUp if needed
    Else nothing
TurnChange
    Counteract
FaceUp, Entrance
    Reset
*/

public class SuijinEffect extends Effect {

    private MonsterCard sacrifice = null;
    private int lastAttackAmount;
    private boolean hasBeenCounteracted = true;

    private boolean obtainConfirmation() {
        return selfPlayer.obtainConfirmation(
                "Do you want to activate the Suijin Effect? (yes/no)");
    }

    private boolean hasDoneSinceLastFaceUp() {
        return sacrifice != null;
    }

    private void runEffect(MonsterCard attacker) {
        selfCard.setFaceUp(true);
        sacrifice = attacker;
        lastAttackAmount = sacrifice.getCardAttack();
        sacrifice.setCardAttack(0);
        hasBeenCounteracted = false;
    }

    private void counteractEffect() {
        if (hasBeenCounteracted) return;
        sacrifice.setCardAttack(lastAttackAmount);
        hasBeenCounteracted = true;
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        return true;
    }

    public void consider(Event event) {
        if (event instanceof AttackEvent) {
            AttackEvent partEvent = (AttackEvent) event;
            if (partEvent.getDefender().hasEffect(this)) {
                if (!hasDoneSinceLastFaceUp()) {
                    if (obtainConfirmation())
                        runEffect(partEvent.getAttacker());
                }
            }
        }
        if (event instanceof TurnChangeEvent) {
            counteractEffect();
        }
        if (event instanceof CardEvent) {
            CardEvent partEvent = (CardEvent) event;
            if (partEvent.getInfo() == CardEventInfo.FLIP ||
                    partEvent.getInfo() == CardEventInfo.ENTRANCE) {
                if (partEvent.getCard().hasEffect(this)) {
                    sacrifice = null;
                    hasBeenCounteracted = true;
                }
            }
        }
    }
}
