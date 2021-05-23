package effects;

import events.Event;


// Note: This effect seems to be deprecated because it seems that
// the cards that need to have it now have their own exclusive effect.

public class NegateAttackEffect extends Effect {

    public void runEffect() {

    }

    public boolean permit(Event event) {

        return false;
    }

    public void consider(Event event) {

    }
}
