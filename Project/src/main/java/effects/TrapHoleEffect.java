package effects;

import card.Card;
import card.MonsterCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import utility.Utility;

import java.util.ArrayList;

// by Pasha
// [Trap Hole]
public class TrapHoleEffect extends Effect {
    private static int minimumAttack = 1000;
    private void activateEffect(MonsterCard monsterCard) {
        selfPlayer.obtainConfirmation("do you want to activate your trap hole by killing opponent " + monsterCard.getCardName() + "?");
        selfPlayer.getOpponent().removeCardFromField(monsterCard , null);
    }
    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        initializeSelfCardWithEvent(event);
        if(event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if(((cardEventInfo == CardEventInfo.ENTRANCE && card.isFaceUp()) || cardEventInfo == CardEventInfo.FLIP) && card.getPlayer() != selfPlayer) {
                if(card instanceof MonsterCard && ((MonsterCard) card).getCardAttack() >= minimumAttack) {
                    activateEffect((MonsterCard) card);
                }
            }
        }
    }
}
