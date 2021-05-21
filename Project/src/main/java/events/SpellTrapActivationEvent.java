package events;

import card.Card;
import lombok.Getter;

public class SpellTrapActivationEvent extends Event {
    @Getter private Card card;
    public SpellTrapActivationEvent(Card card) {
        this.card = card;
    }
}
