package effects;

import card.Card;
import card.SpellCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

public class SpellAbsorptionEffect extends Effect {


    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard.hasEffect(this) && selfPlayer == null) {
                selfPlayer = sourceCard.getPlayer();
                return true;
            }
        }
        return true;
    }

    public void consider(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard instanceof SpellCard) {
                selfPlayer.setLifePoint(selfPlayer.getLifePoint() + 500);
            }
        }
    }
}
