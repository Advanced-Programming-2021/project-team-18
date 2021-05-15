package effects;

import events.Event;
import events.LifePointChangeEvent;

// by Pasha
// cards with this effect : [exploder dragon]
public class DenyLifePointChangeEffect extends Effect {

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if(event instanceof LifePointChangeEvent) {
            LifePointChangeEvent lifePointChangeEvent = (LifePointChangeEvent) event;
            if(lifePointChangeEvent != null && lifePointChangeEvent.getAttackedTo() == selfCard) {
                return false;
            }
        }
        return true;
    }
}
