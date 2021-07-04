package card;

import data.Printer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrapCard extends Card {
    private TrapType cardTrapType;

    @Override
    public void showCard() {
        String result = "Name: " + this.getCardName() + "\n";
        result += "Trap\n";
        result += "Type: " + this.getCardTrapType() + "\n";
        result += "Description: " + this.getCardDescription() + "\n";
        Printer.prompt(result);
    }

    @Override
    public Card cloneCard() {
        TrapCard card = new TrapCard();
        this.cloneDefaults(card);
        card.setCardTrapType(this.getCardTrapType());
        return card;
    }
}
