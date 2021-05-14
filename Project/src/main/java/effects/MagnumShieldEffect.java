package effects;

import events.Event;

public class MagnumShieldEffect extends Effect {

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
