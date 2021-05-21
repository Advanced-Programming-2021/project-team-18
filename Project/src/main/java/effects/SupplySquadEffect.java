package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.*;
import game.Player;

public class SupplySquadEffect extends Effect {
    Player player;
    boolean hasActivatedEffectThisTurn;

    public void runEffect() {
        hasActivatedEffectThisTurn = true;
        if(player.getRemainingDeck().isEmpty()){
            Printer.prompt("Your deck is empty so Supply Squad cannot draw a card for you");
            return;
        }
        if(player.getHand().getCardsList().size() == 6){
            Printer.prompt("Your hand is full so Supply Squad cannot draw a card for you");
            return;
        }
        Card newCard = player.getRemainingDeck().pop();
        DrawCardEvent drawCardEvent = new DrawCardEvent(newCard);
        if (!player.getPermissionFromAllEffects(drawCardEvent)) {
            player.getRemainingDeck().addCard(newCard);
        }
        player.getHand().addCard(newCard);
    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT
                    && player == null
                    && sourceCard.hasEffect(this)) {
                player = sourceCard.getPlayer();
                hasActivatedEffectThisTurn = false;
                return true;
            } else if (info == CardEventInfo.DESTROYED
                    && !hasActivatedEffectThisTurn
                    && sourceCard instanceof MonsterCard
                    && sourceCard.getPlayer() == player) {
                runEffect();
            }
        }
        if(event instanceof TurnChangeEvent){
            hasActivatedEffectThisTurn = false;
        }
        return true;
    }

    public void consider(Event event) {

    }
}
