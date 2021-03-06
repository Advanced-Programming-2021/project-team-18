package game;

import card.Card;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

// TODO : PASHA

@Setter
@Getter
public class Deck {
    @Expose
    private int capacity;
    @Expose
    private ArrayList<Card> cardsList;
    private HashMap<String, Integer> cardCount; // NOTE : don't use this; only needed for gson

    public Deck(int capacity) {
        this.capacity = capacity;
        cardsList = new ArrayList<>();
    }

    public boolean isEmpty() {
        return cardsList.isEmpty();
    }

    public Card getCardByName(String cardName) {
        for (Card card : cardsList) {
            if (card.getCardName().equals(cardName)) return card;
        }
        return null;
    }

    public Card getRandomCard() {
        if (cardsList.isEmpty()) return null;
        return cardsList.get((new Random()).nextInt(cardsList.size()));
    }

    public Card pop() {
        if (cardsList.isEmpty()) return null;
        Card card = cardsList.get(cardsList.size() - 1);
        cardsList.remove(cardsList.size() - 1);
        return card;
    }

    public void shuffleDeck() {
        Collections.shuffle(cardsList, new Random(System.nanoTime()));
    }

    public void addCard(Card newCard) {
        if (cardsList.size() == capacity) return;
        cardsList.add(newCard);
    }

    public void removeCardAt(int index) {
        if (index >= cardsList.size() || index < 0) return;
        cardsList.remove(index);
    }

    public boolean removeCard(Card card) {
        return cardsList.remove(card);
    }

    public int getCardCount(String cardName) {
        int count = 0;
        for (Card myCard : cardsList)
            if (myCard.getCardName().equals(cardName))
                ++count;
        return count;
    }

    public int getSize() {
        return cardsList.size();
    }

    public Deck cloneDeck() {
        Deck deck = new Deck(this.capacity);
        for (Card card : cardsList)
            deck.getCardsList().add(card.cloneCard());
        return deck;
    }
}
