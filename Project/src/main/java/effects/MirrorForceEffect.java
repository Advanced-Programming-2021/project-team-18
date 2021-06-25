package effects;

import events.AttackEvent;
import events.CardEvent;
import events.Event;
import game.Player;
import utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;

public class MirrorForceEffect extends Effect {


    private void activateEffect() {
        for(int i = 1; i <= Player.getFIELD_SIZE(); ++ i)
            if(selfPlayer.getOpponent().getMonstersFieldList()[i] != null)
                selfPlayer.getOpponent().removeCardFromField(selfPlayer.getOpponent().getMonstersFieldList()[i] , selfCard);
        selfPlayer.removeCardFromField(selfCard , null);
    }

    public boolean permit(Event event) {
        boolean result = true;
        isInConsideration = true;
        if(event instanceof AttackEvent) {
            AttackEvent attackEvent = (AttackEvent) event;
            if(attackEvent.getAttacker().getPlayer() != selfPlayer) {
                String message = "do you want to activate your mirror force ?";
                ArrayList<String> options = new ArrayList<>(Arrays.asList("yes" , "no"));
                String response = Utility.askPlayer(selfPlayer , message , options);
                if(response.equals("yes")) {
                    activateEffect();
                    result = false;
                }
            }
        }
        isInConsideration = false;
        return result;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        isInConsideration = false;
    }
}
