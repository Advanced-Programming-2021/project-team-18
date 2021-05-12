package effects;

import events.Event;

public class AddToHandFromGraveyardEffect extends Effect {
    private int requiredHandRemoval;
    private int levelRequired;

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
