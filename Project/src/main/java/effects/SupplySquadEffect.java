package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.*;
import game.Player;

public class SupplySquadEffect extends Effect {
    boolean hasActivatedEffectThisTurn;

    public void runEffect() {
        hasActivatedEffectThisTurn = true;
        if(selfPlayer.getRemainingDeck().isEmpty()){
            Printer.prompt("Your deck is empty so Supply Squad cannot draw a card for you");
            return;
        }
        if(selfPlayer.getHand().getCardsList().size() == 6){
            Printer.prompt("Your hand is full so Supply Squad cannot draw a card for you");
            return;
        }
        Card newCard = selfPlayer.getRemainingDeck().pop();
        DrawCardEvent drawCardEvent = new DrawCardEvent(newCard);
        if (!selfPlayer.getPermissionFromAllEffects(drawCardEvent)) {
            selfPlayer.getRemainingDeck().addCard(newCard);
        }
        selfPlayer.getHand().addCard(newCard);
    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT
                    && selfPlayer == null
                    && sourceCard.hasEffect(this)) {
                selfPlayer = sourceCard.getPlayer();
                hasActivatedEffectThisTurn = false;
                return true;
            } else if (info == CardEventInfo.DESTROYED
                    && !hasActivatedEffectThisTurn
                    && sourceCard instanceof MonsterCard
                    && sourceCard.getPlayer() == selfPlayer) {
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
