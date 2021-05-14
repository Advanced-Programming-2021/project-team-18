package effects;

import events.Event;

public class ScannerEffect extends Effect {

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
