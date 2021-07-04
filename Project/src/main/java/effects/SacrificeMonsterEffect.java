package effects;

import events.Event;

// TODO : KAMYAR
// Note: This effects seems to be deprecated since
// the cards that need it now seem to have their own
// exclusive effect.


public class SacrificeMonsterEffect extends Effect {
    private int requiredFromHand;
    private int requiredFromBoard;
    private int requiredFromHandOrBoard;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        return false;
    }

    public void consider(Event event) {

    }
}
