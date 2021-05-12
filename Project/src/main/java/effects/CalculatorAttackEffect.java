package effects;

import events.Event;

public class CalculatorAttackEffect extends Effect{
    private int attackPerLevel;

    public boolean permit(Event event) {

        return true;
    }
}
