package effects;

import card.Card;
import card.TrapCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

//Mirage Dragon
public class TrapActivationDenialEffect extends Effect {
    private boolean isActivated = false;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            Card sourceCard = cardEvent.getCard();
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            if ((cardEventInfo == CardEventInfo.ACTIVATE_EFFECT && sourceCard.hasEffect(this) && selfPlayer == null)
                    || (cardEventInfo == CardEventInfo.FLIP && sourceCard.hasEffect(this) && selfPlayer == null)) {
                selfPlayer = sourceCard.getPlayer();
                isActivated = true;
                return true;
            }
            if (sourceCard instanceof TrapCard
                    && sourceCard.getPlayer().equals(selfPlayer.getOpponent())
                    && isActivated) {
                if (cardEventInfo == CardEventInfo.ACTIVATE_EFFECT
                        || cardEventInfo == CardEventInfo.FLIP) return false;
            }
        }
        return true;
    }

    public void consider(Event event) {

    }
}
