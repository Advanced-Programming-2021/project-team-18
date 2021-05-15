package events;

import card.Card;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CardEvent extends Event {
    private Card causedByCard;// null if no card caused this event
    private Card card;
    private CardEventInfo info;
}
