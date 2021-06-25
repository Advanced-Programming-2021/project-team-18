package effects;

import card.Card;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;


// TODO : KAMYAR
public class TwinTwistersEffect extends Effect {

    public void runEffect() {
        Card card = selfPlayer.obtainCardFromHand();
        if (selfPlayer.getOpponent().getSpellCountOnField() == 0) {
            Printer.prompt("Your opponent does not have anymore spells");
            return;
        }
        Card firstSpell = selfPlayer.getOpponent().obtainSpellTrapFromField();
        selfPlayer.getOpponent().removeCardFromField(firstSpell, selfCard);
        if (selfPlayer.getOpponent().getSpellCountOnField() == 0) {
            Printer.prompt("Your opponent does not have anymore spells");
            return;
        }
        Card secondSpell = selfPlayer.getOpponent().obtainSpellTrapFromField();
        selfPlayer.getOpponent().removeCardFromField(secondSpell, selfCard);
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
                selfPlayer.forceRemoveCardFromField(selfCard);
            }
        }
    }
}
