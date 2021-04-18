package effects;

import card.MonsterCardType;
import events.Event;

public class AddAttackAndDefenseEffect extends Effect {
    private int attackAddValue;
    private int defenseAddValue;
    private MonsterCardType monsterType;
    private int attackAddedPerGraveyardMonsters;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        return false;
    }
}