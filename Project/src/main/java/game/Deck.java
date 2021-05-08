package game;

import card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// TODO : PASHA
@Getter
@Setter
public class Deck {

    private int capacity;
    private ArrayList<Card> cardsList;
    private HashMap<String,Integer> cardCount; // NOTE : dont use this only needed for gson
    public Deck() {
        cardsList = new ArrayList<>();
    }

    public boolean isEmpty() {
        return cardsList.isEmpty();
    }

    public Card pop() {
        if (cardsList.isEmpty()) return null;
        Card card = cardsList.get(cardsList.size() - 1);
        cardsList.remove(cardsList.size() - 1);
        return card;
    }

    public void addCard(Card newCard) {
        if (cardsList.size() == capacity) return;
        cardsList.add(newCard);
    }

    public void removeCard(int index) {
        if (index >= cardsList.size() || index < 0) return;
        cardsList.remove(index);
    }

    public int getCardCount(String cardName) {
        int count = 0;
        for (Card myCard : cardsList)
            if (myCard.getCardName().equals(cardName))
                ++count;
        return count;
    }

    public List<Card> getAllCards() {
        return Collections.unmodifiableList(cardsList);
    }
    public Deck cloneDeck() {
        Deck deck = new Deck();
        deck.setCapacity(this.getCapacity());
        for(Card card : this.getCardsList())
            deck.getCardsList().add(card.cloneCard());
        return deck;
    }
}
