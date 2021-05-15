package events;

import game.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TurnChangeEvent extends Event{
    private Player player;
}
