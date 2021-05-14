package effects;

import events.Event;

public class AttackAndDefenseEquipEffect extends Effect {
    private int addAttack;
    private int addDefense;

    public void runEffect() {

    }
    public boolean permit(Event event) {
        return false;
    }
}
