package effects;

import card.Card;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Player;
import utility.Utility;


// TODO : KAMYAR
public class TwinTwistersEffect extends Effect {

    public void runEffect() {
        int handSize = selfPlayer.getHand().getSize();
        int index;
        while (true) {
            Printer.prompt("Please choose a number from 1 to " + handSize + " so the card would be moved to graveYard and spell would be activated");
            try {
                index = Integer.parseInt(Utility.getNextLine());
            } catch (NumberFormatException e) {
                Printer.prompt("You didn't input a number");
                continue;
            }
            if (index < 1
                    || index > handSize
                    || selfPlayer.getHand().getCardsList().get(index) == null)
                Printer.prompt("Invalid number try again");
            else break;
        }
        index--;
        Card card = selfPlayer.getHand().getCardsList().get(index);
        selfPlayer.removeCardFromHand(card);
        int firstSpellIndex;
        if (selfPlayer.getOpponent().getFirstEmptyPlaceOnSpellsField() == 1) {
            Printer.prompt("Your opponent does not have anymore spells");
            return;
        }
        while (true) {
            Printer.prompt("now choose the first opponent's spell card to be destroyed (Your input needs to be a single number between 1 and 5)");
            try {
                firstSpellIndex = Integer.parseInt(Utility.getNextLine());
            } catch (NumberFormatException e) {
                Printer.prompt("You didn't input a number");
                continue;
            }
            if (firstSpellIndex < 1 || firstSpellIndex > 5 || selfPlayer.getOpponent().getSpellsAndTrapFieldList()[firstSpellIndex] == null)
                Printer.prompt("Invalid number try again");
            else {
                Card firstSpell = selfPlayer.getOpponent().getSpellsAndTrapFieldList()[firstSpellIndex];
                selfPlayer.getOpponent().removeCardFromField(firstSpell, null);
                break;
            }
        }
        if (selfPlayer.getOpponent().getFirstEmptyPlaceOnSpellsField() == 1) {
            Printer.prompt("Your opponent does not have anymore spells");
            return;
        }
        int secondSpellIndex;
        while (true) {
            Printer.prompt("now choose the second opponent's spell card to be destroyed (Your input needs to be a single number between 1 and 5)");
            try {
                secondSpellIndex = Integer.parseInt(Utility.getNextLine());
            } catch (NumberFormatException e) {
                Printer.prompt("You didn't input a number");
                continue;
            }
            if (secondSpellIndex < 1 || secondSpellIndex > 5 || selfPlayer.getOpponent().getSpellsAndTrapFieldList()[secondSpellIndex] == null)
                Printer.prompt("Invalid number try again");
            else {
                Card secondSpell = selfPlayer.getOpponent().getSpellsAndTrapFieldList()[secondSpellIndex];
                selfPlayer.getOpponent().removeCardFromField(secondSpell, null);
                return;
            }
        }
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
