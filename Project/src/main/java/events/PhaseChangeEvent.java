package events;

import lombok.Getter;

@Getter
public class PhaseChangeEvent extends Event {
    private Phase phase;// phase ended
}
