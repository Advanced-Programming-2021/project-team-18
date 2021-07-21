package effects;

import card.Card;
import card.MonsterCard;
import data.Printer;
import events.Event;
import events.SpellTrapActivationEvent;
import events.SummonEvent;
import game.Deck;
import utility.Utility;
import view.UtilityView;

// By Sina
// Note that the only card which has this effect, is "Advanced Ritual Art"
public class RitualSummonEffect extends Effect {

    private boolean hasRitualInHand() {
        for (Card card : selfPlayer.getHand().getCardsList()) {
            if (card instanceof MonsterCard) {
                if (((MonsterCard) card).isRitual()) return true;
            }
        }
        return false;
    }

    private boolean hasEnoughStarsToTribute() {
        int minStars = Integer.MAX_VALUE;
        for (Card card : selfPlayer.getHand().getCardsList()) {
            if (card instanceof MonsterCard &&
                    ((MonsterCard) card).isRitual()) {
                minStars = Math.min(minStars, ((MonsterCard) card).getCardLevel());
            }
        }
        Deck playerDeck = selfPlayer.getHand();
        int levelSum = 0;
        for (Card card : playerDeck.getCardsList()) {
            if (card instanceof MonsterCard)
                levelSum += ((MonsterCard) card).getCardLevel();
        }
        return levelSum >= minStars;
    }

    private MonsterCard selectRitualMonsterToSummon() {
        Card selectedCard;
        UtilityView.displayMessage("Please select ritual monster to summon.", selfPlayer.getUser());
        selectedCard = selfPlayer.obtainCardFromHand();
        while (true) {
            if (selectedCard instanceof MonsterCard) {
                if (((MonsterCard) selectedCard).isRitual()) {
                    return (MonsterCard) selectedCard;
                }
            }
            UtilityView.displayMessage("invalid input", selfPlayer.getUser());
        }
    }

    private void runEffect() {
        MonsterCard selectedMonster = selectRitualMonsterToSummon();
        SummonEvent newEvent = new SummonEvent(selectedMonster, true);
        if (selfPlayer.getPermissionFromAllEffects(newEvent)) {
            selectAndDeleteTributes(selectedMonster);
            selfPlayer.addMonsterCardToField(selectedMonster);
            selectedMonster.setFaceUp(true);
            selectedMonster.setDefenseMode(selfPlayer.obtainConfirmation(
                    "Do you want to summon the monster in defence position? (yes/no)"));
        }
        selfPlayer.removeCardFromField(selfCard, selfCard);
    }

    private void selectAndDeleteTributes(MonsterCard selectedMonster) {
        int neededLevelSum = selectedMonster.getCardLevel();
        MonsterCard sacrificedMonster;
        Card deckCard;
        boolean firstSelection = true;
        while (neededLevelSum > 0) {
            UtilityView.displayMessage("Please choose a monster to tribute", selfPlayer.getUser());
            deckCard = selfPlayer.obtainCardFromDeck(firstSelection);
            firstSelection = false;
            if (!(deckCard instanceof MonsterCard)) {
                UtilityView.showError(selfPlayer.getUser(), "Not a monster card!");
                continue;
            }
            sacrificedMonster = (MonsterCard) deckCard;
            if (sacrificedMonster == selectedMonster) {
                UtilityView.showError(selfPlayer.getUser(), "You cannot choose the card itself as tribute!");
                continue;
            }
            if (sacrificedMonster.isRitual()) {
                UtilityView.showError(selfPlayer.getUser(), "Only normal monsters can be selected as tribute!");
                continue;
            }
            selfPlayer.removeCardFromDeck(deckCard);
            neededLevelSum -= sacrificedMonster.getCardLevel();
        }
        selfPlayer.getRemainingDeck().shuffleDeck();
    }

    public boolean permit(Event event) {
        initializeSelfCardWithEvent(event);
        if (event instanceof SpellTrapActivationEvent) {
            if (((SpellTrapActivationEvent) event).getCard() == selfCard) {
                if (Utility.checkAndPrompt(selfPlayer.getUser(), !hasRitualInHand(),
                        "You have no ritual monster in hand!")) return false;
                return !Utility.checkAndPrompt(selfPlayer.getUser(), !hasEnoughStarsToTribute(),
                        "You don't have enough stars to tribute!");
            }
        }
        return true;
    }

    public void consider(Event event) {
        if (event instanceof SpellTrapActivationEvent) {
            if (((SpellTrapActivationEvent) event).getCard() == selfCard) {
                runEffect();
            }
        }
    }
}
