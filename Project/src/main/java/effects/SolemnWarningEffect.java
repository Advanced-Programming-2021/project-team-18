package effects;

import card.MonsterCard;
import events.Event;
import events.SummonEvent;
import events.TurnChangeEvent;

public class SolemnWarningEffect extends Effect {
    boolean hasEnteredThisTurn = true;

    public boolean runEffect(MonsterCard summonedMonster) {
        if (hasEnteredThisTurn) return true;
        if (!selfPlayer.getOpponent().decreaseLifePoint(2000, selfCard))
            return true;
        selfPlayer.getOpponent().removeCardFromHand(summonedMonster);
        // Note that the "summoned" monster actually is not added to field yet!
        // So The function removeCardFromHand is called, instead of removeCardFromField
        return false;
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof TurnChangeEvent)
            hasEnteredThisTurn = false;
        if (event instanceof SummonEvent) {
            return runEffect(((SummonEvent) event).getMonster());
        }
        return true;
    }
}
