package effects;

import events.Event;
// by Pasha
public class DestroyAttackerEffect extends Effect {

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
