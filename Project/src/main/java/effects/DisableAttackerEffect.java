package effects;

import events.Event;
import events.Phase;
import events.PhaseChangeEvent;
import game.Player;
// by pasha
// cards with this effect [Command Knight]
// tested Command Knight
public class DisableAttackerEffect extends Effect{
    private int requiredCountMonsters;
    private int maximumTimesPerTurn;
    private int timesPerThisTurn;
    public DisableAttackerEffect(int requiredCountMonsters , int maximumTimesPerTurn) {
        this.requiredCountMonsters = requiredCountMonsters;
        this.maximumTimesPerTurn = maximumTimesPerTurn;
        timesPerThisTurn = 0;
    }

    public boolean permit(Event event) {
        int countMonsters = 0;
        for(int i = 1;i <= Player.getFIELD_SIZE();++ i)
            if(selfPlayer.getMonstersFieldList()[i] != null)
                ++ countMonsters;
        if(countMonsters >= requiredCountMonsters && timesPerThisTurn ++ < maximumTimesPerTurn)
            return false;
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        if(event instanceof PhaseChangeEvent && ((PhaseChangeEvent) event).getPhase() == Phase.DRAW)
            timesPerThisTurn = 0;
        isInConsideration = false;
    }
}
