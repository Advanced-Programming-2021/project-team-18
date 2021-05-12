package effects;

import events.Event;

// TODO : KAMYAR
public class SacrificeMonsterEffect extends Effect {
    private int requiredFromHand;
    private int requiredFromBoard;
    private int requiredFromHandOrBoard;

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
