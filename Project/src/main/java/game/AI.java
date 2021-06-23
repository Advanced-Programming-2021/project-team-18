package game;

import card.Card;
import utility.Utility;

import java.util.ArrayList;

public class AI extends Player {

    private final ArrayList<Integer> indexes;

    public AI(User user, Deck deck) {
        super(user, deck);
        indexes = new ArrayList<>();
    }

    private Card getARandomElement(Card[] cardList) {
        indexes.clear();
        for (int i = 0; i < cardList.length; i++) {
            if (cardList[i] != null) indexes.add(i);
        }
        return cardList[Utility.getARandomNumber(indexes.size())];
    }

    private Card getARandomElement(Deck deck) {
        return deck.getCardsList().get(Utility.getARandomNumber(deck.getSize()));
    }

    @Override
    public void print(String str) {
    }

    @Override
    public boolean obtainConfirmation(String promptMassage) {
        if (promptMassage.matches(".*chain.*")) return false;
        return Utility.getARandomNumber(2) == 1;
    }

    @Override
    public Card obtainSpellTrapFromField() {
        return getARandomElement(spellsAndTrapFieldList);
    }

    @Override
    public Card obtainCardFromDeck(boolean showDeck) {
        return getARandomElement(remainingDeck);
    }

    @Override
    public Card obtainCardFromGraveYard() {
        return getARandomElement(graveyard);
    }

    @Override
    public Card obtainCardFromHand() {
        return getARandomElement(hand);
    }

    @Override
    public void setSpellOrTrap() {
    }

    @Override
    public void mainPhase1() {
        // Setting spells and traps
        // Import a monster
        // Chain: No answer
        // Attacking: The most powerful monster
        // NOTE: FOR SIMPLICITY WE DO NOT PUT RITUAL MONSTERS AND SPELLS IN OUR DECK!
    }
}

/*
Set a spell/trap
Set a monster
Import a monster (with or without tribute)
Attack
Answer opponent's card activations (in chain)
Activate spell/trap
Flip a monster

 */