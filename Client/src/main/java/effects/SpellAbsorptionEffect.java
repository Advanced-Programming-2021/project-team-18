package effects;

import card.Card;
import card.SpellCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;

public class SpellAbsorptionEffect extends Effect {
    private boolean isActivated = false;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard.hasEffect(this) && selfPlayer == null) {
                selfPlayer = sourceCard.getPlayer();
                isActivated = true;
                return true;
            }
        }
        return true;
    }

    public void consider(Event event) {
        if (event instanceof CardEvent && isActivated) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard instanceof SpellCard) {
                selfPlayer.setLifePoint(selfPlayer.getLifePoint() + 500);
            }
        }
    }
}
