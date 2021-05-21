package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.*;
import utility.Utility;

public class SuijinEffect extends Effect {

    private MonsterCard sacrifice = null;
    private int lastAttackAmount;
    private boolean hasBeenCounteracted = false;

    private boolean obtainConfirmation() {
        String response;
        while (true) {
            Printer.prompt("Do you want to activate the Suijin Effect? (yes/no)");
            response = Utility.getNextLine();
            if (response.equals("yes")) return true;
            if (response.equals("no")) return false;
            Printer.prompt("invalid input");
        }
    }

    private boolean hasDoneSinceLastFaceUp() {
        return sacrifice != null;
    }

    private void runEffect(MonsterCard attacker) {
        sacrifice = attacker;
        lastAttackAmount = sacrifice.getCardAttack();
        sacrifice.setCardAttack(0);
    }

    private void counteractEffect() {
        if (hasBeenCounteracted) return;
        sacrifice.setCardAttack(lastAttackAmount);
        hasBeenCounteracted = true;
    }

    public boolean permit(Event event) {
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
                    hasBeenCounteracted = false;
                }
            }
        }
        return true;
    }

    public void consider(Event event) {

    }
}
