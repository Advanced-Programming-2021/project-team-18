package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.CardEvent;
import events.CardEventInfo;
import events.Event;
import game.Deck;
import utility.Utility;

import java.util.ArrayList;

// by Pasha
// [advanced ritual art]
public class AdvancedRitualEffect extends Effect {
    private static String message = "which monster do you want to ritual summon ?";

    private void activateEffect() {
        ArrayList<Card> optionsCard = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();
        for(Card card : selfPlayer.getHand().getCardsList()) {
            boolean isRitualCard = false;
            for(Effect effect : card.getEffects())
                if(effect instanceof RitualSummonEffect)
                    isRitualCard = true;
            if(isRitualCard) {
                optionsCard.add(card);
                options.add(card.getCardName());
            }
        }
        if(optionsCard.isEmpty()) {
            Printer.prompt("no ritual card in your hand");
            selfPlayer.removeCardFromField(selfCard , null);
            return ;
        }
        String response = Utility.askPlayer(selfPlayer , message , options);
        for(Card card : optionsCard)
            if(card.getCardName().equals(response)) {
                selfPlayer.summonRitualMonster((MonsterCard) card);
                break ;
            }
        // todo ask player for deck cards
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if(event instanceof CardEvent) {
            CardEvent cardEvent = (CardEvent) event;
            CardEventInfo cardEventInfo = cardEvent.getInfo();
            Card card = cardEvent.getCard();
            if(card.hasEffect(this) && cardEventInfo == CardEventInfo.ACTIVATE_EFFECT) {
                activateEffect();
            }
        }
        return true;
    }
}
