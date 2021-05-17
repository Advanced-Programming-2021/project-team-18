package events;

import card.MonsterCard;
import lombok.Getter;

@Getter
public class AttackEvent extends Event {
    private MonsterCard attacker;
    private MonsterCard defender; // null if attacked direct
    public AttackEvent(MonsterCard attacker , MonsterCard defender) {
        this.attacker = attacker;
        this.defender = defender;
    }
}
