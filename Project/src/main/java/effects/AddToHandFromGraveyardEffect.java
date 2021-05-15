package effects;

import events.Event;
// by Pasha
public class AddToHandFromGraveyardEffect extends Effect {
    private int requiredHandRemoval;
    private int levelRequired;
    private int tributeCount;

    public boolean permit(Event event) {
        return false;
    }
}
