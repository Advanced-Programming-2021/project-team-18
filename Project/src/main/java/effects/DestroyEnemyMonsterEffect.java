package effects;

import events.Event;
// by Pasha
public class DestroyEnemyMonsterEffect extends Effect{
    private int monstersCount;

    public boolean permit(Event event) {

        return true;
    }
}
