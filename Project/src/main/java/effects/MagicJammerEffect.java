package effects;

import card.Card;
import card.SpellCard;
import events.Event;
import events.SpellTrapActivationEvent;
import game.Player;

public class MagicJammerEffect extends Effect {

    private void runEffect(SpellCard spellCardToDelete) {
        Player opponent = selfPlayer.getOpponent();
        Card card = opponent.obtainCardFromHand();
        opponent.removeCardFromHand(card);
        opponent.removeCardFromField(spellCardToDelete);
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent partEvent = (SpellTrapActivationEvent) event;
            if (partEvent.getCard() instanceof SpellCard) {
                runEffect((SpellCard) partEvent.getCard());
                return false;
            }
        }
        return true;
    }
}
