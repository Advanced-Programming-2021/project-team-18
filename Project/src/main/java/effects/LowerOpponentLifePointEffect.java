package effects;

import events.Event;

public class LowerOpponentLifePointEffect extends Effect{
    private int amountLost;

    public boolean permit(Event event) {

        return true;
    }
}
