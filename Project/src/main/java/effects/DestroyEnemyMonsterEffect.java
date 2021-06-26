package effects;

import events.Event;
import events.SpellTrapActivationEvent;
import game.Player;

// by Pasha
// cards with this effect : [raigeki , Dark Hole]
// tested raigeki
public class DestroyEnemyMonsterEffect extends Effect {
    private boolean destroyBothPlayers; // should be initialized
    public DestroyEnemyMonsterEffect(boolean destroyBothPlayers) { this.destroyBothPlayers = destroyBothPlayers; }
    private void destroyPlayer(Player player) {
        for(int i = 1;i <= Player.getFIELD_SIZE();++ i)
            if(player.getMonstersFieldList()[i] != null)
                player.removeCardFromField(player.getMonstersFieldList()[i] , selfCard);
    }
    private void activateEffect() {
        destroyPlayer(selfPlayer.getOpponent());
        if(destroyBothPlayers)
            destroyPlayer(selfPlayer);
        selfPlayer.removeCardFromField(selfCard , null);
    }
    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        if(event instanceof SpellTrapActivationEvent) {
            SpellTrapActivationEvent spellTrapActivationEvent = (SpellTrapActivationEvent) event;
            if(spellTrapActivationEvent.getCard().hasEffect(this)) {
                activateEffect();
            }
        }
        isInConsideration = false;
    }
}
