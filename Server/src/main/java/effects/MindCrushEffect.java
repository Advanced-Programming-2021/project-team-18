package effects;

import card.Card;
import data.Printer;
import events.Event;
import events.SpellTrapActivationEvent;
import events.TurnChangeEvent;
import utility.Utility;


// By Sina
public class MindCrushEffect extends Effect {
    private boolean hasEnteredThisTurn = true;

    private void runEffect() {
        int index = 0;
        int cardSize = Card.getAllCardNames().size();
        // todo change displaying cards
//        for (String cardName : Card.getAllCardNames()) {
//            index++;
//            Printer.prompt(index + ". " + cardName);
//        }
        String selectedCardName = Card.getAllCardNames().get(selfPlayer.obtainNumberInRange(
                1, cardSize + 1, "Please enter a card number (in range 1 - " + cardSize + " )"));
        Card selectedCard = selfPlayer.getOpponent().getHand().getCardByName(selectedCardName);
        boolean opponentContainsThisCard = false;
        while (selfPlayer.getOpponent().removeCardFromHand(selectedCard))
            opponentContainsThisCard = true;
        if (!opponentContainsThisCard)
            selfPlayer.removeCardFromHand(selfPlayer.getHand().getRandomCard());
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof SpellTrapActivationEvent) {
            if (((SpellTrapActivationEvent) event).getCard() == selfCard) {
                return !Utility.checkAndPrompt(hasEnteredThisTurn,
                        "You cannot activate a trap at it's entrance turn!");
            }
        }
        return true;
    }

    public void consider(Event event) {
        if (event instanceof TurnChangeEvent)
            hasEnteredThisTurn = false;
        if (event instanceof SpellTrapActivationEvent) {
            if (((SpellTrapActivationEvent) event).getCard() == selfCard) {
                runEffect();
            }
        }
    }
}
