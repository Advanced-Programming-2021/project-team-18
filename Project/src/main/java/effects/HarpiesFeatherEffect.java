package effects;

import events.Event;
import events.SpellTrapActivationEvent;
import game.Player;

// TODO : SINA
public class HarpiesFeatherEffect extends Effect {
    private int count;

    public void runEffect(Player opponent) {

    }

    public boolean permit(Event event) {
        if (event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent partEvent = (SpellTrapActivationEvent) event;
            if (partEvent.getCard().hasEffect(this))
                runEffect(partEvent.getCard().getPlayer().getOpponent());
        }
        return true;
    }
}
