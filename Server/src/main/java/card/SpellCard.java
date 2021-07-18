package card;

import data.Printer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpellCard extends Card {
    private SpellType cardSpellType;
    public SpellCard() {

    }
    @Override
    public void showCard() {

    }

    @Override
    public Card cloneCard() {
        SpellCard card = new SpellCard();
        this.cloneDefaults(card);
        card.setCardSpellType(this.getCardSpellType());
        return card;
    }
}
