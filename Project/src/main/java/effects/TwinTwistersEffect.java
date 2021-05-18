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
    Player player;

    public void runEffect() {
        int handSize = player.getHand().getSize();
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
                    || player.getHand().getCardsList().get(index) == null) Printer.prompt("Invalid number try again");
            else break;
        }
        index--;
        Card card = player.getHand().getCardsList().get(index);
        player.removeCardFromHand(card);
        int firstSpellIndex;
        if(player.getOpponent().getFirstEmptyPlaceOnSpellsField() == 1){
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
            if (firstSpellIndex < 1 || firstSpellIndex > 5 || player.getOpponent().getSpellsAndTrapFieldList()[firstSpellIndex] == null) Printer.prompt("Invalid number try again");
            else {
                Card firstSpell = player.getOpponent().getSpellsAndTrapFieldList()[firstSpellIndex];
                player.getOpponent().removeCardFromField(firstSpell , null);
                break;
            }
        }
        if(player.getOpponent().getFirstEmptyPlaceOnSpellsField() == 1){
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
            if (secondSpellIndex < 1 || secondSpellIndex > 5 || player.getOpponent().getSpellsAndTrapFieldList()[secondSpellIndex] == null) Printer.prompt("Invalid number try again");
            else {
                Card secondSpell = player.getOpponent().getSpellsAndTrapFieldList()[secondSpellIndex];
                player.getOpponent().removeCardFromField(secondSpell , null);
                return;
            }
        }
    }

    public boolean permit(Event event) {
        if (event instanceof CardEvent) {
            Card sourceCard = ((CardEvent) event).getCard();
            CardEventInfo info = ((CardEvent) event).getInfo();
            if (info == CardEventInfo.ACTIVATE_EFFECT && sourceCard.hasEffect(this) && player == null) {
                player = sourceCard.getPlayer();
                runEffect();
                return true;
            }
        }
        return true;
    }
}
