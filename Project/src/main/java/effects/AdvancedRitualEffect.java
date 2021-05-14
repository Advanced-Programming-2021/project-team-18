package effects;

import events.Event;

public class AdvancedRitualEffect extends Effect {


    public boolean permit(Event event) {
        return false;
    }
}
