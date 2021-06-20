package events;

import game.Player;
import lombok.Getter;

@Getter
public class PhaseChangeEvent extends Event {
    private Phase phase;
    private Player player;
    public PhaseChangeEvent(Phase phase , Player player) {
        this.phase = phase;
        this.player = player;
    }
}
