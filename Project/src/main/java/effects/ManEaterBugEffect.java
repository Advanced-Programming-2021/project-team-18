package effects;

import card.Card;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;
import utility.Utility;

import java.util.ArrayList;
// by Pasha
// [man-eater bug]
public class ManEaterBugEffect extends Effect {
    private static String message = "which monster do you want to destroy with your man-eater bug?";
    private void activateEffect() {

        ArrayList<String> options = new ArrayList<>();
        for(int i = 1;i <= Player.getFIELD_SIZE();++ i)
            if(selfPlayer.getOpponent().getMonstersFieldList()[i] != null)
                options.add(i + " " + selfPlayer.getOpponent().getMonstersFieldList()[i].getCardName());
        if(options.size() <= 0)
            return ;
        String response = Utility.askPlayer(selfPlayer , message , options);
        selfPlayer.getOpponent().removeCardFromField(selfPlayer.getOpponent().getMonstersFieldList()[Integer.parseInt(String.valueOf(response.charAt(0)))] , selfCard);
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
            if(card.hasEffect(this) && cardEventInfo == CardEventInfo.FLIP) {
                activateEffect();
            }
        }
    }
}
