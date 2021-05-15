package effects;

import card.Card;
import card.MonsterCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;

// by Pasha
// cards with this effect : [Yomi ship , Exploder dragon]
public class DestroyAttackerEffect extends Effect {

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if(event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            Card causedByCard = cardEvent.getCausedByCard();
            if(cardEventInfo == CardEventInfo.DESTROYED && card == selfCard && causedByCard != null) {
                selfPlayer.getOpponent().destroyMonster((MonsterCard) causedByCard);
            }
        }
        return true;
    }
}
