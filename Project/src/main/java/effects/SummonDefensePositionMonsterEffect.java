package effects;

import events.Event;

public class SummonDefensePositionMonsterEffect extends Effect {
    private int maximumLevelAllowed;

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
