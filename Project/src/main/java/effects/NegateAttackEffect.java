package effects;

import card.Card;
import data.Printer;
import events.AttackEvent;
import events.Event;
import events.SpellTrapActivationEvent;
import game.Player;


public class NegateAttackEffect extends Effect {

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof AttackEvent) {
            Card attacker = ((AttackEvent) event).getAttacker();
            Player player = attacker.getPlayer();
            if (player == selfPlayer.getOpponent()) {
                boolean playersChoice = selfPlayer.obtainConfirmation("do you want to activate Negate Attack effect?");
                Event activationEvent = new SpellTrapActivationEvent(selfCard);
                if (getPermissionFromAllEffects(activationEvent)) {
                    player.getOpponent().endBattlePhaseByEffect();
                    return false;
                }
                else Printer.prompt("some effect prevented trap activation");
            }
        }
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        isInConsideration = false;
    }
}
