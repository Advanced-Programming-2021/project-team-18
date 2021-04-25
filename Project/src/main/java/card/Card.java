package card;

import events.MonsterCardEvent;
import game.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter

public class Card implements Comparable<Card> {
    private static ArrayList<Card> allCards = new ArrayList<>();
    private int price;
    private String cardName;
    private String cardNumber;
    private String cardDescription;
    private boolean isFaceUp;
    private Player player;

    public static Card getCardByName(String cardName) {
        for (Card card : allCards)
            if (card.getCardName().equals(cardName))
                return card;
        return null;
    }

    public static ArrayList<Card> getAllCards() {
        return allCards;
    }

    public void showCard() {

    }

    public void runEffects(MonsterCardEvent event) {

    }


    @Override
    public int compareTo(Card o) {
        return this.cardName.compareTo(o.cardName);
    }
}