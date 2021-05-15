package effects;

import card.Card;
import card.TrapCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

//Mirage Dragon
public class TrapActivationDenialEffect extends Effect {

    private Player player;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            Card sourceCard = cardEvent.getCard();
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            if ((cardEventInfo == CardEventInfo.ENTRANCE && sourceCard.hasEffect(this))
                    || (cardEventInfo == CardEventInfo.FLIP && sourceCard.hasEffect(this))) {
                player = sourceCard.getPlayer();
                return true;
            }
            if (sourceCard instanceof TrapCard && sourceCard.getPlayer().equals(player.getOpponent())) {
                if (cardEventInfo == CardEventInfo.ACTIVATE_EFFECT
                        || cardEventInfo == CardEventInfo.FLIP) return false;
            }
        }
        return true;
    }
}
