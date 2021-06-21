package effects;

import card.Card;
import card.SpellCard;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;
import utility.Utility;


// TODO : KAMYAR
public class TwinTwistersEffect extends Effect {

    public void runEffect() {
        Card card = selfPlayer.obtainCardFromHand();
        if (selfPlayer.getOpponent().getFirstEmptyPlaceOnSpellsField() == 1) {
            Printer.prompt("Your opponent does not have anymore spells");
            return;
        }
        SpellCard firstSpell = selfPlayer.getOpponent().obtainSpellCardFromHand();
        CardEvent firstDestruction = new CardEvent(firstSpell, CardEventInfo.DESTROYED, selfCard);
        if (selfPlayer.getPermissionFromAllEffects(firstDestruction)) {
            selfPlayer.getOpponent().removeCardFromHand(firstSpell);
            selfPlayer.notifyAllEffectsForConsideration(firstDestruction);
        }
        selfPlayer.removeCardFromHand(card);
        selfPlayer.getOpponent().removeCardFromHand(firstSpell);
        if (selfPlayer.getOpponent().getFirstEmptyPlaceOnSpellsField() == 1) {
            Printer.prompt("Your opponent does not have anymore spells");
            return;
        }
        SpellCard secondSpell = selfPlayer.getOpponent().obtainSpellCardFromHand();
        selfPlayer.getOpponent().removeCardFromHand(secondSpell);
        CardEvent secondDestruction = new CardEvent(secondSpell,CardEventInfo.DESTROYED,selfCard);
        if(selfPlayer.getPermissionFromAllEffects(secondDestruction)){
            selfPlayer.getOpponent().removeCardFromHand(secondSpell);
            selfPlayer.notifyAllEffectsForConsideration(secondDestruction);
        }
        selfPlayer.removeCardFromHand(selfCard);
    }

    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard.hasEffect(this) && selfPlayer == null) {
                selfPlayer = sourceCard.getPlayer();
                selfCard = sourceCard;
                runEffect();
            }
        }
    }
}
