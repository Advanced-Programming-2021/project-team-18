package effects;

import events.Event;

public class MessengerOfPeaceEffect extends Effect {



    public boolean permit(Event event) {

        return false;
    }
}
