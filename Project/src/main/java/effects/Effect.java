package effects;

import events.Event;

public abstract class Effect {

    public abstract boolean permit(Event event);
}
