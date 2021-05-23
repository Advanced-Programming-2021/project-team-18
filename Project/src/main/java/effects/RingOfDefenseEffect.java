package effects;

import events.Event;

// Seems to be similar to DenyLifePointChangeEffect
public class RingOfDefenseEffect extends Effect {

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }

    public void consider(Event event) {

    }
}
