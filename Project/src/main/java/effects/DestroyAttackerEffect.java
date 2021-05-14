package effects;

import events.Event;

public class DestroyAttackerEffect extends Effect {

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
