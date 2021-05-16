package effects;

import card.Card;
import card.SpellCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

public class SpellAbsorptionEffect extends Effect {

    Player player;

    public void runEffect() {

    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard.hasEffect(this)) {
                player = sourceCard.getPlayer();
                return true;
            }
            else if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard instanceof SpellCard){
                player.setLifePoint(player.getLifePoint() + 500);
                return  true;
            }
        }
        return true;
    }
}
