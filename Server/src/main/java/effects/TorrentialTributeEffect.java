package effects;

import card.Card;
import card.MonsterCard;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;

public class TorrentialTributeEffect extends Effect {

    private void destroyPlayer(Player player) {
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i)
            if (player.getMonstersFieldList()[i] != null)
                player.removeCardFromField(player.getMonstersFieldList()[i], selfCard);
    }

    private void activateEffect() {
        if (!selfPlayer.obtainConfirmation("do you want to activate" +
                " your torrential Tribute ? (yes/no)")) return;
        destroyPlayer(selfPlayer);
        destroyPlayer(selfPlayer.getOpponent());
        selfPlayer.removeCardFromField(selfCard, null);
    }

    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        if (event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if (cardEventInfo == CardEventInfo.ENTRANCE && card instanceof MonsterCard) {
                MonsterCard monsterCard = (MonsterCard) card;
                if (monsterCard.isFaceUp()) {
                    activateEffect();
                }
            }
        }
        isInConsideration = false;
    }
}
