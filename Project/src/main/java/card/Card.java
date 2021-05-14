package card;

import effects.Effect;
import events.Event;
import game.Player;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter

public abstract class Card implements Comparable<Card> {
    private static ArrayList<Card> allCards = new ArrayList<>();
    private int price;
    private String cardName;
    private String cardNumber;
    private String cardDescription;
    private boolean isFaceUp = true;
    private Player player;
    private Origin cardOrigin;
    private ArrayList<Effect> effects;

    public static Card getCardByName(String cardName) {

        for (Card card : allCards)
            if (card.getCardName().equals(cardName))
                return card;
        return null;
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
    }
    public abstract Card cloneCard();
    public abstract void showCard();
    public abstract void runEffects(Event event);

    @Override
    public int compareTo(Card o) {
        return this.cardName.compareTo(o.cardName);
    }
}