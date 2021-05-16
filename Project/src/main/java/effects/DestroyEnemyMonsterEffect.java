package effects;

import events.Event;
import events.SpellTrapActivationEvent;
import game.Player;

// by Pasha
// cards with this effect : [raigeki , Dark Hole]
public class DestroyEnemyMonsterEffect extends Effect {
    private boolean destroyBothPlayers;// should be initialized
    private void destroyPlayer(Player player) {
        for(int i = 1;i <= Player.getFIELD_SIZE();++ i)
            if(player.getMonstersFieldList()[i] != null)
                player.destroyMonster(player.getMonstersFieldList()[i]);
    }
    private void activateEffect() {
        destroyPlayer(selfPlayer.getOpponent());
        if(destroyBothPlayers)
            destroyPlayer(selfPlayer);
    }
    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if(event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent spellTrapActivationEvent = (SpellTrapActivationEvent) event;
            if(spellTrapActivationEvent.getCard().hasEffect(this)) {
                activateEffect();
            }
        }
        return true;
    }
}
