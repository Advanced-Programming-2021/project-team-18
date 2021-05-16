package card;

import effects.Effect;
import events.Event;
import game.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter

public abstract class Card implements Comparable<Card> {
    private static final ArrayList<Card> allCards = new ArrayList<>();
    private static final ArrayList<String> allCardNames = new ArrayList<>();
    private String cardName;
    @Setter private int price;
    @Setter private String cardNumber;
    @Setter private String cardDescription;
    @Setter private boolean isFaceUp = true;
    @Setter private Player player;
    @Setter private Origin cardOrigin;
    @Setter private ArrayList<Effect> effects;

    public void setCardName(String cardName) {
        this.cardName = cardName;
        allCardNames.add(cardName);
    }

    public static Card getCardByName(String cardName) {
        for (Card card : allCards)
            if (card.getCardName().equals(cardName))
                return card;
        return null;
    }

    public static List<String> getAllCardNames() {
        return Collections.unmodifiableList(allCardNames);
    }

    public static ArrayList<Card> getAllCards() {
        return allCards;
    }

    public static Card createNewCard(String cardName) {
        return getCardByName(cardName).cloneCard();
    }

    public boolean hasEffect(Effect effect) {
        return effects.contains(effect);
    }

    public void cloneDefaults(Card card) {
        card.setPrice(this.getPrice());
        card.setCardName(this.getCardName());
        card.setCardNumber(this.getCardNumber());
        card.setCardDescription(this.getCardDescription());
        card.setEffects(this.getEffects());
    }

    public abstract Card cloneCard();

    public abstract void showCard();

    public abstract void runEffects(Event event);

    @Override
    public int compareTo(Card o) {
        return this.cardName.compareTo(o.cardName);
    }
}