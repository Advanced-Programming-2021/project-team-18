package events;

import game.Player;
import lombok.Getter;

@Getter
public class PhaseEndedEvent extends Event {
    private Phase phase;
    private Player player;
    public PhaseEndedEvent(Phase phase , Player player) {
        this.phase = phase;
        this.player = player;
    }
}
