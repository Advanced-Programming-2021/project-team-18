package effects;

import card.Card;
import data.Printer;
import events.Event;
import events.SpellTrapActivationEvent;
import events.TurnChangeEvent;
import menus.Menu;
import utility.Utility;

import javax.print.attribute.standard.MediaName;
import java.util.HashSet;
import java.util.Random;

public class MindCrushEffect extends Effect {
    boolean hasEnteredThisTurn = true;

    public boolean runEffect() {
        if (Utility.checkAndPrompt(hasEnteredThisTurn,
                "You cannot activate a trap at it's entrance turn!"))
            return false;
        String selectedCardName = null;
        int index = 0;
        int cardSize = Card.getAllCardNames().size();
        for (String cardName : Card.getAllCardNames()) {
            index ++;
            Printer.prompt(index + ". " + cardName);
        }
        String response;
        while (true) {
            Printer.prompt("Please enter a card number (in range 1 - " + cardSize + " )");
            response = Utility.getNextLine();
            if (response.matches("\\d{1,3}")) {
                if ((index = Integer.parseInt(response)) <= cardSize) {
                    selectedCardName = Card.getAllCardNames().get(index - 1);
                    break;
                }
            }
            Printer.prompt(Menu.INVALID_COMMAND);
        }
        Card selectedCard = selfPlayer.getOpponent().getHand().getCardByName(selectedCardName);
        if (selfPlayer.getOpponent().removeCardFromHand(selectedCard))
            return true;
        selfPlayer.removeCardFromHand(selfPlayer.getHand().getRandomCard());
        return true;
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof TurnChangeEvent) hasEnteredThisTurn = false;
        if (event instanceof SpellTrapActivationEvent) {
            if (((SpellTrapActivationEvent) event).getCard() == selfCard) {
                return runEffect();
            }
        }
        return true;
    }
}
