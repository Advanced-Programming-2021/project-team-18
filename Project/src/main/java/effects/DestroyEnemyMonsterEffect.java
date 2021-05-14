package effects;

import events.Event;

public class DestroyEnemyMonsterEffect extends Effect{
    private int monstersCount;

    public boolean permit(Event event) {

        return true;
    }
}
