package events;

import card.Card;
import lombok.Getter;

@Getter
public class DrawCardEvent extends Event {
    private Card card;

    public DrawCardEvent(Card card) {
        this.card = card;
    }
}
