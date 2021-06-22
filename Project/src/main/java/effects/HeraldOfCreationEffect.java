package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.Event;
import events.Phase;
import events.PhaseChangeEvent;
import utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;

// by Pasha
// [herald of creation]
public class HeraldOfCreationEffect extends Effect {

    private void activateEffect() {
        String message = "do you want to activate your use your herald of creation this turn ?";
        ArrayList<String> options = new ArrayList<>(Arrays.asList("yes" , "no"));
        String response = Utility.askPlayer(selfPlayer , message , options);
        if(response.equals("no")) return ;
        options = new ArrayList<>();
        for(Card card : selfPlayer.getHand().getCardsList())
            options.add(card.getCardName());
        message = "pick a card from your hand";
        response = Utility.askPlayer(selfPlayer , message , options);
        Card selectedCard = null;
        for(Card card : selfPlayer.getHand().getCardsList())
            if(response.equals(card.getCardName())) {
                selectedCard = card;
                break ;
            }
        selfPlayer.getHand().removeCard(selectedCard);
        message = "pick a monster from graveyard from your options";
        options = new ArrayList<>();
        for(Card card : selfPlayer.getGraveyard().getCardsList())
            if(card instanceof MonsterCard && ((MonsterCard) card).getCardLevel() >= 7)
                options.add(card.getCardName());
        if(options.size() == 0) {
            Printer.prompt("unfortunately no card for you to pick from");
            return ;
        }
        response = Utility.askPlayer(selfPlayer , message , options);
        selectedCard = null;
        for(Card card : selfPlayer.getGraveyard().getCardsList())
            if(response.equals(card.getCardName())) {
                selectedCard = card;
                break ;
            }
        selfPlayer.getGraveyard().removeCard(selectedCard);
        selfPlayer.getHand().addCard(selectedCard);
    }

    public boolean permit(Event event) {
        return true;
    }

    public void consider(Event event) {
        isInConsideration = true;
        initializeSelfCardWithEvent(event);
        if(event instanceof PhaseChangeEvent && selfCard.isFaceUp() &&((PhaseChangeEvent) event).getPhase() == Phase.DRAW && ((PhaseChangeEvent) event).getPlayer() == selfPlayer) {
            activateEffect();
        }
        isInConsideration = false;
    }
}
