package card;

import data.Printer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrapCard extends Card {
    private TrapType cardTrapType;

    public TrapCard() {

    }

    @Override
    public void showCard() {

    }

    @Override
    public Card cloneCard() {
        TrapCard card = new TrapCard();
        this.cloneDefaults(card);
        card.setCardTrapType(this.getCardTrapType());
        return card;
    }
}
