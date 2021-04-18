
public class Effect {
    private Game game;		
    private Player owner;		
    private Player opponent;		
    private Card causedByCard;		
    private int speed;		

    
    public void runEffect() {
        
    }		
    
    public boolean permit(Event event) {

        return false;
    }		
}
