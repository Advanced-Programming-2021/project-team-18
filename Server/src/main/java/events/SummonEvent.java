package events;

import card.MonsterCard;
import lombok.Getter;

@Getter
public class SummonEvent extends Event {
    MonsterCard monster;
    boolean isSpecial = false; // Ritual Summons are also special

    public SummonEvent(MonsterCard monster) {
        this.monster = monster;
    }

    public SummonEvent(MonsterCard monster, boolean isSpecial) {
        this.monster = monster;
        this.isSpecial = isSpecial;
    }
}
