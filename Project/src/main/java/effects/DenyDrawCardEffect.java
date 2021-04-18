package effects;
import events.Event;
public class DenyDrawCardEffect extends Effect {
    
    public boolean permit(Event event) {
        return false;
    }		
}
