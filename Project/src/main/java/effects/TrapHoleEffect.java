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
        String message = "do you want to activate your trap hole by killing opponent " + monsterCard.getCardName() + "?";
        ArrayList<String> options = new ArrayList<>();
        options.add("no");
        options.add("yes");
        String response = Utility.askPlayer(selfPlayer , message , options);
        if(response.equals("no")) return ;
        selfPlayer.getOpponent().removeCardFromField(monsterCard , null);
        // effect event
        selfPlayer.removeCardFromField(selfCard , null);
    }
    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
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
            isInConsideration = false;
        }
    }
}
