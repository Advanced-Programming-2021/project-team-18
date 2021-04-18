package effects;

import events.Event;

public class DestroyHighAttackMonsterEffect extends Effect {
    private int requiredAttack;
    private int opponentCount;
    private int selfCount;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        return false;
    }
}
