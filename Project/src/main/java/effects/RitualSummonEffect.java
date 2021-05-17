package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.Event;
import events.SpellTrapActivationEvent;
import events.SummonEvent;
import game.Deck;
import utility.Utility;

import java.util.ArrayList;

// The only card which has this effect, is "Advanced Ritual Art"
public class RitualSummonEffect extends Effect {
    boolean hasRitualInHand = false;
    private MonsterCard selecetRitualMonsterToSummon() {
        for (Card card : selfPlayer.getHand().getCardsList()) {
            if (card instanceof MonsterCard) {
                if (((MonsterCard) card).isRitual()) hasRitualInHand = true;
            }
        }
        if (!hasRitualInHand) return null;
        Card selectedCard;
        while (true) {
            Printer.prompt("Please select ritual monster to summon.");
            selectedCard = selfPlayer.obtainCardFromHand();
            if (selectedCard instanceof MonsterCard) {
                if (((MonsterCard) selectedCard).isRitual()) {
                    return (MonsterCard) selectedCard;
                }
            }
            Printer.prompt("invalid input");
        }
    }

    // Returns true iff there was enough level for tribute the monster
    private boolean selectAndDeleteTributes(MonsterCard selectedMonster) {
        Deck playerDeck = selfPlayer.getHand();
        int levelSum = 0;
        for (Card card : playerDeck.getCardsList()) {
            if (card instanceof MonsterCard)
                levelSum += ((MonsterCard) card).getCardLevel();
        }
        int neededLevelSum = selectedMonster.getCardLevel();
        if (Utility.checkAndPrompt(
                levelSum < neededLevelSum,
                "insufficient stars for summoning the monster!")) return false;
        MonsterCard sacrificedMonster;
        Card deckCard;
        boolean firstSelection = true;
        while (neededLevelSum > 0) {
            Printer.prompt("Please choose a monster to tribute");
            deckCard = selfPlayer.obtainCardFromDeck(firstSelection);
            firstSelection = false;
            if (!(deckCard instanceof MonsterCard)) {
                Printer.prompt("Not a monster card!");
                continue;
            }
            sacrificedMonster = (MonsterCard) deckCard;
            if (sacrificedMonster == selectedMonster) {
                Printer.prompt("You cannot choose the card itself as tribute!");
                continue;
            }
            if (sacrificedMonster.isRitual()) {
                Printer.prompt("Only normal monsters can be selected as tribute!");
                continue;
            }
            selfPlayer.removeCardFromDeck(deckCard);
            neededLevelSum -= sacrificedMonster.getCardLevel();
        }
        selfPlayer.getRemainingDeck().shuffleDeck();
        return true;
    }

    private boolean runEffect() {
        MonsterCard selectedMonster = selecetRitualMonsterToSummon();
        if (selectedMonster == null) return false;
        SummonEvent newEvent = new SummonEvent(selectedMonster, true);
        if (!selfPlayer.getPermissionFromAllEffects(newEvent)) return false;
        if (!selectAndDeleteTributes(selectedMonster)) return false;
        selfPlayer.addMonsterCardToField(selectedMonster);
        selfPlayer.removeCardFromField(selfCard, selfCard);
        return true;
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof SpellTrapActivationEvent) {
            if (((SpellTrapActivationEvent) event).getCard() == selfCard) {
                return runEffect();
            }
        }
        return true;
    }
}
