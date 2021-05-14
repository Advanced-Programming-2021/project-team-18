package effects;

import events.Event;

public class DisableAttackerEffect extends Effect{
    private int requiredCountMonsters;
    private int maximumTimesPerTurn;

    public boolean permit(Event event) {

        return true;
    }
}
