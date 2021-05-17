package events;

import card.Card;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CardEvent extends Event {
    private Card card;
    private CardEventInfo info;
    private Card causedByCard;// null if no card caused this event
    public CardEvent(Card card , CardEventInfo info , Card causedByCard) {
        this.card = card;
        this.info = info;
        this.causedByCard = causedByCard;
    }
}
