package events;

import card.MonsterCard;
import lombok.Getter;

@Getter
public class AttackEvent extends Event {
    private MonsterCard attacker;
    private MonsterCard defender;
}
