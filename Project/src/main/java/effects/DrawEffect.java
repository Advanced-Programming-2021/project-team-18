package effects;

import card.SpellType;
import events.Event;
import events.SpellTrapActivationEvent;

public class DrawEffect extends Effect {
    private int drawCardCount;
    private SpellType spellType;

    public DrawEffect(int drawCardCount, SpellType spellType) {
        this.drawCardCount = drawCardCount;
        this.spellType = spellType;
    }

    private void activateEffect() {
        while (drawCardCount > 0) {
            drawCardCount--;
            selfPlayer.drawACard(spellType);
        }
        selfPlayer.removeCardFromField(selfCard, null);
    }

    public boolean permit(Event event) {
        return false;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        if (event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent spellTrapActivationEvent = (SpellTrapActivationEvent) event;
            if (spellTrapActivationEvent.getCard().hasEffect(this)) {
                activateEffect();
            }
        }
        isInConsideration = false;
    }
}
