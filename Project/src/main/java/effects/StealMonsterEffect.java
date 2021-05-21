package effects;

import events.Event;

public class StealMonsterEffect extends Effect {

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }

    public void consider(Event event) {

    }

}
