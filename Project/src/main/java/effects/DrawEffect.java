package effects;

import card.SpellType;
import events.Event;

public class DrawEffect extends Effect {
    private int drawCardCount;
    private SpellType spellType;

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
