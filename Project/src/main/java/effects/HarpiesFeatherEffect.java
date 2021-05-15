package effects;

import card.Card;
import events.Event;
import events.SpellTrapActivationEvent;
import game.Player;

// TODO : SINA
public class HarpiesFeatherEffect extends Effect {
    private int count;

    public void runEffect(Player opponent) {
        Card[] list = opponent.getSpellsAndTrapFieldList();
        for (int i = 1; i < list.length; i++) {
            opponent.removeCardFromField(list[i]);
        }
        // Add the event of effect done
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent partEvent = (SpellTrapActivationEvent) event;
            if (partEvent.getCard().hasEffect(this))
                runEffect(partEvent.getCard().getPlayer().getOpponent());
        }
        return true;
    }
}
