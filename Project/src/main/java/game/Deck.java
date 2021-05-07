package game;

import card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

// TODO : PASHA
@Getter
@Setter
public class Deck {

    private int capacity;
    private User owner;
    private ArrayList<Card> cardsList;

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
        cardsList.add(newCard);
    }

    public int getCardCount(String cardName) {
        int count = 0;
        for (Card myCard : cardsList)
            if (myCard.getCardName().equals(cardName))
                ++count;
        return count;
    }

    public void removeCard(int index) {
        if (index >= cardsList.size() || index < 0) return;
        cardsList.remove(index);
    }
}
