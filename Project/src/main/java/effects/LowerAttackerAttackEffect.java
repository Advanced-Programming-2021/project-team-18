package effects;

import events.Event;

public class LowerAttackerAttackEffect extends Effect{
    private int numberOfTimesPerSummon;
    private int putAttackValue;

    public boolean permit(Event event) {

        return true;
    }
}
